package com.logging.platform;

import com.logging.platform.entity.LogEntity;
import com.logging.platform.models.LogData;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.logging.platform.models.LogDataLevel.ERROR;

public class TestDataHelper {
    public static final String SERVICE_ID_1 = "service1";
    public static final String SERVICE_ID_2 = "service2";
    public static final String SERVICE_NAME_1 = "app-1";
    public static final String SERVICE_NAME_2 = "app-2";

    public static final String PROCESS_NAME = "java.exe";

    public static final String MESSAGE = "test message";

    public static final int PROCESS_ID = 564;

    private TestDataHelper() {}

    public static LogData getLogInstance() {
        final var log = new LogData();

        log.setServiceId(SERVICE_ID_1);
        log.setServiceName(SERVICE_NAME_1);
        log.setLevel(ERROR);
        log.setMessage(MESSAGE);
        log.setProcessId(PROCESS_ID);
        log.setProcessName(PROCESS_NAME);
        log.setTimestamp(Date.from(Instant.now()));

        return log;
    }

    @Transactional
    public static Optional<LogEntity> findFirstLog(LogEntityRepository logEntityRepository) {
        return logEntityRepository.findAll().stream().findFirst();
    }
}
