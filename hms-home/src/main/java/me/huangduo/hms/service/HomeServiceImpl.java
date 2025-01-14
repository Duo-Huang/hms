package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.dao.HomesDao;
import me.huangduo.hms.dao.RolesDao;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.enums.HmsSystemRole;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.mapper.HomeMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    private final HomesDao homesDao;

    private final HomeMemberService homeMemberService;

    private final RolesDao rolesDao;

    private final HomeMapper homeMapper;

    public HomeServiceImpl(HomesDao homesDao, HomeMemberService homeMemberService, RolesDao rolesDao, HomeMapper homeMapper) {
        this.homesDao = homesDao;
        this.homeMemberService = homeMemberService;
        this.rolesDao = rolesDao;
        this.homeMapper = homeMapper;
    }

    @Override
    public void createHome(Home home, User user) throws HomeAlreadyExistsException {
        HomeEntity homeEntity = homeMapper.toEntity(home);
        try {
            homesDao.create(homeEntity);
        } catch (DuplicateKeyException e) {
            BusinessException ex = new HomeAlreadyExistsException(HmsErrorCodeEnum.HOME_ERROR_201);
            log.error("This home is already existed.", ex);
            throw ex;
        }

        // assign current user to default system admin role in this home
        Member member = new Member();
        member.setHomeId(homeEntity.getHomeId());
        member.setUserId(user.getUserId());

        RoleEntity adminRole = rolesDao.getSystemRoleByName(HmsSystemRole.HOME_ADMIN.getRoleName());

        if (Objects.isNull(adminRole)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_209);
            log.error("The member is not assigned a default admin role for this home.", e);
            throw e;
        }

        homeMemberService.assignRoleForMember(member, adminRole.getRoleId());
    }

    @Override
    public Home getHomeInfo(Integer homeId) throws RecordNotFoundException {
        HomeEntity homeInfo = homesDao.getById(homeId);
        if (Objects.isNull(homeInfo)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist.", e);
            throw e;
        }
        return homeMapper.toModel(homeInfo);
    }

    @Override
    public void updateHomeInfo(Home home) throws RecordNotFoundException {
        int row = homesDao.update(homeMapper.toEntity(home));
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public void deleteHome(Integer homeId) throws RecordNotFoundException {
        int row = homesDao.delete(homeId);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist.", e);
            throw e;
        }
    }
}
