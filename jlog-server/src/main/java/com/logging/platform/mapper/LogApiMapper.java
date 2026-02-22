package com.logging.platform.mapper;

import com.logging.platform.entity.LogEntity;
import org.mapstruct.Mapper;
import org.openapi.quarkus.openapi_yaml.model.Log;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static org.mapstruct.MappingConstants.ComponentModel.CDI;

@Mapper(componentModel = CDI)
public abstract class LogApiMapper {

    public abstract List<Log> map(List<LogEntity> source);

    protected OffsetDateTime map(Date source) {
        if (source == null) {
            return null;
        }

        return OffsetDateTime.ofInstant(source.toInstant(), UTC);
    }

}
