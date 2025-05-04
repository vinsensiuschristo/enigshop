package com.enigmacamp.enigshop.models.dto.request;

public record PostRequest(
        String title,
        String body,
        String userId
) {
}
