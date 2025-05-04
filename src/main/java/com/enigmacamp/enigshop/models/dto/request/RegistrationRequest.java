package com.enigmacamp.enigshop.models.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NonNull;

import java.time.LocalDate;

public record RegistrationRequest(
        @NonNull
        String username,
        @NonNull
        String password,
        String email,
        String fullName,
        @JsonAlias("phone_number")
        String phoneNumber,
        String address,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate
) {}
