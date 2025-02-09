package me.huangduo.hms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Integer permissionId;

    private String permissionCode;

    private String permissionDescription;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
