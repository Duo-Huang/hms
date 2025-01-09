package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class HomeMemberRoleEntity extends BaseEntity {

    private Integer memberId;

    private Integer userId;

    private Integer homeId;

    private Integer roleId;

    private String memberName; // member name in the home, default is nickname
}
