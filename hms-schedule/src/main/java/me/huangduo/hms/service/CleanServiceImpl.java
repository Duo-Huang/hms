package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CleanServiceImpl implements CleanService {

    private final ScheduleDao scheduleDao;

    public CleanServiceImpl(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }


    @Override
    public void clean() {
        cleanMessages();
        cleanRevokedTokens();
    }

    private void cleanMessages() {
        int row = scheduleDao.deleteExpiredMessages();
        log.info("[cleanMessages] {} messages has been deleted", row);
    }

    private void cleanRevokedTokens() {
        int row = scheduleDao.deleteExpiredRevokedTokens();
        log.info("[cleanRevokedTokens] {} revoked tokens has been deleted", row);
    }
}
