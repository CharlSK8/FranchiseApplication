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

    /**
     * Removes a product from a specific branch.
     *
     * @param branchId  The unique identifier of the branch from which the product will be removed.
     * @param productId The unique identifier of the product to be removed.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with the updated branch information or an error response if the operation fails.
     */
    @DeleteMapping("/{branchId}/products/{productId}")
    @Operation(summary = "Remove a product from a branch", description = "Removes a product from a branch if it exists.")
    public Mono<ResponseEntity<ResponseDTO<Branch>>> removeProductFromBranch(@PathVariable String branchId,
                                                                            @PathVariable String productId) {
        return branchService.removeProductFromBranch(branchId, productId)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }

    /**
     * Updates the stock quantity of a specific product in a given branch.
     *
     * @param branchId  The unique identifier of the branch where the product stock will be updated.
     * @param productId The unique identifier of the product whose stock will be modified.
     * @param newStock  The new stock quantity to be set for the product.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with the updated product information or an error response if the operation fails.
     */
    @PutMapping("/{branchId}/products/{productId}/stock")
    @Operation(summary = "Update product stock", description = "Updates the stock of a product in a branch.")
    public Mono<ResponseEntity<ResponseDTO<Product>>> updateProductStock(@PathVariable String branchId,
                                                        @PathVariable String productId,
                                                        @RequestParam int newStock) {
        return branchService.updateProductStock(branchId, productId, newStock)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }

}