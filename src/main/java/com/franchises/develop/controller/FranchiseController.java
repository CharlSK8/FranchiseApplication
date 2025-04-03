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
}
