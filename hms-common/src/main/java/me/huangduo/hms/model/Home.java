package me.huangduo.hms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Home {
    private Integer homeId;

    private String homeName;

    private String homeDescription;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
