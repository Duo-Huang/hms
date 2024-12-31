package me.huangduo.hms.dao;


import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RevokedUserTokensMapper {

    @Insert("INSERT INTO revoked_tokens (jti, expiration, username) VALUES (#{jti}, #{expiration}, #{username})")
    int create(RevokedUserTokenEntity revokedUserTokenEntity);
}
