package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Home member 业务模型
 * */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member extends User {

    private Integer homeId;

    private String memberName; // default is user nickname

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
