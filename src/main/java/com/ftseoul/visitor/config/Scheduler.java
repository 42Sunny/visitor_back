package com.ftseoul.visitor.config;

import com.ftseoul.visitor.data.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private final VisitorRepository visitorRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updateExpiredVisitor() {
        log.info("Update Expired Visitors");
        try {
            int affectedRows = visitorRepository.updateExpiredVisitors();
            log.info("Affected Rows: {}", affectedRows);
        } catch (RuntimeException ex) {
            log.error("Faild to update expired visitors : {}", ex.getMessage());
        }
    }
}
