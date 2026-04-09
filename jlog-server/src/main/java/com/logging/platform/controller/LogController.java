package com.logging.platform.controller;

import com.logging.platform.models.LogSearchRequest;
import com.logging.platform.services.LogService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/v1/log")
public class LogController {

    private static final int MAX_SEARCH_LENGTH = 200;

    @Inject
    LogService logService;

    @GET
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public PaginationDataLog getLogList(
            @QueryParam("serviceId") Set<String> serviceIds,
            @QueryParam("serviceName") Set<String> serviceNames,
            @QueryParam("hostName") String hostName,
            @QueryParam("fromDate") String fromDate,
            @QueryParam("toDate") String toDate,
            @QueryParam("level") LogLevel level,
            @QueryParam("search") String search,
            @QueryParam("pageIndex") @DefaultValue("0") @Min(value = 0, message = "Page index must be >= 0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") @Min(value = 0, message = "Page size must be >= 1") @Max(value = 500, message = "Page size must be less than 500") int pageSize
    ) {
        return logService.getLogs(
                normalizeSearch(search),
                normalizeSet(serviceIds),
                normalizeSet(serviceNames),
                hostName,
                parseDate(fromDate, false),
                parseDate(toDate, true),
                level,
                pageIndex,
                pageSize
        );
    }

    @POST
    @Path("/search")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public PaginationDataLog searchLogs(LogSearchRequest request) {
        final var payload = request == null ? new LogSearchRequest() : request;

        final var pageIndex = normalizePageIndex(payload.getPageIndex());
        final var pageSize = normalizePageSize(payload.getPageSize());

        return logService.getLogs(
                normalizeSearch(payload.getSearch()),
                normalizeSet(payload.getServiceIds()),
                normalizeSet(payload.getServiceNames()),
                payload.getHostName(),
                parseDate(payload.getFromDate(), false),
                parseDate(payload.getToDate(), true),
                payload.getLevel(),
                pageIndex,
                pageSize
        );
    }

    private Date parseDate(String value, boolean endOfDayForDateOnly) {
        if (value == null || value.isBlank()) {
            return null;
        }

        final var trimmed = value.trim();

        try {
            return Date.from(Instant.parse(trimmed));
        } catch (DateTimeParseException ignored) {
        }

        try {
            return Date.from(OffsetDateTime.parse(trimmed).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        try {
            final var localDateTime = LocalDateTime.parse(trimmed);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        try {
            final var localDate = LocalDate.parse(trimmed);
            final var localDateTime = endOfDayForDateOnly ? localDate.atTime(23, 59, 59, 999_000_000) : localDate.atStartOfDay();
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        try {
            final var localDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            final var localDateTime = endOfDayForDateOnly ? localDate.atTime(23, 59, 59, 999_000_000) : localDate.atStartOfDay();
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        throw new BadRequestException("Invalid date format. Expected ISO-8601 date or date-time.");
    }

    private int normalizePageIndex(Integer value) {
        final int pageIndex = value == null ? 0 : value;
        if (pageIndex < 0) {
            throw new BadRequestException("Page index must be >= 0");
        }

        return pageIndex;
    }

    private int normalizePageSize(Integer value) {
        final int pageSize = value == null ? 20 : value;

        if (pageSize < 1 || pageSize > 500) {
            throw new BadRequestException("Page size must be between 1 and 500");
        }

        return pageSize;
    }

    private String normalizeSearch(String value) {
        if (value == null) {
            return null;
        }

        final var search = value.trim();
        if (search.isEmpty()) {
            return null;
        }

        if (search.length() > MAX_SEARCH_LENGTH) {
            throw new BadRequestException("Search length must be <= " + MAX_SEARCH_LENGTH);
        }

        return search;
    }

    private Set<String> normalizeSet(Set<String> input) {
        return Objects.requireNonNullElse(input, Set.of());
    }

    @GET
    @Path("/services")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getServices() {
        return logService.getServiceNames();
    }

    @GET
    @Path("/hosts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getHosts() {
        return logService.getHostNames();
    }
}