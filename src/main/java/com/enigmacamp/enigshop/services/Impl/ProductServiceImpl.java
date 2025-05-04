package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.constants.ProductColumn;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductCreateRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductUpdateRequest;
import com.enigmacamp.enigshop.models.dto.response.ProductResponse;
import com.enigmacamp.enigshop.models.entities.Image;
import com.enigmacamp.enigshop.models.entities.Product;
import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.repositories.ProductRepository;
import com.enigmacamp.enigshop.services.ImageService;
import com.enigmacamp.enigshop.services.ProductService;
import com.enigmacamp.enigshop.utils.specifications.GlobalSpecification;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Override
    public ProductResponse createProduct(ProductCreateRequest request, MultipartFile image) {
        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stock()
        );
        if (image != null) {
            Image savedImage = imageService.saveImage(image, "product");
            product.setImage(savedImage);
        }
        productRepository.save(product);
        return ConvertType.productToProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(String productId, ProductUpdateRequest updateRequest, MultipartFile image) {
        Product product = findByIdOrThrow(productId);

        boolean changes = false;
        if (updateRequest.name() != null && !updateRequest.name().equals(product.getName())){
            product.setName(updateRequest.name());
            changes = true;
        }
        if (updateRequest.price() != null && !updateRequest.price().equals(product.getPrice())){
            product.setPrice(updateRequest.price());
            changes = true;
        }
        if (updateRequest.stock() != null && !updateRequest.stock().equals(product.getStock())){
            product.setStock(updateRequest.stock());
            changes = true;
        }
        if (image != null){
            Image savedImage = imageService.saveImage(image, "product");
            product.setImage(savedImage);
            changes = true;
        }
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        productRepository.save(product);
        return ConvertType.productToProductResponse(product);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = findByIdOrThrow(productId);
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductResponseById(String productId) {
        Product product = findByIdOrThrow(productId);
        return ConvertType.productToProductResponse(product);
    }

    private Product findByIdOrThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product with id %s not found".formatted(productId)));
    }

    @Override
    public Page<ProductResponse> getProducts(MetaDataRequest request) {
        if (request.page() < 1) {
            throw new RequestValidationException("page must be greater than 0");
        }
        if (request.size() < 1) {
            throw new RequestValidationException("size must be greater than 0");
        }

        Pageable pageable = PageRequest.of(request.page() - 1, request.size());

        Specification<Product> spec = buildSpecification(request);
        Page<Product> products = spec == null
                ? productRepository.findAll(pageable)
                : productRepository.findAll(spec, pageable);

        if(products.isEmpty()){
            throw new ResourceNotFoundException("Product not found");
        }

        return products.map(ConvertType::productToProductResponse);
    }

    private Specification<Product> buildSpecification(MetaDataRequest request){
        if (request.query() != null && !request.query().isEmpty()) {
            return Specification
                    .where(GlobalSpecification
                            .likeIgnoreCase(ProductColumn.NAME.getColumn(), request.query()));
        }
        return null;
    }

    @Override
    public List<ProductResponse> searchProduct(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return products.stream()
                .map(ConvertType::productToProductResponse)
                .toList();
    }

    @Override
    public Product getProductById(String productId) {
        return findByIdOrThrow(productId);
    }
}
