package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.HomeMemberRolesDao;
import me.huangduo.hms.dao.RolePermissionsDao;
import me.huangduo.hms.dao.RolesDao;
import me.huangduo.hms.dao.entity.PermissionEntity;
import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dao.entity.RolePermissionEntity;
import me.huangduo.hms.dto.model.HomeRole;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.enums.HmsRoleType;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.exceptions.RoleAlreadyExistedException;
import me.huangduo.hms.mapper.PermissionMapper;
import me.huangduo.hms.mapper.RoleMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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

    private final PermissionMapper permissionMapper;


    public HomeRoleServiceImpl(RolesDao rolesDao, RoleMapper roleMapper, RolePermissionsDao rolePermissionsDao, PermissionMapper permissionMapper) {
        this.rolesDao = rolesDao;
        this.roleMapper = roleMapper;
        this.rolePermissionsDao = rolePermissionsDao;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public void createHomeRole(Integer baseRoleId, HomeRole role) throws RoleAlreadyExistedException {
        List<RolePermissionEntity> baseRolePermissions = rolePermissionsDao.getItemByRoleId(baseRoleId);
        if (baseRolePermissions.isEmpty()) {
            log.warn("Will create a home role without any permissions");
        }
        RoleEntity roleEntity = roleMapper.toEntity(role);
        roleEntity.setRoleType(HmsRoleType.CUSTOM_ROLE);

        try {
            rolesDao.add(roleEntity);
        } catch (DuplicateKeyException e) {
            BusinessException ex = new RoleAlreadyExistedException(HmsErrorCodeEnum.HOME_ERROR_2013);
            log.error("This role is already existed.", ex);
            throw ex;
        }

        // assign permissions for new role
        baseRolePermissions.forEach(x -> {
            x.setRolePermissionId(null);
            x.setRoleId(roleEntity.getRoleId());
            x.setCreatedAt(null);
            x.setUpdatedAt(null);
        });

        rolePermissionsDao.batchCreate(baseRolePermissions);
    }

    @Override
    public List<HomeRole> getAvailableRolesFromHome(Integer homeId) throws RecordNotFoundException {
        List<RoleEntity> roleEntities = rolesDao.getItemsByHomeId(homeId);

        return roleEntities.stream().map(roleMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void updateHomeRoleInfo(HomeRole role) throws RecordNotFoundException {
        checkRoleInHome(role.getHomeId(), role.getRoleId());
        rolesDao.updateByIdAndHomeId(roleMapper.toEntity(role));
    }

    @Override
    public void deleteHomeRole(Integer homeId, Integer roleId) throws RecordNotFoundException {
        checkRoleInHome(homeId, roleId);
        rolesDao.deleteByIdAndHomeId(homeId, roleId);
    }

    @Override
    public HomeRole getHomeRoleWithPermissions(Integer homeId, Integer roleId) throws RecordNotFoundException {
        checkRoleInHome(homeId, roleId);
        HomeRole homeRole = roleMapper.toModel(rolesDao.getById(roleId));
        List<PermissionEntity> permissionEntities = rolePermissionsDao.getPermissionsByRoleId(roleId);

        homeRole.setPermissions(permissionEntities.stream().map(permissionMapper::toModel).collect(Collectors.toList()));
        return homeRole;
    }

    @Override
    public void assignPermissionsForHomeRole(Integer homeId, Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException {
        checkRoleInHome(homeId, roleId);
        List<RolePermissionEntity> rolePermissionEntities = permissionIds.stream().map(x -> RolePermissionEntity.builder().roleId(roleId).permissionId(x).build()).collect(Collectors.toList());
        try {
            rolePermissionsDao.batchCreate(rolePermissionEntities);
        } catch (DataIntegrityViolationException e) {
            log.error("Batch create permissions failed:", e);
            throw new IllegalArgumentException("some permissions doesn't existed.");
        }
    }

    @Override
    public void removePermissionsForHomeRole(Integer homeId, Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException {
        checkRoleInHome(homeId, roleId);
        List<RolePermissionEntity> rolePermissionEntities = permissionIds.stream().map(x -> RolePermissionEntity.builder().roleId(roleId).permissionId(x).build()).collect(Collectors.toList());
        rolePermissionsDao.batchDelete(rolePermissionEntities);
    }

    private void checkRoleInHome(Integer homeId, Integer roleId) {
        if (roleId == null || Objects.isNull(rolesDao.getItemByIdAndHomeId(homeId, roleId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_2014);
            log.error("This role does not exists in this home.", e);
            throw e;
        }
    }
}
