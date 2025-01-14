package me.huangduo.hms.service;

import me.huangduo.hms.dao.RevokedUserTokensDao;
import me.huangduo.hms.dao.UsersDao;
import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.exceptions.DuplicatedPasswordException;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import me.huangduo.hms.mapper.UserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private final UsersDao usersDao;
    private final AuthService authService;

    private final UserMapper userMapper;

    private final RevokedUserTokensDao revokedUserTokensDao;

    public UserServiceImpl(UsersDao usersDao, AuthService authService, UserMapper userMapper, RevokedUserTokensDao revokedUserTokensDao) {
        this.usersDao = usersDao;
        this.authService = authService;
        this.userMapper = userMapper;
        this.revokedUserTokensDao = revokedUserTokensDao;
    }

    @Override
    public Integer register(User user, String password) throws UserAlreadyExistsException {
        UserEntity userEntity = userMapper.toEntity(user);
        userEntity.setNickname(user.getUsername());
        userEntity.setPassword(password);
        try {
            usersDao.create(userEntity);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException(HmsErrorCodeEnum.USER_ERROR_103);
        }
        return userEntity.getUserId();
    }

    @Override
    public String login(User user, String password) throws IllegalArgumentException {
        UserEntity userEntity = usersDao.findUserByUsernameAndPassword(user.getUsername(), password);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException();
        }
        user = userMapper.toModel(userEntity);
        return authService.generateToken(user);
    }

    @Override
    public void logout(UserToken userToken) throws AuthenticationException {
        RevokedUserTokenEntity revokedUserTokenEntity = RevokedUserTokenEntity.builder().jti(userToken.jti()).expiration(userToken.expiration()).username(userToken.userInfo().getUsername()).build();
        revokedUserTokensDao.create(revokedUserTokenEntity);
    }

    @Override
    public void changePassword(UserToken userToken, String oldPassword, String newPassword) throws IllegalArgumentException {
        if (Objects.equals(oldPassword, newPassword)) {
            throw new DuplicatedPasswordException(HmsErrorCodeEnum.USER_ERROR_1010);
        }
        UserEntity userEntity = usersDao.findUserByUsernameAndPassword(userToken.userInfo().getUsername(), oldPassword);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException();
        }
        userEntity.setPassword(newPassword);
        usersDao.update(userEntity);
        authService.revokeToken(userToken);
    }

    @Override
    public User getProfile(Integer userId) {
        UserEntity userInfo = usersDao.getUserInfoById(userId);
        return userMapper.toModel(userInfo);
    }

    @Override
    public void updateProfile(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        usersDao.update(userEntity);
    }
}
