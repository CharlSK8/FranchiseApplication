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
}
