package me.huangduo.hms.dao.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

    private Integer userId;

    private String username;

    private String password;

    private String nickname; // can modify freely by user themselves
}
