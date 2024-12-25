package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dao.UsersMapper;
import me.huangduo.hms.dao.entity.UserDao;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;

    public UserServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public Integer register(User user, String password) throws IllegalArgumentException, UserAlreadyExistsException {
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
    public String login(User user, String password) {
        return null;
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {
    }
}
