package com.logging.platform.repository;

import com.logging.platform.entity.LogEntity;
import com.logging.platform.models.LogDataLevel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;

import java.util.Set;

@ApplicationScoped
public class LogEntityRepository implements PanacheRepository<LogEntity> {

    public PanacheQuery<LogEntity> find(
            final Set<String> serviceIds,
            final Set<String> serviceNames,
            final LogLevel level
    ) {
        final var query = new StringBuilder();
        final var params = new Parameters();

        if (!serviceIds.isEmpty()) {
            query.append("serviceId in :serviceIds");
            params.and("serviceIds", serviceIds);
        }

        if (!serviceNames.isEmpty()) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" serviceName in :serviceNames");

            params.and("serviceNames", serviceNames);
        }

        if (level != null) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" level = :level");

            params.and("level", LogDataLevel.valueOf(level.toString()));
        }

        return find(query.toString(), params);
    }
}