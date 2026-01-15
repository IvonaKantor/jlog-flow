package com.logging.platform.entity;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "services")
public class ServiceEntity extends PanacheEntity {

    @Column(unique = true)
    private String serviceId;

    @Column(nullable = false, unique = true)
    private String serviceName;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<LogEntity> logs;

    public static Optional<ServiceEntity> findByServiceId(String serviceId) {
        return find("serviceId", serviceId).firstResultOptional();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<LogEntity> getLogs() {
        return logs;
    }

    public void setLogs(List<LogEntity> logs) {
        this.logs = logs;
    }
}