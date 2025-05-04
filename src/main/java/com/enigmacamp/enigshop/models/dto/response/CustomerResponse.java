package com.enigmacamp.enigshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CustomerResponse(
    String id,
    String fullName,
    @JsonAlias("phone_number")
    String phoneNumber,
    String address,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthDate
) {}
