package com.franchises.develop.dto.response;

import com.franchises.develop.model.Product;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithBranchDTO {

    private Product product;
    private String branchId;
}
