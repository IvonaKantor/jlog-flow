package com.logging.platform.mapper;

import com.logging.platform.entity.LogEntity;
import org.mapstruct.Mapper;
import org.openapi.quarkus.openapi_yaml.model.LogData;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Mapper(componentModel = "cdi")
public abstract class LogApiMapper {

    public abstract List<LogData> map(List<LogEntity> source);

    protected abstract LogData map(LogEntity source);

    protected OffsetDateTime map(Date source) {
        if (source == null) {
            return null;
        }

        return OffsetDateTime.ofInstant(source.toInstant(), UTC);
    }

}
