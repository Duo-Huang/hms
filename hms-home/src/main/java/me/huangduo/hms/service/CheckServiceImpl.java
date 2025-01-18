package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.RolesDao;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class CheckServiceImpl implements CheckService {

    private final CommonService commonService;

    private final RolesDao rolesDao;

    public CheckServiceImpl(CommonService commonService, RolesDao rolesDao) {
        this.commonService = commonService;
        this.rolesDao = rolesDao;
    }

    @Override
    public User checkUserExisted(Integer userId) {
        User user = null;
        if (userId == null || Objects.isNull(user = commonService.getUserById(userId))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_204);
            log.error("This user does not existed.", e);
            throw e;
        }
        return user;
    }

    public User checkUserExisted(String username) {
        User user = null;
        if (username == null || Objects.isNull(user = commonService.getUserByName(username))) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_204);
            log.error("This user does not existed.", e);
            throw e;
        }
        return user;
    }

    @Override
    public void checkRoleAccess(Integer homeId, Integer roleId, boolean isModification) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId can not be null.");
        }
        boolean isRoleInHome = Objects.nonNull(rolesDao.getItemByIdAndHomeId(homeId, roleId));
        boolean isSystemRole = commonService.isSystemRole(roleId);

        if (isModification && isSystemRole) {
            throw new IllegalArgumentException("system role is readonly.");
        }

        if (!isRoleInHome && !isSystemRole) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_2011);
            log.error("This role does not exists in this home and it is not a system role.", e);
            throw e;
        }
    }
}
