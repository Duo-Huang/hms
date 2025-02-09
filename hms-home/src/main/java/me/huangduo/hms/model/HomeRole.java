package me.huangduo.hms.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.model.SystemRole;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class HomeRole extends SystemRole {

    private Integer homeId;

}
