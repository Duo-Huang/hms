package me.huangduo.hms.service;

import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dao.entity.PermissionEntity;
import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.request.MemberInfoUpdateRequest;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;

import java.util.List;

interface HomeMemberService {

    void inviteMember(Integer homeId, User user) throws RecordNotFoundException; // 发送邀请信息

    void addMember(Integer homeId, User user) throws RecordNotFoundException, HomeMemberAlreadyExistsException; // 接受邀请信息

    void removeMember(Integer homeId, Integer userId) throws RecordNotFoundException;

    void updateMemberInfo(Integer homeId, Integer userId, MemberInfoUpdateRequest memberInfoUpdateRequest) throws RecordNotFoundException; // 修改家庭成员的名称, 不是user的信息

    List<HomeEntity> getHomesForMember(Integer userId);

    List<Member> getMembersWithRolesForHome(Integer homeId) throws RecordNotFoundException;

    void assignRoleForHomeMember(Integer homeId, Integer userId, Integer roleId) throws RecordNotFoundException;

    void removeRoleForHomeMember(Integer homeId, Integer userId) throws RecordNotFoundException;

    RoleEntity getHomeMemberRole(Integer homeId, Integer userId) throws RecordNotFoundException;

    List<PermissionEntity> getHomeMemberPermissions(Integer roleId) throws RecordNotFoundException;

    void assignPermissionsForHomeMember(Integer roleId, Integer[] permissionIds) throws RecordNotFoundException;

    void removePermissionsForHomeMember(Integer roleId, Integer[] permissionIds) throws RecordNotFoundException;
}
