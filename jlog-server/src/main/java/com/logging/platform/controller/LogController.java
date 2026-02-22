package com.logging.platform.controller;

import com.logging.platform.services.LogService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

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
            @QueryParam("level") LogLevel level,
            @QueryParam("pageIndex") @DefaultValue("0") @Min(value = 0, message = "Page index must be >= 0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") @Min(value = 0, message = "Page size must be >= 1") @Max(value = 500, message = "Page size must be less than 500") int pageSize
    ) {
        return logService.getLogs(serviceIds, serviceNames, level, pageIndex, pageSize);
    }
}