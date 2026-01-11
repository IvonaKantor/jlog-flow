
package com.logging.platform.entity;

import com.logging.platform.models.LogDataLevel;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "logs")
public class LogEntity extends PanacheEntity {

    private String loggerClassName;

    private String loggerName;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private Integer sequence;

    @Enumerated
    @Column(nullable = false)
    private LogDataLevel level;

    @Column(nullable = false)
    private String message;

    private String threadName;

    private int threadId;

    private String ndc;

    @Column(nullable = false)
    private String processName;

    @Column(nullable = false)
    private Integer processId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity serviceEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exception_id")
    private ExceptionEntity exceptionEntity;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> mdc;

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    public ExceptionEntity getExceptionEntity() {
        return exceptionEntity;
    }

    public void setExceptionEntity(ExceptionEntity exceptionId) {
        this.exceptionEntity = exceptionId;
    }

    public Map<String, Object> getMdc() {
        return mdc;
    }

    public void setMdc(Map<String, Object> mdc) {
        this.mdc = mdc;
    }
}