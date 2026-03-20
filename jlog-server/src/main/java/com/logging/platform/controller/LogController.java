package com.logging.platform.controller;

import com.logging.platform.services.LogService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/v1/log")
public class LogController {

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
            @QueryParam("pageIndex") @DefaultValue("0") @Min(value = 0, message = "Page index must be >= 0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") @Min(value = 0, message = "Page size must be >= 1") @Max(value = 500, message = "Page size must be less than 500") int pageSize
    ) {
        return logService.getLogs(serviceIds, serviceNames, hostName, parseDate(fromDate), parseDate(toDate), level, pageIndex, pageSize);
    }

    private java.util.Date parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            java.time.Instant instant = java.time.Instant.parse(value);
            return java.util.Date.from(instant);
        } catch (java.time.format.DateTimeParseException e) {
            throw new BadRequestException("Invalid date format", e);
        }
    }

    @GET
    @Path("/services")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServices() {
        List<String> services = new ArrayList<>();
        services.add("service-1");
        services.add("service-2");

        return Response.ok(services).build();
    }

    @GET
    @Path("/hosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHosts() {
        List<String> hosts = new ArrayList<>();
        hosts.add("laptop-1r0rnidf");
        hosts.add("hp-840-g5");

        return Response.ok(hosts).build();
    }
}