package com.franchises.develop.service;

import com.franchises.develop.dto.request.ProductRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import reactor.core.publisher.Mono;

public interface IBranchService {
    Mono<ResponseDTO<Branch>> addProductToBranch(String branchId, ProductRequestDTO productRequest);
}
