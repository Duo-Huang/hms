package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.SystemRole;
import me.huangduo.hms.events.InvitationEvent;

import java.util.List;

public interface CommonService {

    User getUserById(Integer userId);

    User getUserByName(String username);

    Home getHomeById(Integer homeId);

    boolean isUserInHome(Integer homeId, Integer userId);

    me.huangduo.hms.dto.model.SystemRole getSystemRoleByName(SystemRole hmsSystemRole);

    List<me.huangduo.hms.dto.model.SystemRole> getSystemRoles();

    boolean isSystemRole(Integer roleId);

    Message<InvitationEvent.InvitationMessagePayload> getMessageByInvitationCode(String invitationCode);
}
