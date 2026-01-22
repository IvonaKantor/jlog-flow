package com.logging.platform;

import com.logging.platform.entity.ServiceEntity;
import com.logging.platform.mapper.LogDataMapper;
import com.logging.platform.models.LogData;
import com.logging.platform.models.LogDataLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.List;

@ApplicationScoped
public class LogListener {

    @Inject
    LogDataMapper logDataMapper;

    @Incoming("logs")
    @Transactional
    public void processLogEntry(LogData logData) {
        final var serviceId = logData.getServiceId();
        final var logEntity = logDataMapper.map(logData);

        final var existServiceEntity = ServiceEntity.findByServiceId(serviceId);

        if (existServiceEntity.isPresent()) {
            logEntity.setService(existServiceEntity.get());
            if (logEntity.getLevel().equals(LogDataLevel.ERROR)) {
                logEntity.getException().setLog(logEntity);
            }
            logEntity.persist();
        } else {
            final var serviceEntity = new ServiceEntity();
            serviceEntity.setServiceId(serviceId);
            serviceEntity.setServiceName(logData.getServiceName());
            serviceEntity.setLogs(List.of(logEntity));
            serviceEntity.persist();
        }
    }
}