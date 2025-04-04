package com.franchises.develop.controller;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Product;
import com.franchises.develop.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    private String productId;
    private String newName;
    private ResponseDTO<Product> responseDTO;

    @BeforeEach
    void setUp() {
        productId = "123";
        newName = "Updated Product Name";

        Product product = new Product();
        product.setId(productId);
        product.setName(newName);

        responseDTO = new ResponseDTO<>();
        responseDTO.setCode(HttpStatus.OK.value());
        responseDTO.setResponse(product);
    }

    @Test
    void updateProductName_success() {
        when(productService.updateProductName(productId, newName)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<ResponseDTO<Product>>> result = productController.updateProductName(productId, newName);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok(responseDTO))
                .verifyComplete();

        verify(productService, times(1)).updateProductName(productId, newName);
    }

    @Test
    void updateProductName_serviceError() {
        ResponseDTO<Product> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(productService.updateProductName(productId, newName)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<Product>>> result = productController.updateProductName(productId, newName);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(productService, times(1)).updateProductName(productId, newName);
    }

    @Test
    void updateProductName_serviceEmitsError() {
        when(productService.updateProductName(productId, newName)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<Product>>> result = productController.updateProductName(productId, newName);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(productService, times(1)).updateProductName(productId, newName);
    }
}