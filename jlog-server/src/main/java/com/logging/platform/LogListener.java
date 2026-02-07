package com.logging.platform;

import com.logging.platform.mapper.LogDataMapper;
import com.logging.platform.models.LogData;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class LogListener {

    @Inject
    private LogDataMapper logDataMapper;

    @Inject
    private LogEntityRepository logEntityRepository;

    @Incoming("logs")
    @Transactional
    public void processLogEntry(LogData logData) {
        logEntityRepository.persist(logDataMapper.map(logData));
    }
}