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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getLoggerClassName() {
        return loggerClassName;
    }

    public void setLoggerClassName(String loggerClassName) {
        this.loggerClassName = loggerClassName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public LogDataLevel getLevel() {
        return level;
    }

    public void setLevel(LogDataLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }

    public Map<String, Object> getMdc() {
        return mdc;
    }

    public void setMdc(Map<String, Object> mdc) {
        this.mdc = mdc;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public LogDataException getException() {
        return exception;
    }

    public void setException(LogDataException exception) {
        this.exception = exception;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
