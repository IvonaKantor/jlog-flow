package com.logging.platform.repository;

import com.logging.platform.entity.LogEntity;
import com.logging.platform.models.LogDataLevel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;

import java.util.Date;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class LogEntityRepository implements PanacheRepository<LogEntity> {

    public PanacheQuery<LogEntity> find(
            final String search,
            final Set<String> serviceIds,
            final Set<String> serviceNames,
            final String hostName,
            final Date fromDate,
            final Date toDate,
            final LogLevel level
    ) {
        final var query = new StringBuilder();
        final var params = new Parameters();



        if (!serviceIds.isEmpty()) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" serviceId in :serviceIds");
            params.and("serviceIds", serviceIds);
        }

        if (!serviceNames.isEmpty()) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" serviceName in :serviceNames");

            params.and("serviceNames", serviceNames);
        }

        if (hostName != null && !hostName.isBlank()) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" hostName = :hostName");

            params.and("hostName", hostName);
        }

        if (fromDate != null) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" timestamp >= :fromDate");

            params.and("fromDate", fromDate);
        }

        if (toDate != null) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" timestamp <= :toDate");

            params.and("toDate", toDate);
        }

        if (level != null) {
            query.append(query.isEmpty() ? "" : " and")
                    .append(" level = :level");

            params.and("level", LogDataLevel.valueOf(level.toString()));
        }

        return find(query.toString(), Sort.by("timestamp", Sort.Direction.Descending), params);
    }

    public List<String> getServiceNames() {
        return getEntityManager()
                .createQuery("select distinct l.serviceName from LogEntity l where l.serviceName is not null and l.serviceName <> '' order by l.serviceName", String.class)
                .getResultList();
    }

    public List<String> getHostNames() {
        return getEntityManager()
                .createQuery("select distinct l.hostName from LogEntity l where l.hostName is not null and l.hostName <> '' order by l.hostName", String.class)
                .getResultList();
    }
}