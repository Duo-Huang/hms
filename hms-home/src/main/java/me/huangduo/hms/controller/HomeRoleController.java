package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.dto.model.Role;
import me.huangduo.hms.dto.request.RoleCreateRequest;
import me.huangduo.hms.dto.request.RoleUpdateRequest;
import me.huangduo.hms.dto.response.HmsResponse;
import me.huangduo.hms.dto.response.RoleResponse;
import me.huangduo.hms.exceptions.RoleAlreadyExistedException;
import me.huangduo.hms.mapper.RoleMapper;
import me.huangduo.hms.service.HomeRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/homes/{homeId}/roles")
public class HomeRoleController {

    private final HomeRoleService homeRoleService;

    private final RoleMapper roleMapper;

    public HomeRoleController(HomeRoleService homeRoleService, RoleMapper roleMapper) {
        this.homeRoleService = homeRoleService;
        this.roleMapper = roleMapper;
    }

    @PostMapping
    public ResponseEntity<HmsResponse<Void>> createHomeRole(@PathVariable Integer homeId, @Valid @RequestBody RoleCreateRequest roleCreateOrUpdateRequest) {
        Role role = roleMapper.toModel(roleCreateOrUpdateRequest);
        role.setHomeId(homeId);
        try {
            homeRoleService.createHomeRole(roleCreateOrUpdateRequest.systemRoleId(), role);
            return ResponseEntity.ok(HmsResponse.success());
        } catch (RoleAlreadyExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum().getCode(), e.getHmsErrorCodeEnum().getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<HmsResponse<List<RoleResponse>>> getAvailableRolesFromHome(@PathVariable Integer homeId) {
        List<Role> roles = homeRoleService.getAvailableRolesFromHome(homeId);
        return ResponseEntity.ok(HmsResponse.success(roles.stream().map(roleMapper::toResponse).toList()));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<HmsResponse<Void>> updateHomeRoleInfo(@PathVariable Integer homeId, @PathVariable Integer roleId, @Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        Role role = roleMapper.toModel(roleUpdateRequest);
        role.setHomeId(homeId);
        role.setRoleId(roleId);
        homeRoleService.updateHomeRoleInfo(role);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<HmsResponse<Void>> deleteHomeRole(@PathVariable Integer homeId, @PathVariable Integer roleId) {
        homeRoleService.deleteHomeRole(homeId, roleId);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
