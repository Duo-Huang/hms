package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.huangduo.hms.enums.RoleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Integer roleId;

    private RoleType roleType;

    private String roleName;

    private String roleDescription;
}
