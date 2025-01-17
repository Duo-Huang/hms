package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.HomeMemberRolesDao;
import me.huangduo.hms.dao.HomesDao;
import me.huangduo.hms.dao.RolesDao;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.SystemRole;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.enums.HmsSystemRole;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.mapper.HomeMapper;
import me.huangduo.hms.mapper.MemberMapper;
import me.huangduo.hms.utils.InvitationCoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeMemberServiceImpl implements HomeMemberService {

    private final HomeMemberRolesDao homeMemberRolesDao;

    private final HomesDao homesDao;

    private final CommonService commonService;

    private final RolesDao rolesDao;

    private final HomeMapper homeMapper;

    private final MemberMapper memberMapper;

    public HomeMemberServiceImpl(
            HomeMemberRolesDao homeMemberRolesDao,
            HomesDao homesDao,
            CommonService commonService,
            RolesDao rolesDao,
            HomeMapper homeMapper,
            MemberMapper memberMapper
            ) {
        this.homeMemberRolesDao = homeMemberRolesDao;
        this.homesDao = homesDao;
        this.commonService = commonService;
        this.rolesDao = rolesDao;
        this.homeMapper = homeMapper;
        this.memberMapper = memberMapper;
    }

    @Override
    public void inviteUser(Integer homeId, Integer inviterId, User user) throws RecordNotFoundException {
        final User userInfo = checkUserExisted(user.getUsername()); // check and get all newest user info

        List<HomeMemberRoleEntity> homeMembers = homeMemberRolesDao.getItemsByHomeId(homeId);

        if (homeMembers.stream().anyMatch(x -> x.getUserId().equals(userInfo.getUserId()))) {
            BusinessException e = new HomeMemberAlreadyExistsException(HmsErrorCodeEnum.HOME_ERROR_205);
            log.error("This home member is already existed.", e);
            throw e;
        }
        String invitationCode = InvitationCoder.userIdToCode(inviterId);
        log.info("invitationCode: {}, invitee userId: {}", invitationCode, userInfo.getUserId());

        // TODO: 事件驱动
        /*
         * 1. 创建一个消息表(通用, 未来支持更多的系统消息),消息做成家庭内广播即可,每条消息都有type 如果有收件人, 只有收件人可以操作消息, 其他人只能看到
         * 2. 存储一个邀请type的消息, 包含消息id, 邀请码, inviterId 收件人inviteeId, 接受状态, 只有收件人可以接受邀请
         * 3. 发布一个事件,
         * */

    }

    @Override
    public void acceptInvitation(User user, String invitationCode) throws IllegalArgumentException {
        // 校验,用invitationCode和加入家庭type的消息查询消息表,如果消息没过期并且未处理,然后比对被邀请人inviteeId是否是当前user, 不是就抛IllegalArgumentException
        Integer inviterId = InvitationCoder.codeToUserId(invitationCode);
        Integer inviterHomeId = homeMemberRolesDao.getHomeIdByUserId(inviterId);
        addMember(inviterHomeId, user);
        // assign a default home member role for this user
        SystemRole homeMemberRole = commonService.getSystemRoleByName(HmsSystemRole.HOME_MEMBER);
        assignRoleForMember(Member.builder().homeId(inviterHomeId).userId(user.getUserId()).build(), homeMemberRole.getRoleId());
    }

    @Override
    public void addMember(Integer homeId, User user) throws RecordNotFoundException, HomeMemberAlreadyExistsException {
        final User userInfo = checkUserExisted(user.getUserId()); // check and get all newest user info

        List<HomeMemberRoleEntity> homeMembers = homeMemberRolesDao.getItemsByHomeId(homeId);

        if (homeMembers.stream().anyMatch(x -> x.getUserId().equals(userInfo.getUserId()))) {
            BusinessException e = new HomeMemberAlreadyExistsException(HmsErrorCodeEnum.HOME_ERROR_205);
            log.error("This home member is already existed.", e);
            throw e;
        }

        homeMemberRolesDao.add(
                HomeMemberRoleEntity.builder()
                        .userId(userInfo.getUserId())
                        .homeId(homeId)
                        .memberName(userInfo.getNickname())
                        .build()
        );
    }

    @Override
    public void removeMember(Member member) throws RecordNotFoundException {
        int row = homeMemberRolesDao.removeItemByUserIdAndHomeId(memberMapper.toEntity(member));

        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't existed.", e);
            throw e;
        }
    }

    @Override
    public Member getMemberWithRole(Integer homeId, User user) throws RecordNotFoundException {
        return homeMemberRolesDao.getMemberWithRoleByHomeIdAndUserId(homeId, user.getUserId());
    }

    @Override
    public void updateMemberInfo(Member member) throws RecordNotFoundException {
        int row = homeMemberRolesDao.updateMemberNameByUserIdAndHomeId(memberMapper.toEntity(member));
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public List<Home> getHomesForUser(User user) {
        List<Integer> homeIds = homeMemberRolesDao.getHomeIdsByUserId(user.getUserId());
        if (homeIds.isEmpty()) {
            return List.of();
        }
        List<HomeEntity> homeEntities = homesDao.getByIds(homeIds);
        return homeEntities.stream().map(homeMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Member> getMembersWithRoles(Integer homeId) throws RecordNotFoundException {
        List<Member> members = homeMemberRolesDao.getMembersWithRolesByHomeId(homeId);
        members.forEach(x -> {
            if (x.getRole().getRoleId() == null) {
                x.setRole(null);
            }
        });

        return members;
    }

    @Override
    public void assignRoleForMember(Member member, Integer roleId) throws RecordNotFoundException {
        if (roleId != null && Objects.isNull(rolesDao.getById(roleId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_208);
            log.error("This role does not exist.", e);
            throw e;
        }

        HomeMemberRoleEntity homeMemberRoleEntity = memberMapper.toEntity(member);
        homeMemberRoleEntity.setRoleId(roleId);

        int row = homeMemberRolesDao.updateRoleByUserIdAndHomeId(homeMemberRoleEntity);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public void removeRoleForMember(Member member) throws RecordNotFoundException {
        assignRoleForMember(member, null);
    }

    private User checkUserExisted(Integer userId) {
        User user = null;
        if (userId == null || Objects.isNull(user = commonService.getUserById(userId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_204);
            log.error("This user does not existed.", e);
            throw e;
        }
        return user;
    }

    private User checkUserExisted(String username) {
        User user = null;
        if (username == null || Objects.isNull(user = commonService.getUserByName(username))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_204);
            log.error("This user does not existed.", e);
            throw e;
        }
        return user;
    }
}
