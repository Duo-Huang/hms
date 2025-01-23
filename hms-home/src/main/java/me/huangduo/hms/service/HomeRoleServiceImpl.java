package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class HomeRoleServiceImpl implements HomeRoleService {
    private final RolesDao rolesDao;

    private final RoleMapper roleMapper;

    private final RolePermissionsDao rolePermissionsDao;

    private final PermissionMapper permissionMapper;

    private final CommonService commonService;

    private final CheckService checkService;


    public HomeRoleServiceImpl(RolesDao rolesDao, RoleMapper roleMapper, RolePermissionsDao rolePermissionsDao, PermissionMapper permissionMapper, CommonService commonService, CheckService checkService) {
        this.rolesDao = rolesDao;
        this.roleMapper = roleMapper;
        this.rolePermissionsDao = rolePermissionsDao;
        this.permissionMapper = permissionMapper;
        this.commonService = commonService;
        this.checkService = checkService;
    }

    @Override
    @Transactional
    public void createHomeRole(Integer baseRoleId, HomeRole role) throws RoleAlreadyExistedException, IllegalArgumentException {
        if (role == null) {
            throw new IllegalArgumentException("role can not be null.");
        }
        List<RolePermissionEntity> baseRolePermissions = rolePermissionsDao.getItemByRoleId(baseRoleId);
        if (baseRolePermissions.isEmpty()) {
            log.warn("Will create a home role without any permissions");
        }
        RoleEntity roleEntity = roleMapper.toEntity(role);
        roleEntity.setRoleType(HmsRoleType.CUSTOM_ROLE);

        try {
            rolesDao.add(roleEntity);
        } catch (DuplicateKeyException e) {
            BusinessException ex = new RoleAlreadyExistedException();
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
        Stream<HomeRole> systemRoleStream = commonService.getSystemRoles().stream()
                .map(x -> HomeRole.builder()
                        .roleId(x.getRoleId())
                        .roleType(x.getRoleType())
                        .roleName(x.getRoleName())
                        .roleDescription(x.getRoleDescription())
                        .createdAt(x.getCreatedAt())
                        .build()
                );

        return Stream.concat(systemRoleStream, roleEntities.stream().map(roleMapper::toModel)).collect(Collectors.toList());
    }

    @Override
    public void updateHomeRoleInfo(HomeRole role) throws RecordNotFoundException, IllegalArgumentException {
        if (role == null) {
            throw new IllegalArgumentException("role can not be null.");
        }
        checkService.checkRoleAccess(role.getHomeId(), role.getRoleId(), true);
        rolesDao.updateByIdAndHomeId(roleMapper.toEntity(role));
    }

    @Override
    public void deleteHomeRole(Integer homeId, Integer roleId) throws RecordNotFoundException, IllegalArgumentException {
        checkService.checkRoleAccess(homeId, roleId, true);
        rolesDao.deleteByIdAndHomeId(homeId, roleId);
    }

    @Override
    public HomeRole getHomeRoleWithPermissions(Integer homeId, Integer roleId) throws RecordNotFoundException, IllegalArgumentException {
        checkService.checkRoleAccess(homeId, roleId, false);
        HomeRole homeRole = roleMapper.toModel(rolesDao.getById(roleId));
        List<PermissionEntity> permissionEntities = rolePermissionsDao.getPermissionsByRoleId(roleId);

        homeRole.setPermissions(permissionEntities.stream().map(permissionMapper::toModel).collect(Collectors.toList()));
        return homeRole;
    }

    @Override
    @Transactional
    public void updatePermissionsForHomeRole(Integer homeId, Integer roleId, List<Integer> permissionIds) throws RecordNotFoundException, IllegalArgumentException {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("permissionIds cannot be empty.");
        }
        checkService.checkRoleAccess(homeId, roleId, true);
        rolePermissionsDao.removeAllPermissionsByRoleId(roleId);
        List<RolePermissionEntity> rolePermissionEntities = permissionIds.stream().map(x -> RolePermissionEntity.builder().roleId(roleId).permissionId(x).build()).collect(Collectors.toList());
        try {
            rolePermissionsDao.batchCreate(rolePermissionEntities);
        } catch (DataIntegrityViolationException e) {
            log.error("Batch create permissions failed:", e);
            throw new IllegalArgumentException("some permissions doesn't existed.");
        }
    }
}
