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
    @PutMapping("/{productId}/name")
    @Operation(summary = "Update product name", description = "Updates the name of an existing product.")
    public Mono<ResponseEntity<ResponseDTO<Product>>    > updateProductName(@PathVariable String productId, @RequestParam String newName) {
        return productService.updateProductName(productId, newName)
                .map(responseDTO -> ResponseEntity
                        .status(responseDTO.getCode())
                        .body(responseDTO));
    }
}
