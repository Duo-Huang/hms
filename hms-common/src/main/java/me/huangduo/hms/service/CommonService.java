package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.SystemRole;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsSystemRole;

import java.util.List;

public interface CommonService {

    User getUserInfo(Integer userId);

    Home getHomeInfo(Integer homeId);

    boolean isUserInHome(Integer homeId, Integer userId);

    SystemRole getSystemRoleByName(HmsSystemRole hmsSystemRole);

    List<SystemRole> getSystemRoles();
}
