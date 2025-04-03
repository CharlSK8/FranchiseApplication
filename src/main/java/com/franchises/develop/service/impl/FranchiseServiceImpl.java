package com.franchises.develop.service.impl;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.dto.request.BranchUpdateNameRequestDTO;
import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.dto.request.FranchiseUpdateNameRequestDTO;
import com.franchises.develop.exception.handler.*;
import com.franchises.develop.util.ErrorHandlerUtils;
import com.franchises.develop.dto.response.ProductWithBranchDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.mapper.IBrancheMapper;
import com.franchises.develop.mapper.IFranchiseMapper;
import com.franchises.develop.model.Franchise;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.BranchRepository;
import com.franchises.develop.repository.FranchiseRepository;
import com.franchises.develop.repository.ProductRepository;
import com.franchises.develop.service.IFranchiseService;
import com.franchises.develop.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class FranchiseServiceImpl implements IFranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final IFranchiseMapper franchiseMapper;
    private final IBrancheMapper branchMapper;
    private final ProductRepository productRepository;

    /**
     * Creates a new franchise.
     *
     * @param franchiseRequestDTO The request containing the details of the franchise to be created.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the created franchise details
     *         or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Franchise>> createFranchise(FranchiseRequestDTO franchiseRequestDTO) {
        return franchiseRepository.existsByName(franchiseRequestDTO.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.just(buildResponse(HttpStatus.CONFLICT, Constants.FRANCHISE_ALREADY_EXISTS, null));
                    } else {
                        return franchiseRepository.save(franchiseMapper.toFranchise(franchiseRequestDTO))
                                .map(savedFranchise ->buildResponse(HttpStatus.CREATED, Constants.MESSAGE_CREATED_FRANCHISE, savedFranchise))
                                .onErrorResume(ErrorHandlerUtils::handleError);
                    }
                });

    }

    /**
     * Updates the name of an existing franchise.
     *
     * @param franchiseUpdateNameRequestDTO The request containing the franchise ID and the new name.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated franchise details
     *         or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Franchise>> updateFranchiseName(FranchiseUpdateNameRequestDTO franchiseUpdateNameRequestDTO) {
        return franchiseRepository.existsByName(franchiseUpdateNameRequestDTO.getName())
                .flatMap(nameExists -> {
                    if (nameExists) {
                        return Mono.just(buildResponse(HttpStatus.CONFLICT, Constants.FRANCHISE_ALREADY_EXISTS, null));
                    }
                    return franchiseRepository.findById(franchiseUpdateNameRequestDTO.getId())
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException(franchiseUpdateNameRequestDTO.getId())))
                            .flatMap(existingFranchise -> {
                                existingFranchise.setName(franchiseUpdateNameRequestDTO.getName());
                                return franchiseRepository.save(existingFranchise)
                                        .thenReturn(existingFranchise);
                            })
                            .map(updatedFranchise -> buildResponse(HttpStatus.OK, Constants.FRANCHISE_NAME_UPDATED, updatedFranchise))
                            .onErrorResume(ErrorHandlerUtils::handleError);
                });
    }

    /**
     * Adds a new branch to a franchise.
     *
     * @param franchiseId The unique identifier of the franchise to which the branch will be added.
     * @param branchDTO   The request containing the details of the branch to be created.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated franchise details
     *         with the newly added branch, or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Franchise>> addBranch(String franchiseId, BranchDTO branchDTO) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(franchiseId)))
                .flatMap(franchise ->
                        branchRepository.save(branchMapper.toBranch(branchDTO))
                                .map(savedBranch -> {
                                    franchise.getBranchIds().add(savedBranch.getId());
                                    return franchise;
                                })
                                .flatMap(franchiseRepository::save)
                )
                .map(updatedFranchise -> buildResponse(HttpStatus.CREATED, Constants.BRANCH_CREATED_SUCCESSFULLY, updatedFranchise))
                .onErrorResume(ErrorHandlerUtils::handleError);
    }

    /**
     * Updates the name of a branch within a franchise.
     *
     * @param franchiseId The unique identifier of the franchise that owns the branch.
     * @param requestDTO  The request containing the branch ID and the new name.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated franchise details
     *         with the renamed branch, or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Franchise>> updateBranchName(String franchiseId, BranchUpdateNameRequestDTO requestDTO) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    if (!franchise.getBranchIds().contains(requestDTO.getId())) {
                        return Mono.error(new BranchNotFoundException("Branch not found with ID: " + requestDTO.getId()));
                    }
                    return branchRepository.findById(requestDTO.getId())
                            .switchIfEmpty(Mono.error(new BranchNotFoundException("Branch not found with ID: " + requestDTO.getId())))
                            .flatMap(branch -> {
                                if (branch.getName().equalsIgnoreCase(requestDTO.getNewName())){
                                    return Mono.error(new BranchNameAlreadyUpToDateException(requestDTO.getId(), requestDTO.getNewName()));
                                }
                                branch.setName(requestDTO.getNewName());
                                return branchRepository.save(branch);
                            })
                            .thenReturn(franchise);
                })
                .map(franchise -> buildResponse(HttpStatus.OK, Constants.BRANCH_UPDATED_SUCCESSFULLY, franchise))
                .onErrorResume(ErrorHandlerUtils::handleError);
    }

    /**
     * Retrieves the products with the highest stock for each branch within a franchise.
     *
     * @param franchiseId The unique identifier of the franchise.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing a list of {@link ProductWithBranchDTO}
     *         representing the products with the highest stock in each branch, or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<List<ProductWithBranchDTO>>> getProductsWithMaxStockByBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(franchiseId)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranchIds()))
                .flatMap(branchId -> getProductWithMaxStock(branchId)
                        .map(product -> new ProductWithBranchDTO(product, branchId)))
                .collectList()
                .map(products -> buildResponse(HttpStatus.OK, Constants.PRODUCTS_WITH_HIGHEST_STOCK_PER_BRANCH, products))
                .onErrorResume(ErrorHandlerUtils::handleError);
    }

    /**
     * Retrieves the product with the highest stock in a given branch.
     *
     * @param sucursalId The unique identifier of the branch.
     * @return A {@link Mono} emitting the {@link Product} with the highest stock in the specified branch,
     *         or an empty {@link Mono} if no product is found.
     */
    private Mono<Product> getProductWithMaxStock(String sucursalId) {
        return branchRepository.findById(sucursalId)
                .flatMapMany(sucursal -> Flux.fromIterable(sucursal.getProductsId()))
                .flatMap(productRepository::findById)
                .sort(Comparator.comparingInt(Product::getStock).reversed())
                .next();
    }

    /**
     * Builds a ResponseDTO object with the provided HTTP status, message, and data.
     *
     * @param <T>     The data type of the ResponseDTO object.
     * @param status  The HTTP status of the response.
     * @param message The message of the response.
     * @param data    The data of the response.
     * @return A ResponseDTO object with the provided data.
     */
    private <T> ResponseDTO<T> buildResponse(HttpStatus status, String message, T data) {
        return ResponseDTO.<T>builder()
                .code(status.value())
                .message(message)
                .response(data)
                .build();
    }

}
