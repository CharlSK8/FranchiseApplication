package com.franchises.develop.controller;

import com.franchises.develop.dto.request.ProductRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import com.franchises.develop.service.IBranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branch controllers", description = "Endpoints for managing branches and their products")
public class BranchController {

    private final IBranchService branchService;

    /**
     * Adds a product to a specific branch.
     *
     * @param branchId      The unique identifier of the branch where the product will be added.
     * @param productRequest The request body containing the product details.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with the updated branch information or an error response.
     */
    @PutMapping("/{branchId}/products")
    @Operation(summary = "Add a product to a branch",
            description = "Adds a product to a branch. If the product does not exist, it will be created automatically.")
    public Mono<ResponseEntity<ResponseDTO<Branch>>> addProductToBranch( @PathVariable String branchId,
                                                                        @RequestBody ProductRequestDTO productRequest) {
        return branchService.addProductToBranch(branchId, productRequest)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));

    }
}