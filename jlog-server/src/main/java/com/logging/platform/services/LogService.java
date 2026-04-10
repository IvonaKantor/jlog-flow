package com.logging.platform.services;

import com.logging.platform.mapper.LogApiMapper;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.openapi.quarkus.openapi_yaml.model.LogLevel;
import org.openapi.quarkus.openapi_yaml.model.PaginationDataLog;

import java.util.Date;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class LogService {

    @Inject
    private LogApiMapper  logApiMapper;

    @Inject
    private LogEntityRepository logEntityRepository;

    public PaginationDataLog getLogs(
            final String search,
            final Set<String> serviceIds,
            final Set<String> serviceNames,
            final String hostName,
            final Date fromDate,
            final Date toDate,
            final LogLevel level,
            final int pageIndex,
            final int pageSize
    ) {
        final var query = logEntityRepository.find(search, serviceIds, serviceNames, hostName, fromDate, toDate, level);
        final var totalCount = query.count();
        final var list = query.page(pageIndex, pageSize).list();
        final var logs = logApiMapper.map(list);

        final var pagination = new PaginationDataLog();

        pagination.setPageSize(pageSize);
        pagination.setPageIndex(pageIndex);
        pagination.setItems(logs);
        pagination.setPageCount(query.pageCount());
        pagination.setTotalCount(totalCount);

        return pagination;
    }

    public List<String> getServiceNames() {
        return logEntityRepository.getServiceNames();
    }

    public List<String> getHostNames() {
        return logEntityRepository.getHostNames();
    }
}