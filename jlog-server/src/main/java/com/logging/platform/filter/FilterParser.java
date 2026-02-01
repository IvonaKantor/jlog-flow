package com.logging.platform.filter;


import com.logging.platform.dto.LogDto;
import com.logging.platform.models.LogDataLevel;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class FilterParser implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MultivaluedMap<String, String> queryParams =
                requestContext.getUriInfo().getQueryParameters();

        LogDto filter = new LogDto();

        setIfPresent(queryParams, "serviceId", filter::setServiceId);
        setIfPresent(queryParams, "serviceName", filter::setServiceName);

        if (queryParams.containsKey("level")) {
            try {
                filter.setLevel(LogDataLevel.valueOf(
                        queryParams.getFirst("level").toUpperCase()
                ));
            } catch (IllegalArgumentException ignored) {}
        }

        requestContext.setProperty("logFilter", filter);
    }

    private void setIfPresent(MultivaluedMap<String, String> params,
                              String paramName,
                              java.util.function.Consumer<String> setter) {
        if (params.containsKey(paramName)) {
            setter.accept(params.getFirst(paramName));
        }
    }
}