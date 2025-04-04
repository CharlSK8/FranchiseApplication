package com.franchises.develop.controller;

import com.franchises.develop.dto.request.ProductRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import com.franchises.develop.service.IBranchService;
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
class BranchControllerTest {

    @Mock
    private IBranchService branchService;

    @InjectMocks
    private BranchController branchController;

    private String branchId;
    private String productId;
    private int newStock;
    private ProductRequestDTO productRequest;
    private ResponseDTO<Branch> responseDTO;
    private ResponseDTO<Product> responseProductDTO;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        branchId = "123";
        productId = "456";
        newStock = 10;
        product = new Product();
        product.setId(productId);
        productRequest = new ProductRequestDTO();
        productRequest.setName("Test Product");
        productRequest.setStock(10);
        branch = new Branch();
        branch.setId(branchId);
        responseDTO = new ResponseDTO<>();
        responseDTO.setCode(HttpStatus.OK.value());
        responseDTO.setResponse(branch);
        responseProductDTO = new ResponseDTO<>();
        responseProductDTO.setCode(HttpStatus.OK.value());
        responseProductDTO.setResponse(product);
    }

    @Test
    void addProductToBranch_success() {
        when(branchService.addProductToBranch(branchId, productRequest)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<ResponseDTO<Branch>>> result = branchController.addProductToBranch(branchId, productRequest);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok(responseDTO))
                .verifyComplete();

        verify(branchService, times(1)).addProductToBranch(branchId, productRequest);
    }

    @Test
    void addProductToBranch_serviceError() {
        ResponseDTO<Branch> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(branchService.addProductToBranch(branchId, productRequest)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<Branch>>> result = branchController.addProductToBranch(branchId, productRequest);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(branchService, times(1)).addProductToBranch(branchId, productRequest);
    }

    @Test
    void addProductToBranch_serviceEmitsError() {
        when(branchService.addProductToBranch(branchId, productRequest)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<Branch>>> result = branchController.addProductToBranch(branchId, productRequest);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(branchService, times(1)).addProductToBranch(branchId, productRequest);
    }

    @Test
    void removeProductFromBranch_success() {
        when(branchService.removeProductFromBranch(branchId, productId)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<ResponseDTO<Branch>>> result = branchController.removeProductFromBranch(branchId, productId);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok(responseDTO))
                .verifyComplete();

        verify(branchService, times(1)).removeProductFromBranch(branchId, productId);
    }

    @Test
    void removeProductFromBranch_serviceError() {
        ResponseDTO<Branch> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(branchService.removeProductFromBranch(branchId, productId)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<Branch>>> result = branchController.removeProductFromBranch(branchId, productId);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(branchService, times(1)).removeProductFromBranch(branchId, productId);
    }

    @Test
    void removeProductFromBranch_serviceEmitsError() {
        when(branchService.removeProductFromBranch(branchId, productId)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<Branch>>> result = branchController.removeProductFromBranch(branchId, productId);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(branchService, times(1)).removeProductFromBranch(branchId, productId);
    }

    @Test
    void updateProductStock_success() {
        when(branchService.updateProductStock(branchId, productId, newStock)).thenReturn(Mono.just(responseProductDTO));

        Mono<ResponseEntity<ResponseDTO<Product>>> result = branchController.updateProductStock(branchId, productId, newStock);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok(responseProductDTO))
                .verifyComplete();

        verify(branchService, times(1)).updateProductStock(branchId, productId, newStock);
    }

    @Test
    void updateProductStock_serviceError() {
        ResponseDTO<Product> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(branchService.updateProductStock(branchId, productId, newStock)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<Product>>> result = branchController.updateProductStock(branchId, productId, newStock);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(branchService, times(1)).updateProductStock(branchId, productId, newStock);
    }

    @Test
    void updateProductStock_serviceEmitsError() {
        when(branchService.updateProductStock(branchId, productId, newStock)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<Product>>> result = branchController.updateProductStock(branchId, productId, newStock);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(branchService, times(1)).updateProductStock(branchId, productId, newStock);
    }
}