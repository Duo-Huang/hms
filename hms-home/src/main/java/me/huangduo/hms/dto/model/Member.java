package me.huangduo.hms.dto.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

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
