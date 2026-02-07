package com.logging.platform.services;

import com.logging.platform.mapper.LogApiMapper;
import com.logging.platform.pagination.Pagination;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.openapi.quarkus.openapi_yaml.model.LogData;

import java.util.Set;

@ApplicationScoped
public class LogService {

    @Inject
    private LogApiMapper  logApiMapper;

    @Inject
    private LogEntityRepository logEntityRepository;

    public Pagination<LogData> getLogs(final Set<String> serviceIds, int pageIndex, int pageSize) {
        final var query = logEntityRepository.find(serviceIds);
        final var logs = logApiMapper.map(query.page(pageIndex, pageSize).list());

        final var pagination = new Pagination<LogData>();

        pagination.setPageSize(pageSize);
        pagination.setPageIndex(pageIndex);
        pagination.setItems(logs);
        pagination.setPageCount(query.pageCount());

        return pagination;
    }
}
