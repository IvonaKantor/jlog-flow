package com.logging.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logging.platform.entity.LogEntity;
import com.logging.platform.models.LogData;
import com.logging.platform.models.LogDataLevel;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDataHelper {
    public static final String SERVICE_ID_1 = "app-7865";
    public static final String SERVICE_ID_2 = "app-9882";
    public static final String SERVICE_ID_3 = "app-9889";
    public static final String SERVICE_NAME_1 = "service-1";
    public static final String SERVICE_NAME_2 = "service-2";
    public static final String SERVICE_NAME_3 = "service-3";

    public static final String PROCESS_NAME = "C:\\Users\\user\\.jdks\\azul-25.0.2\\bin\\java.exe";

    public static final String MESSAGE_INFO = "Quarkus application starting. Initializing log generator.";
    public static final String MESSAGE_ERROR = "test exception";

    public static final int PROCESS_ID = 27820;

    public static final String EXCEPTION_TYPE = "java.lang.RuntimeException";

    final static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private TestDataHelper() {
    }

    @Transactional
    public static Optional<LogEntity> findFirstLog(LogEntityRepository logEntityRepository) {
        return logEntityRepository.findAll().stream().findFirst();
    }

    public static LogData createLogDataInfo(final ServiceData serviceData) throws Exception {
        final var infoJson = Thread.currentThread().getContextClassLoader()
                .getResource("data/info.json");

        assertNotNull(infoJson);

        final var log = mapper.readValue(new File(infoJson.toURI()), LogData.class);

        log.setServiceId(serviceData.serviceId);
        log.setServiceName(serviceData.serviceName);
        log.setLevel(LogDataLevel.INFO);

        return log;
    }

    public static LogData createLogDataError(final ServiceData serviceData) throws Exception {
        final var errorJson = Thread.currentThread().getContextClassLoader()
                .getResource("data/error.json");

        assertNotNull(errorJson);

        final var log = mapper.readValue(new File(errorJson.toURI()), LogData.class);

        log.setServiceId(serviceData.serviceId);
        log.setServiceName(serviceData.serviceName);
        log.setLevel(LogDataLevel.ERROR);

        return log;
    }

    public record ServiceData(String serviceId, String serviceName) {
    }
}