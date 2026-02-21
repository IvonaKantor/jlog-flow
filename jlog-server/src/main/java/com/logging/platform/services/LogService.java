package com.logging.platform.services;

import com.logging.platform.mapper.LogApiMapper;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

import java.util.Set;

@ApplicationScoped
public class LogService {

    @Inject
    private LogApiMapper  logApiMapper;

    @Inject
    private LogEntityRepository logEntityRepository;

    public PaginationDataLog getLogs(
            final Set<String> serviceIds,
            final Set<String> serviceNames,
            final LogLevel level,
            final int pageIndex,
            final int pageSize
    ) {
        final var query = logEntityRepository.find(serviceIds, serviceNames, level);
        final var logs = logApiMapper.map(query.page(pageIndex, pageSize).list());

        final var pagination = new PaginationDataLog();

        pagination.setPageSize(pageSize);
        pagination.setPageIndex(pageIndex);
        pagination.setItems(logs);
        pagination.setPageCount(query.pageCount());

        return pagination;
    }
}