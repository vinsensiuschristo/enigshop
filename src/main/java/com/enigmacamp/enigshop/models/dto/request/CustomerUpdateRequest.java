package com.enigmacamp.enigshop.models.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CustomerUpdateRequest(
        String fullName,
        String phoneNumber,
        String address,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate
) {}
