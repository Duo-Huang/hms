package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.enums.HmsRoleType;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class RoleEntity extends BaseEntity {

    private Integer roleId;

    private HmsRoleType roleType;

    private String roleName;

    private String roleDescription;
}
