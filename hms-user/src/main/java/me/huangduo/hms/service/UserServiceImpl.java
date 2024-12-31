package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dao.UsersMapper;
import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;
    private final AuthService authService;

    public UserServiceImpl(UsersMapper usersMapper, AuthService authService) {
        this.usersMapper = usersMapper;
        this.authService = authService;
    }

    @Override
    public Integer register(User user, String password) throws UserAlreadyExistsException {
        // TODO: use auto mapper ?
        UserEntity userEntity = UserEntity.builder().username(user.getUsername()).password(password).build();
        try {
            usersMapper.create(userEntity);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException();
        }
        return userEntity.getUserId();
    }

    @Override
    public String login(User user, String password) throws IllegalArgumentException {
        UserEntity userEntity = usersMapper.findUserByUsernameAndPassword(user.getUsername(), password);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException();
        }
        user.setUserId(userEntity.getUserId());
        return authService.generateToken(user);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {
    }
}
