package me.huangduo.hms.dto.model;

import lombok.*;
import me.huangduo.hms.enums.HmsRoleType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer roleId;

    private HmsRoleType roleType;

    private String roleName;

    private String roleDescription;

    private Integer homeId;

    private List<Permission> permissions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
