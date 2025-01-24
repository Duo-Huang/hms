package me.huangduo.hms.dao;


import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RevokedUserTokensDao {

    @Insert("INSERT INTO revoked_tokens (jti, expiration, user_id) VALUES (#{jti}, #{expiration}, #{userId})")
    int create(RevokedUserTokenEntity revokedUserTokenEntity);

    @Select("SELECT * FROM revoked_tokens WHERE jti = #{jti}")
    RevokedUserTokenEntity getByJti(String jti);
}
