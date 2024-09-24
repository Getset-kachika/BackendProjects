package com.fable.LogIngestionService.model;


import com.fasterxml.jackson.annotation.JsonProperty;
public class DataNode {
    public String getEvent_name() {
        return eventName;
    }

    public void setEvent_name(String event_name) {
        this.eventName = event_name;
    }
    @JsonProperty("event_name")
    private String eventName;

}
