package com.logging.platform.mapper;

import com.logging.platform.entity.LogEntity;
import com.logging.platform.models.LogData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LogDataMapper {

    LogEntity map(LogData source);
}