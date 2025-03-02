package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.HomesDao;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.model.Home;
import me.huangduo.hms.model.Member;
import me.huangduo.hms.model.SystemRole;
import me.huangduo.hms.model.User;
import me.huangduo.hms.enums.SystemRoleEnum;
import me.huangduo.hms.enums.ErrorCodeEnum;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.mapper.HomeMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    private final HomesDao homesDao;

    private final HomeMemberService homeMemberService;

    private final HomeMapper homeMapper;

    private final CommonService commonService;

    public HomeServiceImpl(HomesDao homesDao, HomeMemberService homeMemberService, HomeMapper homeMapper, CommonService commonService) {
        this.homesDao = homesDao;
        this.homeMemberService = homeMemberService;
        this.homeMapper = homeMapper;
        this.commonService = commonService;
    }

    @Override
    @Transactional
    public void createHome(Home home, User user) throws HomeAlreadyExistsException, IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null.");
        }

        if (home == null) {
            throw new IllegalArgumentException("home can not be null.");
        }

        HomeEntity homeEntity = homeMapper.toEntity(home);
        try {
            homesDao.create(homeEntity);
        } catch (DuplicateKeyException e) {
            BusinessException ex = new HomeAlreadyExistsException();
            log.error("This home is already existed.", ex);
            throw ex;
        }

        // add this user to this home and assign current user to default system admin role
        Member member = new Member();
        member.setHomeId(homeEntity.getHomeId());
        member.setUserId(user.getUserId());

        homeMemberService.addMember(member.getHomeId(), user);

        SystemRole adminRole = commonService.getSystemRoleByName(SystemRoleEnum.HOME_ADMIN);

        if (Objects.isNull(adminRole)) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_208);
            log.error("The member is not assigned a default admin role for this home.", e);
            throw e;
        }

        homeMemberService.assignRoleForMember(user, member, adminRole.getRoleId(), true);
    }

    @Override
    public Home getHomeInfo(Integer homeId) throws RecordNotFoundException {
        HomeEntity homeInfo = homesDao.getById(homeId);
        if (Objects.isNull(homeInfo)) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist.", e);
            throw e;
        }
        return homeMapper.toModel(homeInfo);
    }

    @Override
    public void updateHomeInfo(Home home) throws RecordNotFoundException {
        int row = homesDao.update(homeMapper.toEntity(home));
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public void deleteHome(Integer homeId) throws RecordNotFoundException {
        int row = homesDao.delete(homeId);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist.", e);
            throw e;
        }
    }
}
