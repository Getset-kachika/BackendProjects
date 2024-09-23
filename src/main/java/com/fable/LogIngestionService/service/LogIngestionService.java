package com.fable.LogIngestionService.service;

import com.fable.LogIngestionService.model.DataNode;
import com.fable.LogIngestionService.model.LogEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LogIngestionService {
    void processLogs(DataNode data) throws JsonProcessingException;
}
