package me.huangduo.hms.service;

import me.huangduo.hms.model.Home;
import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.SystemRole;
import me.huangduo.hms.model.User;
import me.huangduo.hms.enums.SystemRoleEnum;

import java.util.List;

public interface CommonService {

    User getUserById(Integer userId);

    User getUserByName(String username);

    Home getHomeById(Integer homeId);

    boolean isUserInHome(Integer homeId, Integer userId);

    SystemRole getSystemRoleByName(SystemRoleEnum systemRoleEnum);

    List<SystemRole> getSystemRoles();

    boolean isSystemRole(Integer roleId);

    Message getMessageByInvitationCode(String invitationCode);
}
