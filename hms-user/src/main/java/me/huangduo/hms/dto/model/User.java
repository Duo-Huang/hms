package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
* User 业务模型
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;

    private String username;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
