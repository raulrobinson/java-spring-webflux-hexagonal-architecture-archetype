package com.raulbolivarnavas.ms.api.mappers;

import com.raulbolivarnavas.ms.api.dto.ApiMockDto;
import com.raulbolivarnavas.ms.api.dto.ApiMockRuntimeDto;
import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import com.raulbolivarnavas.ms.model.ApiMockRuntimeInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApiMocksDtoMapper {

    ApiMockDto toDto(ApiMockDefinition definition);

    List<ApiMockDto> toDtoList(List<ApiMockDefinition> definitions);

    ApiMockDefinition toDomain(ApiMockDto dto);

    ApiMockRuntimeDto toDto(ApiMockRuntimeInfo info);
}

