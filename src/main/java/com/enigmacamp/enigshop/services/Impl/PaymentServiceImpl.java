package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.models.dto.request.ItemDetailsRequest;
import com.enigmacamp.enigshop.models.dto.request.PaymentDetailRequest;
import com.enigmacamp.enigshop.models.dto.request.PaymentRequest;
import com.enigmacamp.enigshop.models.dto.response.PaymentStatusResponse;
import com.enigmacamp.enigshop.models.dto.response.TransactionResponse;
import com.enigmacamp.enigshop.models.entities.Payment;
import com.enigmacamp.enigshop.models.entities.Transaction;
import com.enigmacamp.enigshop.models.enums.TransactionStatus;
import com.enigmacamp.enigshop.repositories.PaymentRepository;
import com.enigmacamp.enigshop.services.PaymentService;
import com.enigmacamp.enigshop.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TransactionService transactionService;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String TRANSACTIONS_URL;
    private final String BASE_URL;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            TransactionService transactionService,
            RestClient restClient,
            @Value("${payment.transactions-url}") String TRANSACTIONS_URL,
            @Value("${payment.base-url}") String BASE_URL,
            @Value("${payment.secret-key}") String SECRET_KEY
    ) {
        this.paymentRepository = paymentRepository;
        this.transactionService = transactionService;
        this.restClient = restClient;
        this.TRANSACTIONS_URL = TRANSACTIONS_URL;
        this.BASE_URL = BASE_URL;
        this.SECRET_KEY = SECRET_KEY;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(String transactionId) {
        // TODO : get amount from transaction details
        Transaction transaction = transactionService.getById(transactionId);
        TransactionResponse transactionResponse = transactionService.getResponseById(transactionId);

        List<ItemDetailsRequest> itemDetails = transactionResponse.transactionDetailResponses().stream()
                .map(transactionDetailResponse -> {
                    return new ItemDetailsRequest(
                            transactionDetailResponse.productResponse().id(),
                            transactionDetailResponse.productResponse().price(),
                            transactionDetailResponse.quantity(),
                            transactionDetailResponse.productResponse().name()
                    );
                }).toList();

        //TODO: Create Payment request
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentDetail(
                        PaymentDetailRequest.builder()
                                .orderId("EnigShop-" + transactionResponse.id())
                                .grossAmount(transactionResponse.totalPayment())
                                .build()
                )
                .itemDetails(itemDetails)
                .build();

        //TODO: Payment request
        ResponseEntity<Map<String, String>> responseEntity = restClient.post()
                .uri(TRANSACTIONS_URL + "/transactions")
                .body(paymentRequest)
                .header("Authorization", "Basic " + SECRET_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String, String> response = responseEntity.getBody();

        Payment payment = Payment.builder()
                .token(response.get("token"))
                .redirectUrl(response.get("redirect_url"))
                .transaction(transaction)
                .transactionStatus(TransactionStatus.ORDERED)
                .build();

        return paymentRepository.saveAndFlush(payment);
    }

    @Scheduled(fixedRate = 60000)
    public void processPaymentOrdered() {
        List<Payment> payments = paymentRepository.findPaymentsByTransactionStatus(TransactionStatus.ORDERED);
        if (payments.isEmpty()) return;
        log.info("Processing payment for {} transactions", payments.size());

        for (Payment payment : payments) {
            try {
                Transaction transaction = payment.getTransaction();
                String orderId = "EnigShop-" + transaction.getId();
                String urlTransactionStatus = BASE_URL + "/" + orderId + "/status";

                log.info("Checking transaction status for order: {}", orderId);

                ResponseEntity<PaymentStatusResponse> response = restClient.get()
                        .uri(urlTransactionStatus)
                        .header("Accept", "application/json")
                        .header("Authorization", "Basic " + SECRET_KEY )
                        .retrieve()
                        .toEntity(new ParameterizedTypeReference<>() {
                        });

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    log.info("Processing payment for order: {}", orderId);
                    String status = response.getBody().transaction_status();
                    payment.setTransactionStatus(TransactionStatus.findByName(status));
                    paymentRepository.saveAndFlush(payment);
                } else {
                    System.err.println("Failed to get transaction status for order: " + orderId);
                }

            } catch (Exception e) {
                System.err.println("Error processing payment for order: " + payment.getTransaction().getId());
            }
        }
    }
}
