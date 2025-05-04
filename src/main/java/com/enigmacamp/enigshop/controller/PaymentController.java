package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.PaymentCreateRequest;
import com.enigmacamp.enigshop.models.dto.request.PaymentRequest;
import com.enigmacamp.enigshop.models.dto.response.CommonResponse;
import com.enigmacamp.enigshop.models.dto.response.PaymentResponse;
import com.enigmacamp.enigshop.models.entities.Payment;
import com.enigmacamp.enigshop.services.PaymentService;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PAYMENT)
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public ResponseEntity<CommonResponse<PaymentResponse>> createPayment(@RequestBody PaymentCreateRequest request) {
        Payment payment = paymentService.createPayment(request.transactionId());
        PaymentResponse paymentResponse = ConvertType.paymentToPaymentResponse(payment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        HttpStatus.CREATED.value(),
                        "Success",
                        paymentResponse,
                        null
                ));
    }
}
