package com.fable.LogIngestionService.controller;

import com.fable.LogIngestionService.model.DataNode;
import com.fable.LogIngestionService.service.LogIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogIngestionController {
    private static final Logger logger = LoggerFactory.getLogger(LogIngestionController.class);
    @Autowired
    LogIngestionService logIngestionService;
        @PostMapping("/log")
        //@Async("asyncExecutor")
        public void logsControllerAsync(@RequestBody DataNode data) {
            try {
                logger.info("logsControllerAsync");
                logIngestionService.processLogs(data);
            } catch (Exception e) {
                logger.error("Error publishing log data: {}", e.getMessage(), e);
            }
        }
    }

