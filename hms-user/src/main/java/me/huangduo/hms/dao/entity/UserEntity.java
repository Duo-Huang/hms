package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

    private Integer userId;

    private String username;

    private String password;

    private String nickname;
}
