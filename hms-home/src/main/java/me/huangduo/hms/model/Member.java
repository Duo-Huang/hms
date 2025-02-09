package me.huangduo.hms.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.model.SystemRole;
import me.huangduo.hms.model.User;

import java.time.LocalDateTime;

/*
 * Home member 业务模型
 * */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Member extends User {

    private Integer homeId;

    private String memberName; // default is user nickname

    private SystemRole role; // can be system role or home role

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
