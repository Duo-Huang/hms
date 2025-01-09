package me.huangduo.hms.service;

import me.huangduo.hms.dao.RevokedUserTokensMapper;
import me.huangduo.hms.dao.UsersMapper;
import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;
    private final AuthService authService;

    private final RevokedUserTokensMapper revokedUserTokensMapper;

    public UserServiceImpl(UsersMapper usersMapper, AuthService authService, RevokedUserTokensMapper revokedUserTokensMapper) {
        this.usersMapper = usersMapper;
        this.authService = authService;
        this.revokedUserTokensMapper = revokedUserTokensMapper;
    }

    @Override
    public Integer register(User user, String password) throws UserAlreadyExistsException {
        // TODO: use auto mapper ?
        UserEntity userEntity = UserEntity.builder().username(user.getUsername()).password(password).nickname(user.getUsername()).build();
        try {
            usersMapper.create(userEntity);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException(HmsErrorCodeEnum.USER_ERROR_103);
        }
        return userEntity.getUserId();
    }

    @Override
    public String login(User user, String password) throws IllegalArgumentException {
        UserEntity userEntity = usersMapper.findUserByUsernameAndPassword(user.getUsername(), password);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException();
        }
        // TODO: use auto mapper ?
        user.setUserId(userEntity.getUserId());
        user.setUsername(userEntity.getUsername());
        user.setNickname(userEntity.getNickname());
        user.setCreatedAt(userEntity.getCreatedAt());
        user.setUpdatedAt(userEntity.getUpdatedAt());
        return authService.generateToken(user);
    }

    @Override
    public void logout(UserToken userToken) throws AuthenticationException {
        RevokedUserTokenEntity revokedUserTokenEntity = RevokedUserTokenEntity.builder().jti(userToken.jti()).expiration(userToken.expiration()).username(userToken.userInfo().getUsername()).build();
        revokedUserTokensMapper.create(revokedUserTokenEntity);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {
    }
}
