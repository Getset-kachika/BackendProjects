package com.fable.LogIngestionService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogEvent {
    @JsonProperty("unix_timestamp")
    private long unixTimeStamp;
    protected DataNode dataNode;
    public DataNode getData() {
        return dataNode;
    }

    public void setData(DataNode dataNode) {
        this.dataNode = dataNode;
    }
    public long getUnix_timestamp() {
        return unixTimeStamp;
    }

    public void setUnix_timestamp(long unixTimeStamp) {
        this.unixTimeStamp = unixTimeStamp;
    }

}
