package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.TransactionRequest;
import com.enigmacamp.enigshop.models.dto.response.TransactionDetailResponse;
import com.enigmacamp.enigshop.models.dto.response.TransactionResponse;
import com.enigmacamp.enigshop.models.entities.Customer;
import com.enigmacamp.enigshop.models.entities.Product;
import com.enigmacamp.enigshop.models.entities.Transaction;
import com.enigmacamp.enigshop.models.entities.TransactionDetail;
import com.enigmacamp.enigshop.repositories.TransactionDetailRepository;
import com.enigmacamp.enigshop.repositories.TransactionRepository;
import com.enigmacamp.enigshop.services.CustomerService;
import com.enigmacamp.enigshop.services.ProductService;
import com.enigmacamp.enigshop.services.TransactionService;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest transactionRequest) {
        Customer customer = customerService.getCustomerById(transactionRequest.customerId());
        LocalDateTime currentTime = LocalDateTime.now();

        //create transaction
        Transaction transaction = new Transaction(customer, currentTime);
        Transaction resultTransaction = transactionRepository.save(transaction);

        List<TransactionDetail> transactionDetails = transactionRequest.transactionDetailsRequest()
                .stream().map(
                detailRequest -> {
                    Product product = productService.getProductById(detailRequest.productId());

                    // TODO: Make sure Quantity request less or equal than stock product
                    if (product.getStock() <= detailRequest.quantity()){
                        throw new RequestValidationException("Stock Product %s is not enough".formatted(product.getName()));
                    }

                    TransactionDetail transactionDetail = new TransactionDetail(
                            resultTransaction,
                            product,
                            detailRequest.quantity(),
                            product.getPrice()
                    );
                    transactionDetailRepository.save(transactionDetail);

                    // TODO: Update stock Product in Entity
                    Integer stock = product.getStock() - detailRequest.quantity();
                    product.setStock(stock);
//                    // TODO: Save updated product to DB,
                      // ini tidak perlu ternya ketika kita melakukan setStock itu sudah merubah yang ada di database
//                    ProductUpdateRequest updateRequestProduct = new ProductUpdateRequest(
//                                    null,
//                                    null,
//                                    null,
//                                    stock);
//                    productService.updateProduct(product.getId(), updateRequestProduct);
                    return transactionDetail;
                }).toList();

        Long totalPayment = transactionDetails.stream()
                .mapToLong(transactionDetail ->
                        transactionDetail.getPrice() * transactionDetail.getQuantity()).sum();

        List<TransactionDetailResponse> transactionDetailResponseList =
                transactionDetails.stream()
                        .map(ConvertType::transactionDetailToTransactionDetailResponse)
                        .toList();

        return new TransactionResponse(
                resultTransaction.getId(),
                ConvertType.customerToCustomerResponse(customer),
                resultTransaction.getDate(),
                transactionDetailResponseList,
                totalPayment
        );
    }

    @Override
    public Page<TransactionResponse> getAll(MetaDataRequest metaDataRequest){

        Pageable pageable = Pageable.ofSize(metaDataRequest.size()).withPage(metaDataRequest.page()-1);

        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        List<TransactionDetail> transactionDetails = transactionDetailRepository.findAll();

        List<TransactionResponse> transactionResponses = transactions.getContent().stream()
                .map(transaction -> {
                    List<TransactionDetailResponse> transactionDetailResponses = transactionDetails.stream()
                            .filter(transactionDetail -> transactionDetail.getTransaction().getId().equals(transaction.getId()))
                            .map(ConvertType::transactionDetailToTransactionDetailResponse)
                            .toList();

                    Long totalPayment = transactionDetailResponses.stream()
                            .mapToLong(detailResponse ->
                                    detailResponse.productPrice() * detailResponse.quantity()).sum();

                    return new TransactionResponse(
                            transaction.getId(),
                            ConvertType.customerToCustomerResponse(transaction.getCustomer()),
                            transaction.getDate(),
                            transactionDetailResponses,
                            totalPayment
                    );
                }).toList();

        return new PageImpl<>(transactionResponses, pageable, transactions.getTotalElements());
    }

    @Override
    public Transaction getById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found", new RuntimeException("Transaction with id %s not found".formatted(transactionId))));
    }

    @Override
    public TransactionResponse getResponseById(String transactionId) {
        Transaction transaction = getById(transactionId);
        List<TransactionDetail> transactionDetails = transactionDetailRepository.findTransactionDetailsByTransaction_Id(transaction.getId());

        List<TransactionDetailResponse> transactionDetailResponses = transactionDetails.stream()
                .map(ConvertType::transactionDetailToTransactionDetailResponse)
                .toList();

        Long totalPayment = transactionDetailResponses.stream()
                .mapToLong(detailResponse -> detailResponse.productPrice() * detailResponse.quantity())
                .sum();

        return new TransactionResponse(
                transaction.getId(),
                ConvertType.customerToCustomerResponse(transaction.getCustomer()),
                transaction.getDate(),
                transactionDetailResponses,
                totalPayment
        );
    }
}
