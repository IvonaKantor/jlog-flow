package com.logging.platform;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.openapi.quarkus.openapi_yaml.model.LogData;

import java.util.List;


@Path("/v1/log")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource {

    @GET
    public List<LogData> getLogList() {
        return List.of(); // mock
    }
}


