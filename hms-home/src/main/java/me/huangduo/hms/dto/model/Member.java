package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Integer userId;

    private Integer homeId;

    private String memberName; // default is user nickname

    private Integer roleId;

    private String roleName;

    private String roleDescription;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
