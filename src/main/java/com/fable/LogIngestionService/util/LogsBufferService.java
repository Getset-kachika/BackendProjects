package com.fable.LogIngestionService.util;

import com.fable.LogIngestionService.service.GCPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LogsBufferService {

    private static final Logger logger = LoggerFactory.getLogger(LogsBufferService.class);

    @Autowired
    private GCPService gcpService;
    private final int  initialBufferSize = 20000;

    private final int flushItAt = 2000;
    private LinkedBlockingQueue<String> buffer;

    private final List<String> extraBuffer = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    private volatile boolean isFlushing = false;//added volatile

    LogsBufferService(){
        this.buffer = new LinkedBlockingQueue<>(initialBufferSize);
        new Thread(this::flushLoop).start();
    }

    public void add(String logEventString) {
        if (!buffer.offer(logEventString)) {
            handleIncomingRequests(logEventString);
        } else if (buffer.size() >= flushItAt && !isFlushing) {
            callGCPService();
        }
    }

    public void handleIncomingRequests(String logEventString){
        extraBuffer.add(logEventString);
        logger.warn("Buffer is full, handling incoming request");
    }
    @Scheduled(fixedRate = 10000)
    private void flushLoop() {
        if (!buffer.isEmpty() && !isFlushing) {
            callGCPService();
            flushExtraBuffer();
        }
    }
    private void flushExtraBuffer() {
        if (!extraBuffer.isEmpty()) {
            List<String> overflowDataBatch = new ArrayList<>(extraBuffer);
            extraBuffer.clear();
            gcpService.publishLogsToGcp(overflowDataBatch);
            logger.info("Flushed overflow data to GCP");
        }
    }
    public void callGCPService(){
        lock.lock();
        try {
            isFlushing = true;
            List<String> batchData = new ArrayList<>();
            while (batchData.size() < flushItAt && !buffer.isEmpty()) {
                String jsonData = buffer.poll();
                if (jsonData!=null && !jsonData.isEmpty()) {
                    batchData.add(jsonData);
                }
            }
            logger.info("Flushed {} log events to GCP", batchData.size());
            if (!batchData.isEmpty()) {
                gcpService.publishLogsToGcp(batchData);
                logger.info("Flushed {} log events to GCP",batchData.size());
            }
        }finally {
            isFlushing = false;
            lock.unlock();
        }
    }
}
