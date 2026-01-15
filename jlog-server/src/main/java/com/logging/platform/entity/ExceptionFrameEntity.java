package com.logging.platform.entity;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "exception_frames")
public class ExceptionFrameEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exception_id")
    private ExceptionEntity exceptionId;

    @Column(nullable = false)
    private String clazz;

    @Column(nullable = false)
    private String method;

    private int line;

    public ExceptionEntity getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(ExceptionEntity exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClass(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
