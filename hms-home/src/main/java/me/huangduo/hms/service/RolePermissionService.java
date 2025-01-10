package me.huangduo.hms.service;

import me.huangduo.hms.exceptions.RecordNotFoundException;

public interface RolePermissionService {

    void assignPermissionsForRole(Integer roleId, Integer[] permissionIds) throws RecordNotFoundException;

    void removePermissionsForRole(Integer roleId, Integer[] permissionIds) throws RecordNotFoundException;
}
