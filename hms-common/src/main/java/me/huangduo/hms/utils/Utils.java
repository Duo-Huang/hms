package me.huangduo.hms.utils;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class Utils {

    private final CommonDao commonDao;

    public Utils(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public Home checkHomeExisted(Integer homeId, String errLogMsg, HmsErrorCodeEnum hmsErrorCodeEnum) {
        Home home = null;
        if (homeId == null || Objects.isNull(home = commonDao.getHomeById(homeId))) {
            BusinessException e = new RecordNotFoundException(hmsErrorCodeEnum);
            log.error(errLogMsg, e);
            throw e;
        }

        return home;
    }
}