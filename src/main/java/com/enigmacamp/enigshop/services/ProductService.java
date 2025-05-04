package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductCreateRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductUpdateRequest;
import com.enigmacamp.enigshop.models.dto.response.ProductResponse;
import com.enigmacamp.enigshop.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest request, MultipartFile image);
    ProductResponse updateProduct(String productId, ProductUpdateRequest updateRequest, MultipartFile image);

    void deleteProduct(String productId);
    ProductResponse getProductResponseById(String productId);
    Page<ProductResponse> getProducts(MetaDataRequest request);
    List<ProductResponse> searchProduct(String name);
    Product getProductById(String productId);
}
