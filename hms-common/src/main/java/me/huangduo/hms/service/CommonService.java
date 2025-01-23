package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.SystemRole;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsSystemRole;

import java.util.List;

public interface CommonService {

    User getUserById(Integer userId);

    User getUserByName(String username);

    Home getHomeById(Integer homeId);

    boolean isUserInHome(Integer homeId, Integer userId);

    SystemRole getSystemRoleByName(HmsSystemRole hmsSystemRole);

    List<SystemRole> getSystemRoles();

    boolean isSystemRole(Integer roleId);

    Message getMessageByInvitationCode(String invitationCode);
}
