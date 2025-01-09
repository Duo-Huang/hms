package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonMapper;
import me.huangduo.hms.dao.HomeMemberRolesMapper;
import me.huangduo.hms.dao.HomesMapper;
import me.huangduo.hms.dao.RolesMapper;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dao.entity.PermissionEntity;
import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.request.MemberInfoUpdateRequest;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class HomeMemberServiceImpl implements HomeMemberService {

    private final HomeMemberRolesMapper homeMemberRolesMapper;

    private final HomesMapper homesMapper;

    private final CommonMapper commonMapper;

    private final RolesMapper rolesMapper;

    public HomeMemberServiceImpl(HomeMemberRolesMapper homeMemberRolesMapper, HomesMapper homesMapper, CommonMapper commonMapper, RolesMapper rolesMapper) {
        this.homeMemberRolesMapper = homeMemberRolesMapper;
        this.homesMapper = homesMapper;
        this.commonMapper = commonMapper;
        this.rolesMapper = rolesMapper;
    }

    @Override
    public void inviteMember(Integer homeId, User user) throws RecordNotFoundException {
        // TODO: 事件驱动
    }

    @Override
    public void addMember(Integer homeId, User user) throws RecordNotFoundException, HomeMemberAlreadyExistsException {
        HomeEntity homeEntity = homesMapper.getById(homeId);
        if (Objects.isNull(homeEntity)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }

        if (Objects.isNull(commonMapper.getUserIdById(user.getUserId()))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_204);
            log.error("This user does not existed.", e);
            throw e;
        }

        List<HomeMemberRoleEntity> homeMembers = homeMemberRolesMapper.getItemsByHomeId(homeId);

        if (homeMembers.stream().anyMatch(x -> x.getUserId().equals(user.getUserId()))) {
            BusinessException e = new HomeMemberAlreadyExistsException(HmsErrorCodeEnum.HOME_ERROR_205);
            log.error("This home member is already existed", e);
            throw e;
        }
        // assign a default role for this member
        RoleEntity homeMemberRole = rolesMapper.getByName("home member");
        Integer roleId = null;
        if (Objects.isNull(homeMemberRole)) {
            log.warn("The member is not assigned a default role");
        } else {
            roleId = homeMemberRole.getRoleId();
        }

        homeMemberRolesMapper.addUserToTheHome(HomeMemberRoleEntity.builder().userId(user.getUserId()).homeId(homeId).roleId(roleId).memberName(user.getNickname()).build());
    }

    @Override
    public void removeMember(Integer homeId, Integer userId) throws RecordNotFoundException {
        HomeEntity homeEntity = homesMapper.getById(homeId);
        if (Objects.isNull(homeEntity)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }
        int row = homeMemberRolesMapper.removeMemberFromTheHome(HomeMemberRoleEntity.builder().userId(userId).homeId(homeId).build());
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't existed", e);
            throw e;
        }
    }

    @Override
    public void updateMemberInfo(Integer homeId, Integer userId, MemberInfoUpdateRequest memberInfoUpdateRequest) throws RecordNotFoundException {
        HomeEntity homeEntity = homesMapper.getById(homeId);
        if (Objects.isNull(homeEntity)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }

        int row = homeMemberRolesMapper.updateMemberName(HomeMemberRoleEntity.builder().userId(userId).homeId(homeId).memberName(memberInfoUpdateRequest.memberName()).build());
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist", e);
            throw e;
        }
    }

    @Override
    public List<HomeEntity> getHomesForMember(Integer userId) {
        List<Integer> homeIds = homeMemberRolesMapper.getHomeIdsByUserId(userId);
        if (homeIds.isEmpty()) {
            return List.of();
        }
        return homesMapper.getByIds(homeIds);
    }

    @Override
    public List<Member> getMembersWithRolesForHome(Integer homeId) throws RecordNotFoundException {
        HomeEntity homeEntity = homesMapper.getById(homeId);
        if (Objects.isNull(homeEntity)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }

        return homeMemberRolesMapper.getMembersWithRolesByHomeId(homeId);
    }

    @Override
    public void assignRoleForHomeMember(Integer homeId, Integer userId, Integer roleId) throws RecordNotFoundException {
        HomeEntity homeEntity = homesMapper.getById(homeId);
        if (Objects.isNull(homeEntity)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home does not existed.", e);
            throw e;
        }

        int row = homeMemberRolesMapper.updateRoleForTheMember(HomeMemberRoleEntity.builder().userId(userId).homeId(homeId).roleId(roleId).build());
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist", e);
            throw e;
        }
    }

    @Override
    public void removeRoleForHomeMember(Integer homeId, Integer userId) throws RecordNotFoundException {
        assignRoleForHomeMember(homeId, userId, null);
    }

    @Override
    public RoleEntity getHomeMemberRole(Integer homeId, Integer userId) throws RecordNotFoundException {
        return null;
    }

    @Override
    public List<PermissionEntity> getHomeMemberPermissions(Integer roleId) throws RecordNotFoundException {
        return null;
    }

    @Override
    public void assignPermissionsForHomeMember(Integer roleId, Integer[] permissionIds) throws RecordNotFoundException {

    }

    @Override
    public void removePermissionsForHomeMember(Integer roleId, Integer[] permissionIds) throws RecordNotFoundException {

    }
}
