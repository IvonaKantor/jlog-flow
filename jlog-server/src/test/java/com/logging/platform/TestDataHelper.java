package com.logging.platform;

import com.logging.platform.entity.LogEntity;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

public class TestDataHelper {
    public static final String SERVICE_ID_1 = "app-7865";
    public static final String SERVICE_NAME_1 = "service-1";

    public static final String PROCESS_NAME = "C:\\Users\\user\\.jdks\\azul-25.0.2\\bin\\java.exe";

    public static final String MESSAGE_INFO = "Quarkus application starting. Initializing log generator.";
    public static final String MESSAGE_ERROR = "test exception";

    public static final int PROCESS_ID = 27820;

    public static final String EXCEPTION_TYPE = "java.lang.RuntimeException";

    private TestDataHelper() {}

    @Transactional
    public static Optional<LogEntity> findFirstLog(LogEntityRepository logEntityRepository) {
        return logEntityRepository.findAll().stream().findFirst();
    }
}
