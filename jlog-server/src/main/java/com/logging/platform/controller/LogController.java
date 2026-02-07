package com.logging.platform.controller;

import com.logging.platform.services.LogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

import java.util.Set;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/v1/log")
public class LogController {

    @Inject
    private LogService logService;

    @GET
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public PaginationDataLog getLogList(
            @QueryParam("serviceId") Set<String> serviceIds,
            @QueryParam("serviceName") Set<String> serviceNames,
            @QueryParam("level") LogLevel level,
            @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        return logService.getLogs(serviceIds, serviceNames, level, pageIndex, pageSize);
    }

}


