package me.huangduo.hms.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/*
 * User 业务模型
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {
    private Integer userId;

    private String username;

    private String nickname; // can modify freely by user themselves, will display in user profile. default value is username

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
