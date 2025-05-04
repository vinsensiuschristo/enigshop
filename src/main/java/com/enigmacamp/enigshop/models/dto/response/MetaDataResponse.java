package com.enigmacamp.enigshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetaDataResponse(
        Integer page,
        Integer size,
        long total,
        @JsonProperty("total_pages")
        long totalPages,
        @JsonProperty("has_next")
        boolean hasNext,
        @JsonProperty("has_previous")
        boolean hasPrevious
) { }
