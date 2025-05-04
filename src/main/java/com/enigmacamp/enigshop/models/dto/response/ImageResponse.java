package com.enigmacamp.enigshop.models.dto.response;

public record ImageResponse(
        String id,
        String name,
        String path,
        long size,
        String contentType
) { }
