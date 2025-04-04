package com.franchises.develop.service.impl;

import com.franchises.develop.dto.request.ProductRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.BranchRepository;
import com.franchises.develop.repository.ProductRepository;
import com.franchises.develop.util.Constants;
import com.franchises.develop.util.ErrorHandlerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ErrorHandlerUtils errorHandlerUtils;

    @InjectMocks
    private BranchServiceImpl branchService;

    private String branchId;
    private String productId;
    private ProductRequestDTO productRequest;
    private Branch branch;
    private Product product;
    private int newStock;

    @BeforeEach
    void setUp() {

        branchId = "123";
        productId = "456";
        newStock = 10;

        productRequest = new ProductRequestDTO();
        productRequest.setName("Test Product");
        productRequest.setStock(10);

        product = Product.builder()
                .id(productId)
                .name(productRequest.getName())
                .stock(productRequest.getStock())
                .build();

        branch = new Branch();
        branch.setId(branchId);
        branch.setProductsId(List.of(product.getId()));
    }

//    @Test
//    void addProductToBranch_success_newProduct() {
//        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
//        when(productRepository.findByNameIgnoreCase(productRequest.getName())).thenReturn(Mono.empty());
//        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
//        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));
//
//        Mono<ResponseDTO<Branch>> result = branchService.addProductToBranch(branchId, productRequest);
//
//        StepVerifier.create(result)
//                .expectNextMatches(response ->
//                        response.getCode() == HttpStatus.CREATED.value() &&
//                                response.getMessage().equals(Constants.PRODUCT_CREATED_SUCCESSFULLY) &&
//                                response.getResponse().getProductsId().contains(product.getId()))
//                .verifyComplete();
//
//        verify(branchRepository, times(1)).findById(branchId);
//        verify(productRepository, times(1)).findByNameIgnoreCase(productRequest.getName());
//        verify(productRepository, times(1)).save(any(Product.class));
//        verify(branchRepository, times(1)).save(any(Branch.class));
//    }

    @Test
    void addProductToBranch_BranchNotFound() {

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.addProductToBranch(branchId, productRequest))
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.NOT_FOUND.value() &&
                                response.getMessage().contains(branchId)
                )
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verifyNoInteractions(productRepository);
    }


    @Test
    void removeProductFromBranch_ProductNotFound_ReturnsNotFound() {

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));

        StepVerifier.create(branchService.removeProductFromBranch(branchId, "nonExistentProductId"))
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.NOT_FOUND.value() &&
                                response.getMessage().equals("No product found with id nonExistentProductId. Please verify the ID and try again.")
                )
                .verifyComplete();

        verify(branchRepository).findById(branchId);
    }
    @Test
    void updateProductStock_success() {
        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(productRepository.findById(productId)).thenReturn(Mono.just(product));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<ResponseDTO<Product>> result = branchService.updateProductStock(branchId, productId, newStock);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.OK.value() &&
                                response.getMessage().equals(Constants.PRODUCT_STOCK_UPDATED_SUCCESSFULLY) &&
                                response.getResponse().getStock() == newStock)
                .verifyComplete();

        verify(branchRepository, times(1)).findById(branchId);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }


}