package me.huangduo.hms.service;


import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;

/**
 * 处理与用户相关的所有操作，包括注册、登录、修改用户信息等
 */
public interface UserService {

    /**
     * 注册用户, 接收user业务对象而不是username, 方便以后使用邮箱或手机号登录
     *
     * @param user     要注册的用户信息
     * @param password 要注册的用户密码
     * @return 注册成功的用户id
     * @throws UserAlreadyExistsException 用户已存在
     */
    Integer register(User user, String password) throws UserAlreadyExistsException;

    /**
     * 用户登录, 接收user业务对象而不是username, 方便以后使用邮箱或手机号登录
     *
     * @param user 登录的用户信息
     * @return jwt token
     * @throws IllegalArgumentException 用户名或密码错误
     */
    String login(User user, String password) throws IllegalArgumentException;

    void logout(UserToken token) throws AuthenticationException;

    /**
     * 修改用户密码
     *
     * @param user        当前用户
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     * @throws IllegalArgumentException 如果输入的参数无效
     */
    void changePassword(User user, UserToken userToken, String oldPassword, String newPassword) throws IllegalArgumentException;

    User getProfile(Integer userId);

    void updateProfile(User user);
}
