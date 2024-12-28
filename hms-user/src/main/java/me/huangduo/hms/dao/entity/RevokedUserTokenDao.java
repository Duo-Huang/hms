package me.huangduo.hms.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevokedUserTokenDao {

    private Integer id;

    private String jti;

    private LocalDateTime expiration;

    private String username;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
