package me.huangduo.hms.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {
    private Integer userId;

    private String username;
    private String password;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
