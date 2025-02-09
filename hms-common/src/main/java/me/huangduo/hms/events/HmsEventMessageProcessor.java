package me.huangduo.hms.events;

import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.User;

public interface HmsEventMessageProcessor {
    boolean messageConvert(User user, Integer homeId, Message message);
}
