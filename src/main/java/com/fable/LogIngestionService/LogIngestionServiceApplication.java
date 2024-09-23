package com.fable.LogIngestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class LogIngestionServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(LogIngestionServiceApplication.class);

	public static void main(String[] args) {

		logger.info("Starting Log Ingestion Service Application...");

		SpringApplication.run(LogIngestionServiceApplication.class, args);

		logger.info("Log Ingestion Service Application started successfully.");

	}
}
