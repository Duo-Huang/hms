package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
