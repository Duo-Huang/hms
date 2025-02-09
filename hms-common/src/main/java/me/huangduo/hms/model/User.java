package me.huangduo.hms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/*
 * User model
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User/* implements Serializable*/ {
    private Integer userId;

    private String username;

    private String nickname; // can modify freely by user themselves, will display in user profile. default value is username

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
