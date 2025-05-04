package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductCreateRequest;
import com.enigmacamp.enigshop.models.dto.request.ProductUpdateRequest;
import com.enigmacamp.enigshop.models.dto.response.ProductResponse;
import com.enigmacamp.enigshop.models.entities.Product;
import com.enigmacamp.enigshop.repositories.ProductRepository;
import com.enigmacamp.enigshop.services.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl underTest;

    ProductCreateRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = new ProductCreateRequest(
                "Product Name",
                "Product Description",
                1000L,
                10
        );
    }

    @Test
    @DisplayName("Create Product Success")
    void successWhenCreateProduct () {
        //Arrange
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        underTest.createProduct(productRequest, null);

        //Assert
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct.getName()).isEqualTo(productRequest.name());
    }

    @Test
    @DisplayName("Create Product Fail")
    void failWhenCreateProduct () {
        //Arrange
        when(productRepository.save(Mockito.any(Product.class))).thenThrow(new DataIntegrityViolationException("Failed to save product"));

        //Act
        //Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            underTest.createProduct(productRequest, null);
        });
    }

    @Test
    @DisplayName("Update Product Success")
    void successWhenUpdatedProduct () {
        //Arrange
        Product product = Product.builder()
                .id("1")
                .name("Product Name")
                .description("Product Description")
                .price(1000L)
                .stock(10)
                .build();
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(product));

        //Act
        ProductResponse productResponse = underTest.updateProduct("1", new ProductUpdateRequest(
                product.getName() + " Updated",
                product.getDescription() + " Updated",
                2000L,
                20
        ), null);

        //Assert
        assertThat(productResponse.name()).isEqualTo(product.getName());
        assertThat(productResponse.description()).isEqualTo(product.getDescription());
        assertThat(productResponse.price()).isEqualTo(2000L);
        assertThat(productResponse.stock()).isEqualTo(20);
    }

    @Test
    @DisplayName("Update Product Fail - Product Not Found")
    void willThrowExceptionWhenUpdateProductNotFound() {
        //Arrange
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.empty());

        //Act
        //Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            underTest.updateProduct("1", new ProductUpdateRequest(
                    "Product Name Updated",
                    "Product Description Updated",
                    2000L,
                    20
            ), null);
        });
    }

    @Test
    void canDeleteProduct() {
        //Arrange
        Product product = Product.builder()
                .id("1")
                .name("Product Name")
                .description("Product Description")
                .price(1000L)
                .stock(10)
                .build();
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(product));

        //Act
        underTest.deleteProduct("1");

        //Assert
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Delete Product Fail - Product Not Found")
    void willThrowExceptionWhenDeleteProductNotFound() {
        //Arrange
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.empty());

        //Act
        //Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            underTest.deleteProduct("1");
        });
    }

    @Test
    @DisplayName("Get Product Response By Id - Success")
    void canGetProductResponseById() {
        //Arrange
        Product product = Product.builder()
                .id("1")
                .name("Product Name")
                .description("Product Description")
                .price(1000L)
                .stock(10)
                .build();
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(product));

        //Act
        ProductResponse productResponse = underTest.getProductResponseById("1");

        //Assert
        assertThat(productResponse.name()).isEqualTo(product.getName());
        assertThat(productResponse.description()).isEqualTo(product.getDescription());
        assertThat(productResponse.price()).isEqualTo(product.getPrice());
        assertThat(productResponse.stock()).isEqualTo(product.getStock());
    }

    @Test
    @DisplayName("Get Product Response By Id - Fail")
    void willThrowExceptionWhenGetProductResponseByIdNotFound() {
        //Arrange
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.empty());

        //Act
        //Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            underTest.getProductResponseById("1");
        });
    }

    @Test
    void canGetAllProducts() {
        //Arrange
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .id("1")
                .name("Product 1")
                .description("Product 1 Description")
                .price(1000L)
                .stock(10)
                .build());
        products.add(Product.builder()
                .id("2")
                .name("Product 2")
                .description("Product 2 Description")
                .price(2000L)
                .stock(20)
                .build());
        MetaDataRequest metaDataRequest = new MetaDataRequest(null, 1, 10);
        Pageable pageable = PageRequest.of(metaDataRequest.page() - 1, metaDataRequest.size());
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        //Act
        Page<ProductResponse> productResponses = underTest.getProducts(metaDataRequest);

        //Assert
        assertFalse(productResponses.isEmpty());
        assertEquals(2, productResponses.getNumberOfElements());
    }

    @Test
    @DisplayName("Get All Products - Fail when Page and Size is less than 0")
    void willThrowExceptionWhenGetAllProductsPageAndSizeIsLessThan0() {
        //Arrange
        MetaDataRequest metaDataRequest = new MetaDataRequest(null, 0, 0);

        //Act
        //Assert
        assertThrows(RequestValidationException.class, () -> {
            underTest.getProducts(metaDataRequest);
        });
    }

    @Test
    @DisplayName("Search Product - Success")
    void canSearchProduct() {
        //Arrange
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .id("1")
                .name("Product 1")
                .description("Product 1 Description")
                .price(1000L)
                .stock(10)
                .build());
        products.add(Product.builder()
                .id("2")
                .name("Product 2")
                .description("Product 2 Description")
                .price(2000L)
                .stock(20)
                .build());
        when(productRepository.findByNameContainingIgnoreCase(Mockito.anyString())).thenReturn(products);

        //Act
        List<ProductResponse> productResponses = underTest.searchProduct("Product");

        //Assert
        assertFalse(productResponses.isEmpty());
        assertEquals(2, productResponses.size());
    }

    @Test
    @DisplayName("Search Product - Fail")
    void willThrowExceptionWhenSearchProductNotFound() {
        //Arrange
        when(productRepository.findByNameContainingIgnoreCase(Mockito.anyString())).thenReturn(new ArrayList<>());

        //Act
        //Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            underTest.searchProduct("Product");
        });
    }


    @Test
    @DisplayName("Get Product By Id - Success")
    void canGetProductById() {
        //Arrange
        Product product = Product.builder()
                .id("1")
                .name("Product Name")
                .description("Product Description")
                .price(1000L)
                .stock(10)
                .build();
        when(productRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(product));

        //Act
        Product productResponse = underTest.getProductById("1");

        //Assert
        assertThat(productResponse.getName()).isEqualTo(product.getName());
        assertThat(productResponse.getDescription()).isEqualTo(product.getDescription());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
        assertThat(productResponse.getStock()).isEqualTo(product.getStock());
    }
}