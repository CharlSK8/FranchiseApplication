package com.franchises.develop.controller;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.dto.request.BranchUpdateNameRequestDTO;
import com.franchises.develop.dto.request.FranchiseUpdateNameRequestDTO;
import com.franchises.develop.dto.response.ProductWithBranchDTO;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Franchise;
import com.franchises.develop.service.IFranchiseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseControllerTest {

    @Mock
    private IFranchiseService franchiseService;

    @InjectMocks
    private FranchiseController franchiseController;

    private String franchiseId;
    private FranchiseUpdateNameRequestDTO franchiseUpdateNameRequestDTO;
    private FranchiseRequestDTO franchiseRequestDTO;
    private ResponseDTO<Franchise> responseDTO;
    private ResponseDTO<List<ProductWithBranchDTO>> responseProductDTO;
    private BranchDTO branchRequestDTO;
    private BranchUpdateNameRequestDTO branchUpdateNameRequestDTO;

    @BeforeEach
    void setUp() {
        franchiseId = "123";

        franchiseRequestDTO = new FranchiseRequestDTO();
        franchiseRequestDTO.setName("Test Franchise");

        Franchise franchise = new Franchise();
        franchise.setName("Test Franchise");

        ProductWithBranchDTO productWithBranchDTO = new ProductWithBranchDTO();
        productWithBranchDTO.setBranchId("123");
        productWithBranchDTO.setProduct(Product.builder().build());

        responseProductDTO = new ResponseDTO<>();
        responseProductDTO.setCode(HttpStatus.CREATED.value());
        responseProductDTO.setResponse(List.of(productWithBranchDTO));

        responseDTO = new ResponseDTO<>();
        responseDTO.setCode(HttpStatus.CREATED.value());
        responseDTO.setResponse(franchise);

        franchiseUpdateNameRequestDTO = new FranchiseUpdateNameRequestDTO();
        franchiseUpdateNameRequestDTO.setId("123");

        branchRequestDTO = new BranchDTO();
        branchRequestDTO.setName("Test Branch");

        branchUpdateNameRequestDTO = new BranchUpdateNameRequestDTO();
        branchUpdateNameRequestDTO.setId("456");
        branchUpdateNameRequestDTO.setNewName("Updated Branch Name");
    }

    @Test
    void createFranchise_success() {
        when(franchiseService.createFranchise(franchiseRequestDTO)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<Object>> result = franchiseController.createFranchise(franchiseRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO))
                .verifyComplete();

        verify(franchiseService, times(1)).createFranchise(franchiseRequestDTO);
    }

    @Test
    void createFranchise_serviceError() {
        ResponseDTO<Franchise> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(franchiseService.createFranchise(franchiseRequestDTO)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<Object>> result = franchiseController.createFranchise(franchiseRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(franchiseService, times(1)).createFranchise(franchiseRequestDTO);
    }

    @Test
    void createFranchise_serviceEmitsError() {
        when(franchiseService.createFranchise(franchiseRequestDTO)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<Object>> result = franchiseController.createFranchise(franchiseRequestDTO);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseService, times(1)).createFranchise(franchiseRequestDTO);
    }

    @Test
    void updateFranchise_success() {
        when(franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<Object>> result = franchiseController.updateFranchise(franchiseUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO))
                .verifyComplete();

        verify(franchiseService, times(1)).updateFranchiseName(franchiseUpdateNameRequestDTO);
    }

    @Test
    void updateFranchise_serviceError() {
        ResponseDTO<Franchise> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<Object>> result = franchiseController.updateFranchise(franchiseUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(franchiseService, times(1)).updateFranchiseName(franchiseUpdateNameRequestDTO);
    }

    @Test
    void updateFranchise_serviceEmitsError() {
        when(franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<Object>> result = franchiseController.updateFranchise(franchiseUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseService, times(1)).updateFranchiseName(franchiseUpdateNameRequestDTO);
    }

    @Test
    void addBranchToFranchise_success() {
        when(franchiseService.addBranch(franchiseId, branchRequestDTO)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<ResponseDTO<Franchise>>> result = franchiseController.addBranchToFranchise(franchiseId, branchRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO))
                .verifyComplete();

        verify(franchiseService, times(1)).addBranch(franchiseId, branchRequestDTO);
    }

    @Test
    void addBranchToFranchise_serviceError() {
        ResponseDTO<Franchise> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(franchiseService.addBranch(franchiseId, branchRequestDTO)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<Franchise>>> result = franchiseController.addBranchToFranchise(franchiseId, branchRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(franchiseService, times(1)).addBranch(franchiseId, branchRequestDTO);
    }

    @Test
    void addBranchToFranchise_serviceEmitsError() {
        when(franchiseService.addBranch(franchiseId, branchRequestDTO)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<Franchise>>> result = franchiseController.addBranchToFranchise(franchiseId, branchRequestDTO);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseService, times(1)).addBranch(franchiseId, branchRequestDTO);
    }

    @Test
    void updateBranchName_success() {
        when(franchiseService.updateBranchName(franchiseId, branchUpdateNameRequestDTO)).thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<ResponseDTO<Franchise>>> result = franchiseController.updateBranchName(franchiseId, branchUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO))
                .verifyComplete();

        verify(franchiseService, times(1)).updateBranchName(franchiseId, branchUpdateNameRequestDTO);
    }

    @Test
    void updateBranchName_serviceError() {
        ResponseDTO<Franchise> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(franchiseService.updateBranchName(franchiseId, branchUpdateNameRequestDTO)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<Franchise>>> result = franchiseController.updateBranchName(franchiseId, branchUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(franchiseService, times(1)).updateBranchName(franchiseId, branchUpdateNameRequestDTO);
    }

    @Test
    void updateBranchName_serviceEmitsError() {
        when(franchiseService.updateBranchName(franchiseId, branchUpdateNameRequestDTO)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<Franchise>>> result = franchiseController.updateBranchName(franchiseId, branchUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseService, times(1)).updateBranchName(franchiseId, branchUpdateNameRequestDTO);
    }

    @Test
    void getHighestStockPerBranch_success() {
        when(franchiseService.getProductsWithMaxStockByBranch(franchiseId)).thenReturn(Mono.just(responseProductDTO));

        Mono<ResponseEntity<ResponseDTO<List<ProductWithBranchDTO>>>> result = franchiseController.getHighestStockPerBranch(franchiseId);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(responseProductDTO))
                .verifyComplete();

        verify(franchiseService, times(1)).getProductsWithMaxStockByBranch(franchiseId);
    }

    @Test
    void getHighestStockPerBranch_serviceError() {
        ResponseDTO<List<ProductWithBranchDTO>> errorResponse = new ResponseDTO<>();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        when(franchiseService.getProductsWithMaxStockByBranch(franchiseId)).thenReturn(Mono.just(errorResponse));

        Mono<ResponseEntity<ResponseDTO<List<ProductWithBranchDTO>>>> result = franchiseController.getHighestStockPerBranch(franchiseId);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().body(errorResponse))
                .verifyComplete();

        verify(franchiseService, times(1)).getProductsWithMaxStockByBranch(franchiseId);
    }

    @Test
    void getHighestStockPerBranch_serviceEmitsError() {
        when(franchiseService.getProductsWithMaxStockByBranch(franchiseId)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ResponseDTO<List<ProductWithBranchDTO>>>> result = franchiseController.getHighestStockPerBranch(franchiseId);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseService, times(1)).getProductsWithMaxStockByBranch(franchiseId);
    }
}