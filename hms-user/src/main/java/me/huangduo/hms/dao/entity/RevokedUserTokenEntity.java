package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RevokedUserTokenEntity extends BaseEntity {

    private Integer id;

    private String jti;

    private LocalDateTime expiration;

    private Integer userId;
}
