package com.axin.errand.task;

import com.axin.errand.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 智能派单兜底定时任务。
 * 由后台「定时任务」页配置调用目标 dispatchTask.scan()，建议 cron 每 15 秒执行一次。
 */
@Component("dispatchTask")
public class DispatchTask {

    @Autowired
    private DispatchService dispatchService;

    public void scan() {
        dispatchService.scanTimeout();
    }
}
