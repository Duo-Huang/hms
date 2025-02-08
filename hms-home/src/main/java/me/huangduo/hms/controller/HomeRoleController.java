package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.annotations.ValidId;
import me.huangduo.hms.dto.model.HomeRole;
import me.huangduo.hms.dto.request.RolePermissionUpdateRequest;
import me.huangduo.hms.dto.request.RoleCreateRequest;
import me.huangduo.hms.dto.request.RoleUpdateRequest;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.response.RoleResponse;
import me.huangduo.hms.dto.response.RoleWithPermissionResponse;
import me.huangduo.hms.exceptions.RoleAlreadyExistedException;
import me.huangduo.hms.mapper.RoleMapper;
import me.huangduo.hms.service.HomeRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Validated
public class HomeRoleController {

    private final HomeRoleService homeRoleService;

    private final RoleMapper roleMapper;

    public HomeRoleController(HomeRoleService homeRoleService, RoleMapper roleMapper) {
        this.homeRoleService = homeRoleService;
        this.roleMapper = roleMapper;
    }

    @PostMapping
    public ResponseEntity<HmsResponseBody<Void>> createHomeRole(@RequestAttribute Integer homeId, @Valid @RequestBody RoleCreateRequest roleCreateRequest) {
        HomeRole role = roleMapper.toModel(roleCreateRequest);
        role.setHomeId(homeId);
        try {
            homeRoleService.createHomeRole(roleCreateRequest.baseRoleId(), role);
            return ResponseEntity.ok(HmsResponseBody.success());
        } catch (RoleAlreadyExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponseBody.error(e.getErrorCodeEnum()));
        }
    }

    @GetMapping
    public ResponseEntity<HmsResponseBody<List<RoleResponse>>> getAvailableRolesFromHome(@RequestAttribute Integer homeId) {
        List<HomeRole> roles = homeRoleService.getAvailableRolesFromHome(homeId);
        return ResponseEntity.ok(HmsResponseBody.success(roles.stream().map(roleMapper::toResponse).toList()));
    }

    @PatchMapping("/{roleId:\\d+}")
    public ResponseEntity<HmsResponseBody<Void>> updateHomeRoleInfo(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer roleId, @Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        HomeRole role = roleMapper.toModel(roleUpdateRequest);
        role.setHomeId(homeId);
        role.setRoleId(roleId);
        homeRoleService.updateHomeRoleInfo(role);
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @DeleteMapping("/{roleId:\\d+}")
    public ResponseEntity<HmsResponseBody<Void>> deleteHomeRole(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer roleId) {
        homeRoleService.deleteHomeRole(homeId, roleId);
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @GetMapping("/{roleId:\\d+}")
    public ResponseEntity<HmsResponseBody<RoleWithPermissionResponse>> getHomeRoleWithPermissions(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer roleId) {
        HomeRole role = homeRoleService.getHomeRoleWithPermissions(homeId, roleId);
        return ResponseEntity.ok(HmsResponseBody.success(roleMapper.toResponseWithPermission(role)));
    }

    @PutMapping("/{roleId:\\d+}/permissions")
    public ResponseEntity<HmsResponseBody<Void>> updatePermissionsForHomeRole(@ValidId @PathVariable Integer roleId, @RequestAttribute Integer homeId, @Valid @RequestBody RolePermissionUpdateRequest permissionsBody) {
        homeRoleService.updatePermissionsForHomeRole(homeId, roleId, permissionsBody.permissionIds());
        return ResponseEntity.ok(HmsResponseBody.success());
    }
}
