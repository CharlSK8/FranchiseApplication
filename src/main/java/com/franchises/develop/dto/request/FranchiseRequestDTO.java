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
@Schema(name = "FranchiseRequest", description = "Request payload for creating a new franchise")
public class FranchiseRequestDTO {

    @NotBlank(message = "The franchise name is required.")
    @Schema(description = "Name of the franchise", example = "Tech Store")
    private String name;

    @Schema(description = "List of branches associated with the franchise")
    private List<BranchDTO> branches;

}
