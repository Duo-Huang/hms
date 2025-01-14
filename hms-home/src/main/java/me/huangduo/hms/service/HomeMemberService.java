package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;

import java.util.List;

public interface HomeMemberService {

    void inviteMember(Integer homeId, User user) throws RecordNotFoundException; // 发送邀请信息

    void addMember(Integer homeId, User user) throws RecordNotFoundException, HomeMemberAlreadyExistsException; // 接受邀请信息

    void removeMember(Member member) throws RecordNotFoundException;

    void updateMemberInfo(Member member) throws RecordNotFoundException; // 修改家庭成员的信息, 不是user的信息

    List<Home> getHomesForUser(User user);

    List<Member> getMembersWithRolesForHome(Integer homeId) throws RecordNotFoundException; // 包含角色信息

    void assignRoleForMember(Member member, Integer roleId) throws RecordNotFoundException;

    void removeRoleForMember(Member member) throws RecordNotFoundException;
}
