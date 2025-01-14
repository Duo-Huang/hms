package me.huangduo.hms.dao.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class HomeMemberRoleEntity extends BaseEntity {

    private Integer memberId;

    private Integer userId;

    private Integer homeId;

    private Integer roleId;

    private String memberName; // member name in the home, default is nickname
}
