package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.SystemRole;
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
    public me.huangduo.hms.dto.model.SystemRole getSystemRoleByName(SystemRole hmsSystemRole) {
        return commonDao.getSystemRoleByName(hmsSystemRole.getRoleName());
    }

    @Override
    public List<me.huangduo.hms.dto.model.SystemRole> getSystemRoles() {
        return commonDao.getSystemRoles();
    }

    @Override
    public boolean isSystemRole(Integer roleId) {
        List<me.huangduo.hms.dto.model.SystemRole> roles = getSystemRoles();
        return roles.stream().anyMatch(role -> role.getRoleId().equals(roleId));
    }

    @Override
    public Message getMessageByInvitationCode(String invitationCode) {
        return commonDao.getMessageByInvitationCode(invitationCode);
    }
}
