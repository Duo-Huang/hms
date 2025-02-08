package me.huangduo.hms.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.enums.MessageStatusEnum;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class MessageStatusEntity extends BaseEntity {

    private int messageStatusId;

    private int userId;

    private int messageId;

    private MessageStatusEnum status;
}
