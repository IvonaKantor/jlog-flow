package com.logging.platform.models;

import org.openapi.quarkus.openapi_yaml.model.LogLevel;

import java.util.Set;

public class LogSearchRequest {

    private Set<String> serviceIds;
    private Set<String> serviceNames;
    private String hostName;
    private String fromDate;
    private String toDate;
    private LogLevel level;
    private Integer pageIndex;
    private Integer pageSize;

    public Set<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(Set<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public Set<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(Set<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
