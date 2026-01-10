package com.logging.platform;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/v1/log")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource {

    @GET
    public String getLogList() {
        return "Log list";
    }

    @GET
    @Path("{id}")
    public String getLog(String id) {
        return "log" + id;
    }

    @DELETE
    @Path("{id}")
    public void deleteLog(String id) {
    }
}


