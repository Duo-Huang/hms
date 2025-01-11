package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.huangduo.hms.enums.HmsRoleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Integer roleId;

    private HmsRoleType roleType;

    private String roleName;

    private String roleDescription;
}
