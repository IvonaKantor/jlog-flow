package com.logging.platform.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static com.logging.platform.TestDataHelper.*;
import static com.logging.platform.models.LogDataLevel.ERROR;
import static com.logging.platform.models.LogDataLevel.INFO;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;
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

    private LogData logDataInfo;
    private LogData logDataError;

    @Inject
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        final var infoJson = Thread.currentThread().getContextClassLoader()
                .getResource("data/info.json");

        final var errorJson = Thread.currentThread().getContextClassLoader()
                .getResource("data/error.json");

        if (isNull(infoJson) || isNull(errorJson)) {
            throw new IllegalArgumentException("file not found!");
        }

        logDataInfo = objectMapper.readValue(new File(infoJson.toURI()), LogData.class);
        logDataError = objectMapper.readValue(new File(errorJson.toURI()), LogData.class);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        logEntityRepository.deleteAll();
    }

    @Test
    void logListenerTest() {

        logEmitter.send(logDataInfo);

        Mockito.verify(logListenerSpy, timeout(5000).times(1))
                .processLogEntry(any());
    }

    @Test
    void persistLogDataInfo() {

        logEmitter.send(logDataInfo);

        Awaitility.await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var log = findFirstLog(logEntityRepository).get();

        assertEquals(SERVICE_ID_1, log.getServiceId());
        assertEquals(SERVICE_NAME_1, log.getServiceName());
        assertEquals(INFO, log.getLevel());
        assertEquals(MESSAGE_INFO, log.getMessage());
        assertEquals(PROCESS_ID, log.getProcessId());
        assertEquals(PROCESS_NAME, log.getProcessName());
        assertTrue(nonNull(log.getTimestamp()));
    }

    @Test
    @Transactional
    void persistLogDataError() {
        logEmitter.send(logDataError);

        Awaitility.await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var log = findFirstLog(logEntityRepository).get();

        assertEquals(SERVICE_ID_1, log.getServiceId());
        assertEquals(SERVICE_NAME_1, log.getServiceName());
        assertEquals(ERROR, log.getLevel());
        assertEquals(MESSAGE_ERROR, log.getMessage());
        assertEquals(PROCESS_ID, log.getProcessId());
        assertEquals(PROCESS_NAME, log.getProcessName());
        assertTrue(nonNull(log.getTimestamp()));

        final var exception = log.getException();

        assertTrue(nonNull(exception));
        assertEquals(EXCEPTION_TYPE, exception.getExceptionType());
        assertEquals(MESSAGE_ERROR, exception.getMessage());
        assertEquals(7, exception.getFrames().size());

        exception.getFrames()
                .forEach(frame -> {
                    assertTrue(nonNull(frame));
                    assertFalse(frame.getClazz().isEmpty());
                    assertFalse(frame.getMethod().isEmpty());
                    assertNotEquals(0, frame.getLine());
                });
    }

}