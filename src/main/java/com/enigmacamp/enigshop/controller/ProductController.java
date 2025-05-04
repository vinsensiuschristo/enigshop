package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductCreateRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductUpdateRequest;
import com.enigmacamp.enigshop.models.dto.response.*;
import com.enigmacamp.enigshop.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = APIUrl.PRODUCT_API)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getProducts(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ){
        MetaDataRequest dataRequest = new MetaDataRequest(search, page, size);
        Page<ProductResponse> products = productService.getProducts(dataRequest);

        long totalData = products.getTotalElements();
        long totalPage = totalData % size == 0 ? totalData / size : totalData / size + 1;
        boolean hasNext = products.hasNext();
        boolean hasPrevious = products.hasPrevious();

        MetaDataResponse metaData = new MetaDataResponse(page, size, totalData, totalPage, hasNext, hasPrevious);
        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        products.getContent(),
                        metaData
                ));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String id){
        ProductResponse product = productService.getProductResponseById(id);

        CommonResponse<ProductResponse> body = new CommonResponse<>(
                HttpStatus.OK.value(),
                "Success",
                product,
                null
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(body);
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> addProduct(
            @RequestPart(value = "product", required = false) String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ProductResponse product = null;

        if (productJson != null) {
            try {
                ProductCreateRequest productRequest = objectMapper.readValue(productJson, ProductCreateRequest.class);
                product = productService.createProduct(productRequest, image);
                return ResponseEntity
                        .ok(new CommonResponse<>(
                                HttpStatus.CREATED.value(),
                                "Product created",
                                product,
                                null
                        ));
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(new CommonResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid JSON format for product data",
                        null,
                        null
                ));
            }
        }

        return ResponseEntity.badRequest().body(new CommonResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Product data is required",
                null,
                null
        ));
    }

    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(
            @PathVariable("id") String id,
            @RequestPart(value = "product", required = false) String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {

        ProductUpdateRequest productUpdateRequest = null;

        if (productJson != null) {
            try {
                productUpdateRequest = objectMapper.readValue(productJson, ProductUpdateRequest.class);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(new CommonResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid JSON format for product data",
                        null,
                        null
                ));
            }
        }

        ProductResponse updatedProduct = productService.updateProduct(id, productUpdateRequest, image);

        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Product with id %s updated".formatted(id),
                        updatedProduct,
                        null
                ));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .ok(new ProductDeleteResponse(id, "Product deleted"));
    }
}