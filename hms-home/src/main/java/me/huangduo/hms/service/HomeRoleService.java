package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Role;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.exceptions.RoleAlreadyExistedException;

import java.util.List;

public interface HomeRoleService {

    void createHomeRole(Integer basRoleId, Role role) throws RoleAlreadyExistedException; // create a role base on system role

    List<Role> getAvailableRolesFromHome(Integer homeId) throws RecordNotFoundException;

    void updateHomeRoleInfo(Role role) throws RecordNotFoundException;

    void deleteHomeRole(Integer homeId, Integer roleId) throws RecordNotFoundException;

    Role getHomeRoleWithPermissions(Integer homeId, Integer roleId) throws RecordNotFoundException; // can only get home role

    void assignPermissionsForHomeRole(Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException;

    void removePermissionsForHomeRole(Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException;
}
