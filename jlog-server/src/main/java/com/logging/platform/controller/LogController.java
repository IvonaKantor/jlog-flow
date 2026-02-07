package com.logging.platform.controller;

import com.logging.platform.services.LogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.openapi.quarkus.openapi_yaml.model.LogData;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/v1/log")

public class LogController {

    @Inject
    private LogService logService;

    @GET
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public List<LogData> getLogList(@QueryParam("serviceId") String serviceId) {
        return logService.getLogs(serviceId);
    }

}


