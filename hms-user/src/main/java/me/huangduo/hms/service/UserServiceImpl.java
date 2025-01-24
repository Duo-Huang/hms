package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.RevokedUserTokensDao;
import me.huangduo.hms.dao.UsersDao;
import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.DuplicatedPasswordException;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import me.huangduo.hms.mapper.UserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UsersDao usersDao;
    private final AuthenticationService authenticationService;

    private final UserMapper userMapper;

    private final RevokedUserTokensDao revokedUserTokensDao;

    public UserServiceImpl(UsersDao usersDao, AuthenticationService authenticationService, UserMapper userMapper, RevokedUserTokensDao revokedUserTokensDao) {
        this.usersDao = usersDao;
        this.authenticationService = authenticationService;
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
            BusinessException ex = new UserAlreadyExistsException();
            log.error("This is user is already existed", e);
            throw ex;
        }
        return userEntity.getUserId();
    }

    @Override
    public String login(User user, String password) throws IllegalArgumentException {
        UserEntity userEntity = usersDao.findUserByUsernameAndPassword(user.getUsername(), password);
        if (Objects.isNull(userEntity)) {
            log.error("Wrong username or password");
            throw new IllegalArgumentException();
        }
        user = userMapper.toModel(userEntity);
        return authenticationService.generateToken(user);
    }

    @Override
    public void logout(UserToken userToken) throws AuthenticationException {
        RevokedUserTokenEntity revokedUserTokenEntity = RevokedUserTokenEntity.builder().jti(userToken.jti()).expiration(userToken.expiration()).userId(userToken.userId()).build();
        revokedUserTokensDao.create(revokedUserTokenEntity);
    }

    @Override
    public void changePassword(User user, UserToken userToken, String oldPassword, String newPassword) throws IllegalArgumentException {
        if (Objects.equals(oldPassword, newPassword)) {
            BusinessException e = new DuplicatedPasswordException();
            log.error("The new password cannot be the same as the old password", e);
            throw e;
        }
        UserEntity userEntity = usersDao.findUserByUsernameAndPassword(user.getUsername(), oldPassword);
        if (Objects.isNull(userEntity)) {
            RuntimeException e = new IllegalArgumentException();
            log.error("Wrong password", e);
            throw e;
        }
        userEntity.setPassword(newPassword);
        usersDao.update(userEntity);
        authenticationService.revokeToken(userToken);
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
