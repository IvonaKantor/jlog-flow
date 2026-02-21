package com.logging.platform.mapper;

import com.logging.platform.entity.LogEntity;
import com.logging.platform.models.LogData;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.CDI;

@Mapper(componentModel = CDI)
public abstract class LogDataMapper {

    public abstract LogEntity map(LogData source);
}