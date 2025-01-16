package me.huangduo.hms.dto.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class HomeRole extends SystemRole {

    private Integer homeId;

}
