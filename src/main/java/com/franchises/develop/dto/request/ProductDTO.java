package com.franchises.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotBlank(message = "The product name is required.")
    @Schema(description = "Name of the product", example = "Laptop")
    private String name;

    @Min(value = 0, message = "")
    @Schema(description = "Stock quantity of the product", example = "12")
    private int stock;
}
