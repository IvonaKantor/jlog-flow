package com.logging.platform.dto;

import com.logging.platform.models.LogDataExceptionFrame;

import java.util.ArrayList;
import java.util.List;

public class ExceptionDto {

    private int refIid;
    private String exceptionType;
    private String message;
    private List<LogDataExceptionFrame> frames = new ArrayList<>();
}
