package com.enigmacamp.enigshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentResponse(
        String token,
        @JsonProperty("redirect_url")
        String redirectUrl) {
}
