package com.fable.LogIngestionService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class GCPServiceImpl implements GCPService{
    private static final Logger logger = LoggerFactory.getLogger(GCPServiceImpl.class);


    private final Storage storage;
    private final String bucketName;
    private final int maxRetries = 5;
    private static final String FOLDER_NAME = "LogsFinal";
    GCPServiceImpl(@Value("${gcp.bucket.name}") String bucketName){
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucketName = bucketName;
    }
    @Override
    public void publishLogsToGcp(List<String> bufferData){
        int attempt = 0;
        boolean success = false;

        while(attempt < maxRetries && !success){
            try {
                logger.info("Attempting to publish logs to GCP");
                String fileName = "Log_Events_" + UUID.randomUUID();
                String combinedJsonData = String.join("\n", bufferData);
                InputStream jsonString = new ByteArrayInputStream(combinedJsonData.getBytes(StandardCharsets.UTF_8));
                BlobId blobId = BlobId.of(bucketName, FOLDER_NAME + "/" + fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/json").build();
                storage.create(blobInfo, jsonString);
                success = true;
                logger.info("Successfully created batch: {}", fileName);
            }catch (StorageException e){
                attempt++;
                logger.warn("Failed to publish logs to GCP, attempt {}/{}", attempt, maxRetries);

                if(attempt < maxRetries){
                    try {
                        long backoff = (long) Math.pow(2, attempt) * 200;
                        Thread.sleep(backoff);
                    }catch (InterruptedException interruptedException){
                        Thread.currentThread().interrupt();
                        logger.error("Thread interrupted during backoff", interruptedException);
                        break;
                    }
                }
                else{
                    logger.error("Reached max retries for publishing logs: {}", e.getMessage());
                }
            }
        }
    }
}
