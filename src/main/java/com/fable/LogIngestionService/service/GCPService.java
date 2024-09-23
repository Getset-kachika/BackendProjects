package com.fable.LogIngestionService.service;

import java.util.List;

public interface GCPService {
    void publishLogsToGcp(List<String>bufferData);

}
