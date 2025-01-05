package me.huangduo.hms.service;

import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;

public interface HomeService {

    void createHome(HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) throws HomeAlreadyExistsException;

    HomeEntity getHomeInfo(Integer homeId) throws RecordNotFoundException;

    void updateHomeInfo(Integer homeId, HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) throws RecordNotFoundException;

    void deleteHome(Integer homeId) throws RecordNotFoundException;
}
