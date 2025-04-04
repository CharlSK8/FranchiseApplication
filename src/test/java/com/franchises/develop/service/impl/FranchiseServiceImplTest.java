package com.franchises.develop.service.impl;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.dto.request.BranchUpdateNameRequestDTO;
import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.dto.request.FranchiseUpdateNameRequestDTO;
import com.franchises.develop.dto.response.ProductWithBranchDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.exception.handler.BranchNameAlreadyExistsException;
import com.franchises.develop.exception.handler.BranchNameAlreadyUpToDateException;
import com.franchises.develop.exception.handler.BranchNotFoundException;
import com.franchises.develop.exception.handler.ResourceNotFoundException;
import com.franchises.develop.mapper.IBrancheMapper;
import com.franchises.develop.mapper.IFranchiseMapper;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Franchise;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.BranchRepository;
import com.franchises.develop.repository.FranchiseRepository;
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


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private IFranchiseMapper franchiseMapper;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IBrancheMapper branchMapper;

    @InjectMocks
    private FranchiseServiceImpl franchiseService;

    private FranchiseRequestDTO franchiseRequestDTO;
    private Franchise franchise;
    private String franchiseId;
    private BranchDTO branchDTO;
    private Branch branch;
    private Branch branchOld;
    private BranchUpdateNameRequestDTO requestDTO;
    private Branch branch1;
    private Branch branch2;
    private Product product1;
    private Product product2;

    private FranchiseUpdateNameRequestDTO franchiseUpdateNameRequestDTO;

    @BeforeEach
    void setUp() {
        franchiseId = "123";
        branchDTO = new BranchDTO();
        branchDTO.setName("Test Branch");

        requestDTO = new BranchUpdateNameRequestDTO();
        requestDTO.setId("456");
        requestDTO.setNewName("New Branch Name");

        franchise = new Franchise();
        franchise.setId(franchiseId);
        franchise.setBranchIds(new ArrayList<>(List.of("456")));


        branch = new Branch();
        branch.setId("456");
        branch.setName("Test Branch");

        branchOld = new Branch();
        branchOld.setId("456");
        branchOld.setName("Old Branch Name");

        franchiseRequestDTO = new FranchiseRequestDTO();
        franchiseRequestDTO.setName("Test Franchise");

        franchise = new Franchise();
        franchise.setId("123");
        franchise.setName("Test Franchise");

        franchiseUpdateNameRequestDTO = new FranchiseUpdateNameRequestDTO();
        franchiseUpdateNameRequestDTO.setId("123");
        franchiseUpdateNameRequestDTO.setName("New Franchise Name");

        branch1 = new Branch();
        branch1.setId("456");
        branch1.setProductsId(List.of("789", "101"));

        branch2 = new Branch();
        branch2.setId("112");
        branch2.setProductsId(List.of("131", "141"));

        franchise = new Franchise();
        franchise.setId(franchiseId);
        franchise.setBranchIds(List.of("456", "112"));

        product1 = new Product();
        product1.setId("789");
        product1.setStock(10);

        product2 = new Product();
        product2.setId("131");
        product2.setStock(20);

    }

    @Test
    void createFranchise_success() {
        when(franchiseRepository.existsByName(franchiseRequestDTO.getName())).thenReturn(Mono.just(false));
        when(franchiseMapper.toFranchise(franchiseRequestDTO)).thenReturn(franchise);
        when(franchiseRepository.save(franchise)).thenReturn(Mono.just(franchise));

        Mono<ResponseDTO<Franchise>> result = franchiseService.createFranchise(franchiseRequestDTO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.CREATED.value() &&
                                response.getMessage().equals(Constants.MESSAGE_CREATED_FRANCHISE) &&
                                response.getResponse().equals(franchise))
                .verifyComplete();

        verify(franchiseRepository, times(1)).existsByName(franchiseRequestDTO.getName());
        verify(franchiseMapper, times(1)).toFranchise(franchiseRequestDTO);
        verify(franchiseRepository, times(1)).save(franchise);
    }

    @Test
    void createFranchise_franchiseAlreadyExists() {
        when(franchiseRepository.existsByName(franchiseRequestDTO.getName())).thenReturn(Mono.just(true));

        Mono<ResponseDTO<Franchise>> result = franchiseService.createFranchise(franchiseRequestDTO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.CONFLICT.value() &&
                                response.getMessage().equals(Constants.FRANCHISE_ALREADY_EXISTS) &&
                                response.getResponse() == null)
                .verifyComplete();

        verify(franchiseRepository, times(1)).existsByName(franchiseRequestDTO.getName());
    }

    @Test
    void createFranchise_serviceError() {
        when(franchiseRepository.existsByName(franchiseRequestDTO.getName())).thenReturn(Mono.just(false));
        when(franchiseMapper.toFranchise(franchiseRequestDTO)).thenReturn(franchise);
        when(franchiseRepository.save(franchise)).thenReturn(Mono.error(new RuntimeException("Test Error")));

        try (MockedStatic<ErrorHandlerUtils> mockedStatic = mockStatic(ErrorHandlerUtils.class)) {
            mockedStatic.when(() -> ErrorHandlerUtils.handleError(any(Throwable.class))).thenAnswer(invocation -> {
                return Mono.just(new ResponseDTO<>());
            });

            Mono<ResponseDTO<Franchise>> result = franchiseService.createFranchise(franchiseRequestDTO);

            StepVerifier.create(result)
                    .expectNext(new ResponseDTO<>())
                    .verifyComplete();

            verify(franchiseRepository, times(1)).existsByName(franchiseRequestDTO.getName());
            verify(franchiseMapper, times(1)).toFranchise(franchiseRequestDTO);
            verify(franchiseRepository, times(1)).save(franchise);
        }
    }

    @Test
    void updateFranchiseName_success() {
        when(franchiseRepository.existsByName(franchiseUpdateNameRequestDTO.getName())).thenReturn(Mono.just(false));
        when(franchiseRepository.findById(franchiseUpdateNameRequestDTO.getId())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<ResponseDTO<Franchise>> result = franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.OK.value() &&
                                response.getMessage().equals(Constants.FRANCHISE_NAME_UPDATED) &&
                                response.getResponse().getName().equals(franchiseUpdateNameRequestDTO.getName()))
                .verifyComplete();

        verify(franchiseRepository, times(1)).existsByName(franchiseUpdateNameRequestDTO.getName());
        verify(franchiseRepository, times(1)).findById(franchiseUpdateNameRequestDTO.getId());
        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    void updateFranchiseName_nameAlreadyExists() {
        when(franchiseRepository.existsByName(franchiseUpdateNameRequestDTO.getName())).thenReturn(Mono.just(true));

        Mono<ResponseDTO<Franchise>> result = franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.CONFLICT.value() &&
                                response.getMessage().equals(Constants.FRANCHISE_ALREADY_EXISTS) &&
                                response.getResponse() == null)
                .verifyComplete();

        verify(franchiseRepository, times(1)).existsByName(franchiseUpdateNameRequestDTO.getName());
    }

    @Test
    void updateFranchiseName_serviceError() {
        when(franchiseRepository.existsByName(franchiseUpdateNameRequestDTO.getName())).thenReturn(Mono.just(false));
        when(franchiseRepository.findById(franchiseUpdateNameRequestDTO.getId())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.error(new RuntimeException("Test Error")));

        try (MockedStatic<ErrorHandlerUtils> mockedStatic = mockStatic(ErrorHandlerUtils.class)) {
            mockedStatic.when(() -> ErrorHandlerUtils.handleError(any(Throwable.class))).thenAnswer(invocation -> {
                return Mono.just(new ResponseDTO<>());
            });

            Mono<ResponseDTO<Franchise>> result = franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO);

            StepVerifier.create(result)
                    .expectNext(new ResponseDTO<>())
                    .verifyComplete();

            verify(franchiseRepository, times(1)).existsByName(franchiseUpdateNameRequestDTO.getName());
            verify(franchiseRepository, times(1)).findById(franchiseUpdateNameRequestDTO.getId());
            verify(franchiseRepository, times(1)).save(any(Franchise.class));
        }
    }

    @Test
    void addBranch_success() {
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.findByName(branchDTO.getName())).thenReturn(Mono.empty());
        when(branchMapper.toBranch(branchDTO)).thenReturn(branch);
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<ResponseDTO<Franchise>> result = franchiseService.addBranch(franchiseId, branchDTO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.CREATED.value() &&
                                response.getMessage().equals(Constants.BRANCH_CREATED_SUCCESSFULLY) &&
                                response.getResponse().getBranchIds().contains(branch.getId()))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(franchiseId);
        verify(branchRepository, times(1)).findByName(branchDTO.getName());
        verify(branchMapper, times(1)).toBranch(branchDTO);
        verify(branchRepository, times(1)).save(any(Branch.class));
        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    void addBranch_serviceError() {
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.findByName(branchDTO.getName())).thenReturn(Mono.empty());
        when(branchMapper.toBranch(branchDTO)).thenReturn(branch);
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.error(new RuntimeException("Test Error")));

        try (MockedStatic<ErrorHandlerUtils> mockedStatic = mockStatic(ErrorHandlerUtils.class)) {
            mockedStatic.when(() -> ErrorHandlerUtils.handleError(any(Throwable.class))).thenAnswer(invocation -> {
                return Mono.just(new ResponseDTO<>());
            });

            Mono<ResponseDTO<Franchise>> result = franchiseService.addBranch(franchiseId, branchDTO);

            StepVerifier.create(result)
                    .expectNext(new ResponseDTO<>())
                    .verifyComplete();

            verify(franchiseRepository, times(1)).findById(franchiseId);
            verify(branchRepository, times(1)).findByName(branchDTO.getName());
            verify(branchMapper, times(1)).toBranch(branchDTO);
            verify(branchRepository, times(1)).save(any(Branch.class));
        }
    }

    @Test
    void getProductsWithMaxStockByBranch_success() {
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.findById("456")).thenReturn(Mono.just(branch1));
        when(branchRepository.findById("112")).thenReturn(Mono.just(branch2));
        when(productRepository.findById("789")).thenReturn(Mono.just(product1));
        when(productRepository.findById("101")).thenReturn(Mono.just(new Product()));
        when(productRepository.findById("131")).thenReturn(Mono.just(product2));
        when(productRepository.findById("141")).thenReturn(Mono.just(new Product()));

        Mono<ResponseDTO<List<ProductWithBranchDTO>>> result = franchiseService.getProductsWithMaxStockByBranch(franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getCode() == HttpStatus.OK.value() &&
                                response.getMessage().equals(Constants.PRODUCTS_WITH_HIGHEST_STOCK_PER_BRANCH) &&
                                response.getResponse().size() == 2 &&
                                response.getResponse().get(0).getProduct().equals(product1) &&
                                response.getResponse().get(1).getProduct().equals(product2))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(franchiseId);
        verify(branchRepository, times(2)).findById(anyString());
        verify(productRepository, times(4)).findById(anyString());
    }


    @Test
    void getProductWithMaxStock_success() {
        when(branchRepository.findById("456")).thenReturn(Mono.just(branch1));
        when(productRepository.findById("789")).thenReturn(Mono.just(product1));
        when(productRepository.findById("101")).thenReturn(Mono.just(new Product()));

        Mono<Product> result = franchiseService.getProductWithMaxStock("456");

        StepVerifier.create(result)
                .expectNext(product1)
                .verifyComplete();

        verify(branchRepository, times(1)).findById("456");
        verify(productRepository, times(2)).findById(anyString());
    }

    @Test
    void getProductWithMaxStock_empty() {
        Branch emptyBranch = new Branch();
        emptyBranch.setId("emptyBranchId");
        emptyBranch.setProductsId(new ArrayList<>());

        when(branchRepository.findById("emptyBranchId")).thenReturn(Mono.just(emptyBranch));

        Mono<Product> result = franchiseService.getProductWithMaxStock("emptyBranchId");

        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository, times(1)).findById("emptyBranchId");
    }

    @Test
    void getProductsWithMaxStockByBranch_serviceError() {
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.error(new RuntimeException("Test Error")));

        try (MockedStatic<ErrorHandlerUtils> mockedStatic = mockStatic(ErrorHandlerUtils.class)) {
            mockedStatic.when(() -> ErrorHandlerUtils.handleError(any(Throwable.class))).thenAnswer(invocation -> {
                return Mono.just(new ResponseDTO<>());
            });

            Mono<ResponseDTO<List<ProductWithBranchDTO>>> result = franchiseService.getProductsWithMaxStockByBranch(franchiseId);

            StepVerifier.create(result)
                    .expectNext(new ResponseDTO<>())
                    .verifyComplete();

            verify(franchiseRepository, times(1)).findById(franchiseId);
        }
    }
}