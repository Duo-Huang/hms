package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dao.UsersMapper;
import me.huangduo.hms.dao.entity.UserDao;
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
        UserDao userDao = new UserDao(null, user.getUsername(), password, null, null, null);
        try {
            usersMapper.create(userDao);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException();
        }
        return userDao.getUserId();
    }

    @Override
    public String login(User user, String password) throws IllegalArgumentException {
        UserDao userDao = usersMapper.findUserByUsernameAndPassword(user.getUsername(), password);
        if (Objects.isNull(userDao)) {
            throw new IllegalArgumentException();
        }
        user.setUserId(userDao.getUserId());
        return authService.generateToken(user);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {
    }
}
