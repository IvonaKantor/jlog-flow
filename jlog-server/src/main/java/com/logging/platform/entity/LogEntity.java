
package com.logging.platform.entity;

import com.logging.platform.models.LogDataLevel;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "log", indexes = {
        @Index(columnList = "serviceName"),
        @Index(columnList = "serviceId"),
        @Index(columnList = "level")
})
public class LogEntity {

    @Id
    @GeneratedValue
    public Long id;

    @Column(nullable = false)
    private Date timestamp;

    private Integer sequence;

    private String loggerClassName;

    private String loggerName;

    @Enumerated
    @Column(nullable = false)
    private LogDataLevel level;

    @Column(nullable = false)
    private String message;

    private String threadName;

    private int threadId;

    private String ndc;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_v")
    private Map<String, Object> mdc;

    private String hostName;

    @Column(nullable = false)
    private String processName;

    @Column(nullable = false)
    private Integer processId;

    @Column(nullable = false)
    private String serviceId;

    @Column(nullable = false)
    private String serviceName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exception_id")
    private ExceptionEntity exception;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
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

    public ExceptionEntity getException() {
        return exception;
    }

    public void setException(ExceptionEntity exception) {
        this.exception = exception;
    }

    public String getServiceId() {
        return serviceId;
    }

    public LogEntity setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public LogEntity setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }
}