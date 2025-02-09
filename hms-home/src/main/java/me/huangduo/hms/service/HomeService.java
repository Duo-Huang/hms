package me.huangduo.hms.service;

import me.huangduo.hms.model.Home;
import me.huangduo.hms.model.User;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.RecordNotFoundException;

public interface HomeService {
    void createHome(Home home, User user) throws HomeAlreadyExistsException, IllegalArgumentException;

    Home getHomeInfo(Integer homeId) throws RecordNotFoundException;

    void updateHomeInfo(Home home) throws RecordNotFoundException;

    void deleteHome(Integer homeId) throws RecordNotFoundException;
}
