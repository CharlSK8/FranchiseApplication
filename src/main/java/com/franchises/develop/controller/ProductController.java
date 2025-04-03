package com.franchises.develop.controller;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Product;
import com.franchises.develop.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/products")
@Tag(name = "Products Controller", description = "Endpoints for managing products")
public class ProductController {

    private IProductService productService;

    /**
     * Updates the name of a specific product.
     *
     * @param productId The unique identifier of the product whose name will be updated.
     * @param newName   The new name to be assigned to the product.
     * @return A {@link Mono} emitting a {@link ResponseEntity} containing a {@link ResponseDTO}
     *         with the updated product information or an error response if the operation fails.
     */
    @PutMapping("/{productId}/name")
    @Operation(summary = "Update product name", description = "Updates the name of an existing product.")
    public Mono<ResponseEntity<ResponseDTO<Product>>    > updateProductName(@PathVariable String productId, @RequestParam String newName) {
        return productService.updateProductName(productId, newName)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }
}
