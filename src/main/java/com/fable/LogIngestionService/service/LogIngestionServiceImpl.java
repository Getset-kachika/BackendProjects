package com.fable.LogIngestionService.service;

import com.fable.LogIngestionService.model.DataNode;
import com.fable.LogIngestionService.model.LogEvent;
import com.fable.LogIngestionService.util.LogsBufferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LogIngestionServiceImpl implements LogIngestionService{
    @Autowired
    private GCPService gcpService;
    @Autowired
    private LogsBufferService logsBufferService;
    @Autowired
    private ObjectMapper objectMapper;

    //private String logEventString;

    private static final Logger logger = LoggerFactory.getLogger(LogIngestionServiceImpl.class);

    public void processLogs(DataNode data){
        logger.info("Starting to publish log data to S3");

        System.out.println("publishTos3!");
        LogEvent logEvent = new LogEvent();
        logEvent.setData(data);
        logEvent.setUnix_timestamp(Instant.now().getEpochSecond());
        try {
            String logEventString = objectMapper.writeValueAsString(logEvent);//added String
            logsBufferService.add(logEventString);
            logger.info("Log data published successfully");

        }catch (JsonProcessingException e){
            logger.error("Error processing log event to JSON: {}", e.getMessage(), e);
        }
        catch (Exception e){
            logger.error("Failed to add log event to buffer: {}", e.getMessage(), e);
        }
    }
}
