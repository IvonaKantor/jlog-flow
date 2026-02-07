package com.logging.platform.controller;

import com.logging.platform.pagination.Pagination;
import com.logging.platform.services.LogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.openapi.quarkus.openapi_yaml.model.LogData;

import java.util.Set;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/v1/log")
public class LogController {

    @Inject
    private LogService logService;

    @GET
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Pagination<LogData> getLogList(
            @QueryParam("serviceId") Set<String> serviceIds,
            @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    ) {
        return logService.getLogs(serviceIds, pageIndex, pageSize);
    }

}


