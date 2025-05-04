package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.TransactionRequest;
import com.enigmacamp.enigshop.models.dto.response.CommonResponse;
import com.enigmacamp.enigshop.models.dto.response.MetaDataResponse;
import com.enigmacamp.enigshop.models.dto.response.TransactionResponse;
import com.enigmacamp.enigshop.services.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransactions(
            @RequestBody TransactionRequest transactionRequest){

        TransactionResponse transactionResponse = transactionService.create(transactionRequest);

        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.CREATED.value(),
                        "Transaction Created",
                        transactionResponse,
                        null
                ));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getTransactions(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ){
        MetaDataRequest metaDataRequest = new MetaDataRequest(search, page, size);
        Page<TransactionResponse> transactionResponses = transactionService.getAll(metaDataRequest);

        MetaDataResponse metaDataResponse = new MetaDataResponse(
                page,
                size,
                transactionResponses.getTotalElements(),
                transactionResponses.getTotalPages(),
                transactionResponses.hasNext(),
                transactionResponses.hasPrevious()
        );

        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        transactionResponses.getContent(),
                        metaDataResponse
                ));
    }
}
