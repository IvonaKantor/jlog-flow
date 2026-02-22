package com.logging.platform.api;

import com.logging.platform.listener.LogListener;
import com.logging.platform.models.LogData;
import com.logging.platform.models.LogDataLevel;
import com.logging.platform.repository.LogEntityRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

import java.util.concurrent.CompletionStage;

import static com.logging.platform.TestDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.openapi.quarkus.openapi_yaml.model.LogLevel.ERROR;
import static org.openapi.quarkus.openapi_yaml.model.LogLevel.INFO;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class LogControllerTest {

    @Inject
    LogEntityRepository logEntityRepository;

    @Inject
    @Channel("logs")
    Emitter<LogData> logEmitter;

    @InjectSpy
    private LogListener logListenerSpy;

    @AfterEach
    @Transactional
    void tearDown() {
        logEntityRepository.deleteAll();
    }

    @Test
    void testGetAllLogs() throws Exception {
        for (int i = 0; i < 20; i++) {
            logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
            logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));
        }

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaginationDataLog.class);

        assertNotNull(response.getItems());
        assertFalse(response.getItems().isEmpty());

        assertEquals(2, response.getPageCount());
        assertEquals(0, response.getPageIndex());
        assertEquals(20, response.getPageSize());
        assertEquals(20, response.getItems().size());
    }

    @Test
    void testGetLogsByServiceId() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("serviceId", SERVICE_ID_1)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaginationDataLog.class);

        assertNotNull(response.getItems());
        assertFalse(response.getItems().isEmpty());

        assertEquals(1, response.getPageCount());
        assertEquals(0, response.getPageIndex());
        assertEquals(20, response.getPageSize());
        assertEquals(1, response.getItems().size());

        response.getItems().forEach(log -> {
            assertEquals(SERVICE_ID_1, log.getServiceId());
            assertEquals(SERVICE_NAME_1, log.getServiceName());
            assertEquals(INFO, log.getLevel());
            assertFalse(log.getMessage().isEmpty());
        });
    }

    @Test
    void testGetLogsByServiceIds() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("serviceId", SERVICE_ID_1, SERVICE_ID_2)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaginationDataLog.class);

        assertNotNull(response.getItems());
        assertFalse(response.getItems().isEmpty());

        assertEquals(1, response.getPageCount());
        assertEquals(0, response.getPageIndex());
        assertEquals(20, response.getPageSize());
        assertEquals(2, response.getItems().size());
    }

    @Test
    void testGetLogsByServiceName() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("serviceName", SERVICE_NAME_1)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaginationDataLog.class);

        assertNotNull(response.getItems());
        assertFalse(response.getItems().isEmpty());

        assertEquals(1, response.getPageCount());
        assertEquals(0, response.getPageIndex());
        assertEquals(20, response.getPageSize());
        assertEquals(1, response.getItems().size());

        response.getItems().forEach(log -> {
            assertEquals(SERVICE_NAME_1, log.getServiceName());
        });
    }

    @Test
    void testGetLogsByServiceNames() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("serviceName", SERVICE_NAME_1, SERVICE_NAME_2)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaginationDataLog.class);

        assertNotNull(response.getItems());
        assertFalse(response.getItems().isEmpty());

        assertEquals(1, response.getPageCount());
        assertEquals(0, response.getPageIndex());
        assertEquals(20, response.getPageSize());
        assertEquals(2, response.getItems().size());

        assertTrue(response.getItems().stream().anyMatch(log -> log.getServiceName().equals(SERVICE_NAME_1)));
        assertTrue(response.getItems().stream().anyMatch(log -> log.getServiceName().equals(SERVICE_NAME_2)));
    }

    @Test
    void testGetLogsBySingleLevel() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataError(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("level", LogDataLevel.ERROR)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .extract()
                .as(PaginationDataLog.class);

        assertEquals(1, response.getItems().size());
        assertEquals(ERROR, response.getItems().get(0).getLevel());
    }

    @Test
    void testNoLogsFoundForValidLevel() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("level", LogDataLevel.DEBUG)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .extract()
                .as(PaginationDataLog.class);

        assertTrue(response.getItems().isEmpty(), "Items list empty for debug level");
    }

    @Test
    void noLogsFoundByServiceId() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("serviceId", "unknown")
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .extract()
                .as(PaginationDataLog.class);

        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void testFilterByLevelAndServiceId() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataError(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataError(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("level", LogDataLevel.ERROR)
                .queryParam("serviceId", SERVICE_ID_1)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .extract()
                .as(PaginationDataLog.class);

        assertEquals(1, response.getItems().size());
        assertEquals(LogLevel.ERROR, response.getItems().get(0).getLevel());
        assertEquals(SERVICE_ID_1, response.getItems().get(0).getServiceId());
    }

    @Test
    void testInvalidLevelValue() {
        given()
                .queryParam("level", "WARNING")
                .when()
                .get("/v1/log")
                .then()
                .statusCode(404);
    }

    @Test
    void noLogsFoundByServiceName() throws Exception {
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_1, SERVICE_NAME_1)));
        logEmitter.send(createLogDataInfo(new ServiceData(SERVICE_ID_2, SERVICE_NAME_2)));

        await().until(() -> findFirstLog(logEntityRepository).isPresent());

        final var response = given()
                .queryParam("serviceName", "unknown")
                .when()
                .get("/v1/log")
                .then()
                .statusCode(200)
                .extract()
                .as(PaginationDataLog.class);

        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void invalidPageParameters() {
        given()
                .queryParam("pageIndex", -1)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(400)
                .body(containsString("Page index must be >= 0"));
    }

    @Test
    void emptyServiceId() {
        given()
                .queryParam("pageSize", -1)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(400)
                .body(containsString("Page size must be >= 1"));

        given()
                .queryParam("pageSize", 501)
                .when()
                .get("/v1/log")
                .then()
                .statusCode(400)
                .body(containsString("Page size must be less than 500"));
    }


}