package com.stori.quota.api;

import com.stori.quota.biz.SimulateUserOptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@EnableScheduling
@Component
@Slf4j
public class ScheduleTask {

    @Resource
    private SimulateUserOptService simulateUserOptService;

    /**
     * 定时任务，5秒一次
     * 调用模拟用户随机操作服务
     */
    @Scheduled(cron="*/5 * * * * ?")//每秒钟执行一次，以空格分隔
    public void cron() {
        simulateUserOptService.randomOpt();
    }

}
