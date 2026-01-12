package com.logging.platform.models;

import java.util.ArrayList;
import java.util.List;

public class LogDataException {
    private Long refId;
    private String exceptionType;
    private String message;
    private List<LogDataExceptionFrame> frames = new ArrayList<>();

    public LogDataException() {
    }

    public LogDataException(String exceptionType, String message) {
        this.exceptionType = exceptionType;
        this.message = message;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LogDataExceptionFrame> getFrames() {
        return frames;
    }

    public void setFrames(List<LogDataExceptionFrame> frames) {
        this.frames = frames;
    }
}