package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.HomeRole;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.exceptions.RoleAlreadyExistedException;

import java.util.List;

public interface HomeRoleService {

    void createHomeRole(Integer basRoleId, HomeRole role) throws RoleAlreadyExistedException, IllegalArgumentException; // create a role base on system role

    List<HomeRole> getAvailableRolesFromHome(Integer homeId) throws RecordNotFoundException;

    void updateHomeRoleInfo(HomeRole role) throws RecordNotFoundException;

    void deleteHomeRole(Integer homeId, Integer roleId) throws RecordNotFoundException, IllegalArgumentException;

    HomeRole getHomeRoleWithPermissions(Integer homeId, Integer roleId) throws RecordNotFoundException, IllegalArgumentException; // can only get home role

    void updatePermissionsForHomeRole(Integer homeId, Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException, IllegalArgumentException;
}
