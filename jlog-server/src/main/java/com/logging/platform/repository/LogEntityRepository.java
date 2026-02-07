package com.logging.platform.repository;

import com.logging.platform.entity.LogEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

@ApplicationScoped
public class LogEntityRepository implements PanacheRepository<LogEntity> {

    public PanacheQuery<LogEntity> find(final Set<String> serviceIds) {
        return find("serviceId in ?1", serviceIds);
    }

}