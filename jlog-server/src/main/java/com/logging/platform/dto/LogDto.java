package com.logging.platform.dto;

import com.logging.platform.models.LogDataException;
import com.logging.platform.models.LogDataLevel;

import java.util.Date;
import java.util.Map;

public class LogDto {

    private Date timestamp;
    private Integer sequence;
    private String loggerClassName;
    private String loggerName;
    private LogDataLevel level;
    private String message;
    private String threadName;
    private Integer threadId;
    private String ndc;
    private Map<String, Object> mdc;
    private String hostName;
    private String processName;
    private Integer processId;
    private LogDataException exception;
    private String serviceName;
    private String serviceId;
}
