package com.logging.platform.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "exceptions")
public class ExceptionEntity extends PanacheEntity {

    private int refId;

    @Column(nullable = false)
    private String exceptionType;

    private String message;

    @OneToMany(mappedBy = "exceptionId", cascade = CascadeType.ALL)
    private List<ExceptionFrameEntity> frames = new ArrayList<>();

    @OneToOne(mappedBy = "exception")
    private LogEntity log;

    @PrePersist
    public void onPrePersist() {
        log.setException(this);

        frames.forEach(frame -> frame.setExceptionId(this));
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
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

    public List<ExceptionFrameEntity> getFrames() {
        return frames;
    }

    public void setFrames(List<ExceptionFrameEntity> frames) {
        this.frames = frames;
    }

    public LogEntity getLog() {
        return log;
    }

    public void setLog(LogEntity log) {
        this.log = log;
    }
}
