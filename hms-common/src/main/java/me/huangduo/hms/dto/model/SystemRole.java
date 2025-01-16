package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.enums.HmsRoleType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SystemRole {
    private Integer roleId;

    private HmsRoleType roleType;

    private String roleName;

    private String roleDescription;

    private List<Permission> permissions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
