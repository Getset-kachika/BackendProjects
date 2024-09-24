package com.fable.LogIngestionService.model;


public class LogEvent {
    private long unix_timestamp;
    protected DataNode dataNode;
    public DataNode getData() {
        return dataNode;
    }

    public void setData(DataNode dataNode) {
        this.dataNode = dataNode;
    }
    public long getUnix_timestamp() {
        return unix_timestamp;
    }

    public void setUnix_timestamp(long unix_timestamp) {
        this.unix_timestamp = unix_timestamp;
    }

}
