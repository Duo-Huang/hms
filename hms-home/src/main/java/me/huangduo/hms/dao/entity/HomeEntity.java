package me.huangduo.hms.dao.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class HomeEntity extends BaseEntity {

    private Integer homeId;

    private String homeName;

    private String homeDescription;
}
