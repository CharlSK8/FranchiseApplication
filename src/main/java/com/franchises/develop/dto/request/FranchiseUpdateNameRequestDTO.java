package com.franchises.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FranchiseUpdateNameRequest", description = "Request payload for update a franchise")
public class FranchiseUpdateNameRequestDTO {

    @NotBlank(message = "The franchise id is required.")
    @Schema(description = "Id of the franchise", example = "1")
    private String id;

    @NotBlank(message = "The franchise name is required.")
    @Schema(description = "Name of the franchise", example = "Tech Store")
    private String name;
}
