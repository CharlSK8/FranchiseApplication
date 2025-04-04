package com.franchises.develop.service.impl;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.ProductRepository;
import com.franchises.develop.util.Constants;
import com.franchises.develop.util.ErrorHandlerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private String productId;
    private String newName;
    private Product product;

    @BeforeEach
    void setUp() {
        productId = "123";
        newName = "New Product Name";

        product = new Product();
        product.setId(productId);
        product.setName("Old Product Name");
    }

    @Test
    void updateProductName_success() {
        when(productRepository.findById(productId)).thenReturn(Mono.just(product));
        when(productRepository.findByNameIgnoreCase(newName)).thenReturn(Mono.empty());
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<ResponseDTO<Product>> result = productService.updateProductName(productId, newName);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.OK.value() &&
                                response.getMessage().equals(Constants.PRODUCT_NAME_UPDATED_SUCCESSFULLY) &&
                                response.getResponse().getName().equals(newName))
                .verifyComplete();

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findByNameIgnoreCase(newName);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProductName_serviceError() {
        when(productRepository.findById(productId)).thenReturn(Mono.just(product));
        when(productRepository.findByNameIgnoreCase(newName)).thenReturn(Mono.empty());
        when(productRepository.save(any(Product.class))).thenReturn(Mono.error(new RuntimeException("Test Error")));

        try (MockedStatic<ErrorHandlerUtils> mockedStatic = mockStatic(ErrorHandlerUtils.class)) {
            mockedStatic.when(() -> ErrorHandlerUtils.handleError(any(Throwable.class))).thenAnswer(invocation -> {
                return Mono.just(new ResponseDTO<>());
            });

            Mono<ResponseDTO<Product>> result = productService.updateProductName(productId, newName);

            StepVerifier.create(result)
                    .expectNext(new ResponseDTO<>())
                    .verifyComplete();

            verify(productRepository, times(1)).findById(productId);
            verify(productRepository, times(1)).findByNameIgnoreCase(newName);
            verify(productRepository, times(1)).save(any(Product.class));
        }
    }
}