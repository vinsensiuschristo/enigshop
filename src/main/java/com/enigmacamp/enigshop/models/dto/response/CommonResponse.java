package com.enigmacamp.enigshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record CommonResponse<T>(
        Integer statusCode,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        MetaDataResponse metaData
) {}
