package com.logging.platform.dto;

import com.logging.platform.models.LogDataExceptionFrame;

import java.util.ArrayList;
import java.util.List;

public class ExceptionDto {

    private int refIid;
    private String exceptionType;
    private String message;
    private List<LogDataExceptionFrame> frames = new ArrayList<>();

    public int getRefIid() {
        return refIid;
    }

    public void setRefIid(int refIid) {
        this.refIid = refIid;
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
