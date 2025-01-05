package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class HomeEntity extends BaseEntity {

    private Integer homeId;

    private String homeName;

    private String homeDescription;
}
