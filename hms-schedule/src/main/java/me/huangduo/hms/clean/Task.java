package me.huangduo.hms.clean;

import me.huangduo.hms.service.CleanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {

    private final CleanService cleanService;

    public Task(CleanService cleanService) {
        this.cleanService = cleanService;
    }

    @Scheduled(cron = "0 0 0 * * WED")
    public void run() {
        System.out.println(123);
        cleanService.clean();
    }
}
