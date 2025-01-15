package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.RolePermissionsDao;
import me.huangduo.hms.dao.RolesDao;
import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dao.entity.RolePermissionEntity;
import me.huangduo.hms.dto.model.Role;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.enums.HmsRoleType;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.exceptions.RoleAlreadyExistedException;
import me.huangduo.hms.mapper.RoleMapper;
import me.huangduo.hms.utils.Utils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeRoleServiceImpl implements HomeRoleService {
    private final RolesDao rolesDao;

    private final RoleMapper roleMapper;

    private final RolePermissionsDao rolePermissionsDao;


    public HomeRoleServiceImpl(RolesDao rolesDao, RoleMapper roleMapper, RolePermissionsDao rolePermissionsDao) {
        this.rolesDao = rolesDao;
        this.roleMapper = roleMapper;
        this.rolePermissionsDao = rolePermissionsDao;
    }

    @Override
    public void createHomeRole(Integer baseRoleId, Role role) throws RoleAlreadyExistedException {
        List<RolePermissionEntity> baseRolePermissions = rolePermissionsDao.getItemByRoleId(baseRoleId);
        if (baseRolePermissions.isEmpty()) {
            log.warn("Will create a home role without any permissions");
        }
        RoleEntity roleEntity = roleMapper.toEntity(role);
        roleEntity.setRoleType(HmsRoleType.CUSTOM_ROLE);

        try {
            rolesDao.add(roleEntity);
        } catch (RoleAlreadyExistedException e) {
            BusinessException ex = new RoleAlreadyExistedException(HmsErrorCodeEnum.HOME_ERROR_2013);
            log.error("This role is already existed.", ex);
            throw ex;
        }

        // create permissions for new role
        baseRolePermissions.forEach(x -> {
            x.setRolePermissionId(null);
            x.setRoleId(role.getRoleId());
            x.setCreatedAt(null);
            x.setUpdatedAt(null);
        });

        rolePermissionsDao.batchCreate(baseRolePermissions);
    }

    @Override
    public List<Role> getAvailableRolesFromHome(Integer homeId) throws RecordNotFoundException {
        List<RoleEntity> roleEntities = rolesDao.getItemsByHomeId(homeId);

        return roleEntities.stream().map(roleMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void updateHomeRoleInfo(Role role) throws RecordNotFoundException {
        int row = rolesDao.updateByIdAndHomeId(role.getHomeId(), role.getRoleId());
        if (row == 0) {
            BusinessException ex = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_2014);
            log.error("This role does not exist.", ex);
            throw ex;
        }
    }

    @Override
    public void deleteHomeRole(Integer homeId, Integer roleId) throws RecordNotFoundException {
        int row = rolesDao.deleteByIdAndHomeId(homeId, roleId);
        if (row == 0) {
            BusinessException ex = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_2014);
            log.error("This role does not exist.", ex);
            throw ex;
        }
    }

    @Override
    public Role getHomeRoleWithPermissions(Integer homeId, Integer roleId) throws RecordNotFoundException {
        RoleEntity roleEntity = rolesDao.getItemByIdAndHomeId(homeId, roleId);
        rolePermissionsDao.getItemByRoleId(roleId);
        return null;
    }

    @Override
    public void assignPermissionsForHomeRole(Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException {
        checkRoleExisted(roleId);
        List<RolePermissionEntity> rolePermissionEntities = permissionIds.stream().map(x -> RolePermissionEntity.builder().roleId(roleId).permissionId(x).build()).collect(Collectors.toList());
        try {
            rolePermissionsDao.batchCreate(rolePermissionEntities);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("some permissions doesn't existed.");
        }
    }

    @Override
    public void removePermissionsForHomeRole(Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException {

    }

    private void checkRoleExisted(Integer roleId) {
        if (roleId == null || Objects.isNull(rolesDao.getById(roleId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_2014);
            log.error("This role does not existed.", e);
            throw e;
        }
    }
}
