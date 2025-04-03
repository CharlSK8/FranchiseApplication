package com.franchises.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "Product name cannot be blank")
    @Schema(description = "Name of the product", example = "P.C")
    private String name;

    @Positive(message = "Stock must be greater than zero" )
    @Schema(description = "Stock of the product", example = "1")
    private int stock;
}
