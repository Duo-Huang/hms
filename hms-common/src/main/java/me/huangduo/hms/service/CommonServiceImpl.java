package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.SystemRole;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsSystemRole;
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
    public User getUserInfo(Integer userId) {
        return commonDao.getUserById(userId);
    }

    @Override
    public Home getHomeInfo(Integer homeId) {
        return commonDao.getHomeById(homeId);
    }

    @Override
    public boolean isUserInHome(Integer homeId, Integer userId) {
        return commonDao.isUserInHome(homeId, userId) > 0;
    }

    @Override
    public SystemRole getSystemRoleByName(HmsSystemRole hmsSystemRole) {
        return commonDao.getSystemRoleByName(hmsSystemRole.getRoleName());
    }

    @Override
    public List<SystemRole> getSystemRoles() {
        return commonDao.getSystemRoles();
    }
}
