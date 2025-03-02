package me.huangduo.hms.service;

import me.huangduo.hms.exceptions.IllegalAssignRoleException;
import me.huangduo.hms.model.Home;
import me.huangduo.hms.model.Member;
import me.huangduo.hms.model.User;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.InvitationCodeExpiredException;
import me.huangduo.hms.exceptions.RecordNotFoundException;

import java.util.List;

public interface HomeMemberService {

    void inviteUser(Integer homeId, User inviter, User invitee) throws RecordNotFoundException, IllegalArgumentException; // 发送邀请信息

    void acceptInvitation(User user, String invitationCode) throws IllegalArgumentException, RecordNotFoundException, InvitationCodeExpiredException, HomeMemberAlreadyExistsException;

    void addMember(Integer homeId, User user) throws IllegalArgumentException; // 接受邀请信息

    void removeMember(Member member) throws RecordNotFoundException;

    Member getMemberWithRole(Integer homeId, Integer userId) throws RecordNotFoundException;

    void updateMemberInfo(Member member) throws RecordNotFoundException; // 修改家庭成员的信息, 不是user的信息

    List<Home> getHomesForUser(User currentUser) throws IllegalArgumentException; // TODO: pageable

    List<Member> getMembersWithRoles(Integer homeId) throws RecordNotFoundException; // TODO: pageable

    void assignRoleForMember(User currentUser, Member member, Integer roleId, boolean isInitialAssign) throws RecordNotFoundException, IllegalArgumentException, IllegalAssignRoleException;

    void removeRoleForMember(User currentUser, Member member) throws RecordNotFoundException, IllegalArgumentException;
}
