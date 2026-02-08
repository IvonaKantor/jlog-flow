package com.logging.platform.listener;

import com.logging.platform.models.LogData;
import com.logging.platform.repository.LogEntityRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.logging.platform.TestDataHelper.*;
import static com.logging.platform.models.LogDataLevel.ERROR;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;


@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class LogListenerTest {

    @Inject
    @Channel("logs")
    private Emitter<LogData> logEmitter;

    @Inject
    private LogEntityRepository logEntityRepository;

    @InjectSpy
    private LogListener logListenerSpy;

    @AfterEach
    @Transactional
    void tearDown() {
        logEntityRepository.deleteAll();
    }

    @Test
    void logListenerTest() {

        logEmitter.send(getLogInstance());

        Mockito.verify(logListenerSpy, timeout(5000).times(1))
                .processLogEntry(any());
    }

    @Test
    void persistLogDataTest() {
        logEmitter.send(getLogInstance());

        Awaitility.await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var log =  findFirstLog(logEntityRepository).get();

        assertEquals(SERVICE_ID_1, log.getServiceId());
        assertEquals(SERVICE_NAME_1, log.getServiceName());
        assertEquals(ERROR, log.getLevel());
        assertEquals(MESSAGE, log.getMessage());
        assertEquals(PROCESS_ID, log.getProcessId());
        assertEquals(PROCESS_NAME, log.getProcessName());
        assertTrue(nonNull(log.getTimestamp()));
    }

}