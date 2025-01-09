package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.HomesMapper;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    private final HomesMapper homesMapper;

    public HomeServiceImpl(HomesMapper homesMapper) {
        this.homesMapper = homesMapper;
    }

    @Override
    public void createHome(HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) throws HomeAlreadyExistsException {
        HomeEntity homeEntity = HomeEntity.builder().homeName(homeCreateOrUpdateRequest.homeName()).homeDescription(homeCreateOrUpdateRequest.homeDescription()).build();
        try {
            homesMapper.create(homeEntity);
            // TODO: 当前用户加入该家庭, 修改昵称 并赋予admin角色
        } catch (DuplicateKeyException e) {
            BusinessException ex = new HomeAlreadyExistsException(HmsErrorCodeEnum.HOME_ERROR_201);
            log.error("This home is already existed", ex);
            throw ex;
        }
    }

    @Override
    public HomeEntity getHomeInfo(Integer homeId) throws RecordNotFoundException {
        HomeEntity homeInfo = homesMapper.getById(homeId);
        if (Objects.isNull(homeInfo)) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist", e);
            throw e;
        }
        return homeInfo;
    }

    @Override
    public void updateHomeInfo(Integer homeId, HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) throws RecordNotFoundException {
        HomeEntity homeEntity = HomeEntity.builder().homeId(homeId).homeName(homeCreateOrUpdateRequest.homeName()).homeDescription(homeCreateOrUpdateRequest.homeDescription()).build();
        int row = homesMapper.update(homeEntity);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist", e);
            throw e;
        }
    }

    @Override
    public void deleteHome(Integer homeId) throws RecordNotFoundException {
        int row = homesMapper.delete(homeId);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("This home doesn't exist", e);
            throw e;
        }
    }
}
