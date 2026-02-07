package com.logging.platform.services;

import com.logging.platform.entity.ServiceEntity;
import com.logging.platform.mapper.LogApiMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.openapi.quarkus.openapi_yaml.model.LogData;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class LogService {

    @Inject
    private LogApiMapper  logApiMapper;

    public List<LogData> getLogs(final String serviceId) {
        final var service = ServiceEntity.findByServiceId(serviceId);

        return logApiMapper.map(service.isEmpty()
                ? Collections.emptyList()
                : service.get().getLogs());
    }
}
