package com.enigmacamp.enigshop.models.dto.response;

public record PaymentStatusResponse(
        String status_code,
        String status_message,
        String transaction_id,
        String masked_card,
        String order_id,
        String payment_type,
        String transaction_time,
        String transaction_status,
        String fraud_status,
        String approval_code,
        String signature_key,
        String bank,
        String gross_amount,
        String channel_response_code,
        String channel_response_message,
        String card_type,
        String payment_option_type,
        String shopeepay_reference_number,
        String reference_id
) {
}
