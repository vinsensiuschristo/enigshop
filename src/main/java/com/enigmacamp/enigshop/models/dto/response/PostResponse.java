package com.enigmacamp.enigshop.models.dto.response;

public record PostResponse(
        Integer userId,
        Integer id,
        String title,
        String body
) {
}