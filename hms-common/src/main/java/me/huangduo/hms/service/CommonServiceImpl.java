package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.enums.SystemRoleEnum;
import me.huangduo.hms.model.Home;
import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.SystemRole;
import me.huangduo.hms.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    private final CommonDao commonDao;

    public CommonServiceImpl(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @Override
    public User getUserById(Integer userId) {
        return commonDao.getUserById(userId);
    }

    @Override
    public User getUserByName(String username) {
        return commonDao.getUserByName(username);
    }

    @Override
    public Home getHomeById(Integer homeId) {
        return commonDao.getHomeById(homeId);
    }

    @Override
    public boolean isUserInHome(Integer homeId, Integer userId) {
        return commonDao.isUserInHome(homeId, userId) > 0;
    }

    @Override
    public SystemRole getSystemRoleByName(SystemRoleEnum systemRoleEnum) {
        return commonDao.getSystemRoleByName(systemRoleEnum.getRoleName());
    }

    @Override
    public List<SystemRole> getSystemRoles() {
        return commonDao.getSystemRoles();
    }

    @Override
    public boolean isSystemRole(Integer roleId) {
        List<SystemRole> roles = getSystemRoles();
        return roles.stream().anyMatch(role -> role.getRoleId().equals(roleId));
    }

    @Override
    public Message getMessageByInvitationCode(String invitationCode) {
        return commonDao.getMessageByInvitationCode(invitationCode);
    }

    @Override
    public List<Integer> getHomeMemberUserIds(Integer homeId) {

        return commonDao.getUserIdsByHomeId(homeId);
    }
}
