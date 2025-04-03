package com.franchises.develop.controller;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.dto.request.BranchUpdateNameRequestDTO;
import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.dto.request.FranchiseUpdateNameRequestDTO;
import com.franchises.develop.dto.response.ProductWithBranchDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Franchise;
import com.franchises.develop.service.IFranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1/franchises")
@Tag(name = "Franchise Controller", description = "Endpoints for managing franchises")
public class FranchiseController {

    private final IFranchiseService franchiseService;

    /**
     * Creates a new franchise.
     *
     * @param franchiseRequestDTO The request body containing the details of the franchise to be created.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing the created franchise details
     *         or an error response if the operation fails.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new franchise", description = "Creates a new franchise with the provided details")
    public Mono<ResponseEntity<Object>> createFranchise(@Valid @RequestBody FranchiseRequestDTO franchiseRequestDTO) {
        return franchiseService.createFranchise(franchiseRequestDTO)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }

    /**
     * Updates the name of an existing franchise.
     *
     * @param franchiseUpdateNameRequestDTO The request body containing the franchise ID and the new name.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing the updated franchise details
     *         or an error response if the operation fails.
     */
    @PatchMapping("/update")
    @Operation(summary = "Update franchise name", description = "Creates a new franchise with the provided details")
    public Mono<ResponseEntity<Object>> updateFranchise(@Valid @RequestBody FranchiseUpdateNameRequestDTO franchiseUpdateNameRequestDTO) {
        return franchiseService.updateFranchiseName(franchiseUpdateNameRequestDTO)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }

    /**
     * Adds a new branch to a specific franchise.
     *
     * @param franchiseId      The unique identifier of the franchise to which the branch will be added.
     * @param branchRequestDTO The request body containing the details of the branch to be added.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with the updated franchise details or an error response if the operation fails.
     */
    @PostMapping("/{franchiseId}/branches")
    @Operation(summary = "Adds a new branch to a specific franchise",
            description = "Adds a new branch to an existing franchise based on the provided franchise ID.")
    public Mono<ResponseEntity<ResponseDTO<Franchise>>> addBranchToFranchise(@PathVariable String franchiseId,
                                                                @RequestBody @Valid BranchDTO branchRequestDTO) {
        return franchiseService.addBranch(franchiseId, branchRequestDTO)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }

    /**
     * Updates the name of a branch within a specific franchise.
     *
     * @param franchiseId                The unique identifier of the franchise that owns the branch.
     * @param branchUpdateNameRequestDTO The request body containing the branch ID and the new name.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with the updated franchise details or an error response if the operation fails.
     */
    @PutMapping("/{franchiseId}/name")
    @Operation(
            summary = "Update branch name in a franchise",
            description = "Updates the name of a branch within a franchise based on the provided franchise ID and branch update details.")
    public Mono<ResponseEntity<ResponseDTO<Franchise>>> updateBranchName(
            @PathVariable String franchiseId,
            @RequestBody BranchUpdateNameRequestDTO branchUpdateNameRequestDTO) {
        return franchiseService.updateBranchName(franchiseId, branchUpdateNameRequestDTO)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }

    /**
     * Retrieves the products with the highest stock in each branch of a specific franchise.
     *
     * @param franchiseId The unique identifier of the franchise for which the highest stock per branch is retrieved.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with a list of {@link ProductWithBranchDTO}, representing the products with the highest stock per branch,
     *         or an error response if the operation fails.
     */
    @GetMapping("/{franchiseId}/branches/highest-stock")
    @Operation(summary = "Find product with highest stock per branch",
            description = "Returns a list of products (with the highest stock) for each branch of the given franchise.")
    public Mono<ResponseEntity<ResponseDTO<List<ProductWithBranchDTO>>>> getHighestStockPerBranch(
            @PathVariable String franchiseId) {
        return franchiseService.getProductsWithMaxStockByBranch(franchiseId)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }
}
