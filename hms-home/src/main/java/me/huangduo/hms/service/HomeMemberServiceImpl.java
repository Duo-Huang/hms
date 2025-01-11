package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.dao.HomeMemberRolesDao;
import me.huangduo.hms.dao.HomesDao;
import me.huangduo.hms.dao.RolesDao;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.enums.HmsSystemRole;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.mapper.HomeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeMemberServiceImpl implements HomeMemberService {

    private final HomeMemberRolesDao homeMemberRolesDao;

    private final HomesDao homesDao;

    private final CommonDao commonDao;

    private final RolesDao rolesDao;

    private final HomeMapper homeMapper;

    public HomeMemberServiceImpl(HomeMemberRolesDao homeMemberRolesDao, HomesDao homesDao, CommonDao commonDao, RolesDao rolesDao, HomeMapper homeMapper) {
        this.homeMemberRolesDao = homeMemberRolesDao;
        this.homesDao = homesDao;
        this.commonDao = commonDao;
        this.rolesDao = rolesDao;
        this.homeMapper = homeMapper;
    }

    @Override
    public void inviteMember(Integer homeId, User user) throws RecordNotFoundException {
        if (homeId == null || Objects.isNull(homesDao.getById(homeId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }
        // TODO: 事件驱动
       /*
       * 1. 创建一个消息表(通用, 未来支持更多的系统消息)
       * 2. 存储一个邀请信息, 消息做成家庭内广播即可, 如果有收件人, 只有收件人可以操作消息, 其他人只能看到
       * 3. 发布一个事件,
       * */

    }

    @Override
    public void addMember(Integer homeId, Integer userId) throws RecordNotFoundException, HomeMemberAlreadyExistsException {
        if (homeId == null || Objects.isNull(homesDao.getById(homeId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }

        User user = null;
        if (userId == null || Objects.isNull(user = commonDao.getUserInfoById(userId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_204);
            log.error("This user does not existed.", e);
            throw e;
        }

        List<HomeMemberRoleEntity> homeMembers = homeMemberRolesDao.getItemsByHomeId(homeId);

        if (homeMembers.stream().anyMatch(x -> x.getUserId().equals(userId))) {
            BusinessException e = new HomeMemberAlreadyExistsException(HmsErrorCodeEnum.HOME_ERROR_205);
            log.error("This home member is already existed", e);
            throw e;
        }
        // assign a default home member role for this member
        RoleEntity homeMemberRole = rolesDao.getSystemRoleByName(HmsSystemRole.HOME_MEMBER.getRoleName());
        Integer roleId = null;

        if (Objects.isNull(homeMemberRole)) {
            log.warn("The member is not assigned a default role.");
        } else {
            roleId = homeMemberRole.getRoleId();
        }

        homeMemberRolesDao.addUserToTheHome(
                HomeMemberRoleEntity.builder()
                        .userId(user.getUserId())
                        .homeId(homeId)
                        .roleId(roleId)
                        .memberName(user.getNickname())
                        .build()
        );
    }

    @Override
    public void removeMember(Member member) throws RecordNotFoundException {
        if (member.getHomeId() == null || Objects.isNull(homesDao.getById(member.getHomeId()))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("The member's home does not exist.", e);
            throw e;
        }
        int row = homeMemberRolesDao.removeMemberFromTheHome(
                HomeMemberRoleEntity.builder()
                        .userId(member.getUserId())
                        .homeId(member.getHomeId())
                        .build()
        );

        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't existed", e);
            throw e;
        }
    }

    @Override
    public void updateMemberInfo(Member member) throws RecordNotFoundException {
        if (member.getHomeId() == null || Objects.isNull(homesDao.getById(member.getHomeId()))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("The member's home does not exist.", e);
            throw e;
        }

        int row = homeMemberRolesDao.updateMemberName(
                HomeMemberRoleEntity.builder()
                        .userId(member.getUserId())
                        .homeId(member.getHomeId())
                        .memberName(member.getMemberName())
                        .build()
        );
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public List<Home> getHomesForMember(Member member) {
        List<Integer> homeIds = homeMemberRolesDao.getHomeIdsByUserId(member.getUserId());
        if (homeIds.isEmpty()) {
            return List.of();
        }
        List<HomeEntity> homeEntities = homesDao.getByIds(homeIds);
        return homeEntities.stream().map(homeMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Member> getMembersForHome(Integer homeId) throws RecordNotFoundException {
        if (homeId == null || Objects.isNull(homesDao.getById(homeId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }

        return homeMemberRolesDao.getMembersWithRolesByHomeId(homeId);
    }

    @Override
    public void assignRoleForMember(Member member, Integer roleId) throws RecordNotFoundException {
        if (member.getHomeId() == null || Objects.isNull(homesDao.getById(member.getHomeId()))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("The member's home does not exist.", e);
            throw e;
        }

        if (roleId != null && Objects.isNull(rolesDao.getById(roleId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_208);
            log.error("This role does not exist.", e);
            throw e;
        }

        int row = homeMemberRolesDao.updateRoleForTheMember(
                HomeMemberRoleEntity.builder()
                        .userId(member.getUserId())
                        .homeId(member.getHomeId())
                        .roleId(roleId)
                        .build()
        );
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist", e);
            throw e;
        }
    }

    @Override
    public void removeRoleForMember(Member member) throws RecordNotFoundException {
        assignRoleForMember(member, null);
    }
}
