package com.franchises.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    @NotBlank(message = "The branch name is required.")
    @Schema(description = "Name of the branch", example = "Downtown Branch")
    private String name;

    @Schema(description = "List of products available in the branch")
    private List<ProductDTO> products;
}
