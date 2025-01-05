package me.huangduo.hms.service;

import me.huangduo.hms.dao.HomesMapper;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
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
        } catch (DuplicateKeyException e) {
            throw new HomeAlreadyExistsException();
        }
    }

    @Override
    public HomeEntity getHomeInfo(Integer homeId) throws RecordNotFoundException {
        HomeEntity homeInfo = homesMapper.getById(homeId);
        if (Objects.isNull(homeInfo)) {
            throw new RecordNotFoundException("This home doesn't exist");
        }
        return homeInfo;
    }

    @Override
    public void updateHomeInfo(Integer homeId, HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) throws RecordNotFoundException {
        HomeEntity homeEntity = HomeEntity.builder().homeId(homeId).homeName(homeCreateOrUpdateRequest.homeName()).homeDescription(homeCreateOrUpdateRequest.homeDescription()).build();
        int row = homesMapper.update(homeEntity);
        if (row == 0) {
            throw new RecordNotFoundException("This home doesn't exist");
        }
    }

    @Override
    public void deleteHome(Integer homeId) throws RecordNotFoundException {
        int row = homesMapper.delete(homeId);
        if (row == 0) {
            throw new RecordNotFoundException("This home doesn't exist");
        }
    }
}
