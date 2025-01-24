package me.huangduo.hms.dao;

import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.SystemRole;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.events.InvitationEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommonDao {

    @Select("SELECT user_id, username, nickname, created_at, updated_at FROM users WHERE user_id = #{userId}")
    User getUserById(Integer userId);

    @Select("SELECT user_id, username, nickname, created_at, updated_at FROM users WHERE username = #{username}")
    User getUserByName(String username);

    @Select("SELECT * FROM homes WHERE home_id = #{homeId}")
    Home getHomeById(Integer homeId);

    @Select("SELECT COUNT(*) FROM home_member_roles WHERE home_id = #{homeId} AND user_id = #{userId}")
    int isUserInHome(Integer homeId, Integer userId);

    @Select("SELECT * FROM roles WHERE role_name = #{roleName} AND role_type = 0 AND home_id IS NULL")
    SystemRole getSystemRoleByName(String roleName);

    @Select("SELECT * FROM roles WHERE role_type = 0 AND home_id IS NULL")
    List<SystemRole> getSystemRoles();

    @Select("SELECT * FROM messages WHERE message_type = 1 AND JSON_UNQUOTE(JSON_EXTRACT(payload, '$.invitationCode')) = #{invitationCode}")
    Message<InvitationEvent.InvitationMessagePayload> getMessageByInvitationCode(String invitationCode);
}
