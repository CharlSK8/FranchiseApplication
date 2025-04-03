package com.franchises.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BranchUpdateNameRequestDTO", description = "Request payload for update a branch name")
public class BranchUpdateNameRequestDTO {

    @NotBlank(message = "The new branch name is required.")
    @Schema(description = "Name of the branch", example = "Downtown Branch")
    private String newName;

    @NotBlank(message = "The branch id is required.")
    @Schema(description = "Id of the branch", example = "1")
    private String id;
}
