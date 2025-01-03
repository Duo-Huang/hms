package me.huangduo.hms.dao;


import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RevokedUserTokensMapper {

    @Insert("INSERT INTO revoked_tokens (jti, expiration, username) VALUES (#{jti}, #{expiration}, #{username})")
    int create(RevokedUserTokenEntity revokedUserTokenEntity);

    @Select("SELECT * FROM revoked_tokens WHERE jti = #{jti}")
    RevokedUserTokenEntity getByJti(String jti);
}
