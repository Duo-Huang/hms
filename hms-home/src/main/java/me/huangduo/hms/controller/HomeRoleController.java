package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.annotations.ValidId;
import me.huangduo.hms.dto.model.HomeRole;
import me.huangduo.hms.dto.request.RolePermissionUpdateRequest;
import me.huangduo.hms.dto.request.RoleCreateRequest;
import me.huangduo.hms.dto.request.RoleUpdateRequest;
import me.huangduo.hms.dto.response.HmsResponse;
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
    public ResponseEntity<HmsResponse<Void>> createHomeRole(@RequestAttribute Integer homeId, @Valid @RequestBody RoleCreateRequest roleCreateRequest) {
        HomeRole role = roleMapper.toModel(roleCreateRequest);
        role.setHomeId(homeId);
        try {
            homeRoleService.createHomeRole(roleCreateRequest.baseRoleId(), role);
            return ResponseEntity.ok(HmsResponse.success());
        } catch (RoleAlreadyExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum()));
        }
    }

    @GetMapping
    public ResponseEntity<HmsResponse<List<RoleResponse>>> getAvailableRolesFromHome(@RequestAttribute Integer homeId) {
        List<HomeRole> roles = homeRoleService.getAvailableRolesFromHome(homeId);
        return ResponseEntity.ok(HmsResponse.success(roles.stream().map(roleMapper::toResponse).toList()));
    }

    @PatchMapping("/{roleId:\\d+}")
    public ResponseEntity<HmsResponse<Void>> updateHomeRoleInfo(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer roleId, @Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        HomeRole role = roleMapper.toModel(roleUpdateRequest);
        role.setHomeId(homeId);
        role.setRoleId(roleId);
        homeRoleService.updateHomeRoleInfo(role);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{roleId:\\d+}")
    public ResponseEntity<HmsResponse<Void>> deleteHomeRole(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer roleId) {
        homeRoleService.deleteHomeRole(homeId, roleId);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @GetMapping("/{roleId:\\d+}")
    public ResponseEntity<HmsResponse<RoleWithPermissionResponse>> getHomeRoleWithPermissions(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer roleId) {
        HomeRole role = homeRoleService.getHomeRoleWithPermissions(homeId, roleId);
        return ResponseEntity.ok(HmsResponse.success(roleMapper.toResponseWithPermission(role)));
    }

    @PutMapping("/{roleId:\\d+}/permissions")
    public ResponseEntity<HmsResponse<Void>> updatePermissionsForHomeRole(@ValidId @PathVariable Integer roleId, @RequestAttribute Integer homeId, @Valid @RequestBody RolePermissionUpdateRequest permissionsBody) {
        homeRoleService.updatePermissionsForHomeRole(homeId, roleId, permissionsBody.permissionIds());
        return ResponseEntity.ok(HmsResponse.success());
    }
}
