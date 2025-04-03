package com.franchises.develop.service;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.dto.request.BranchUpdateNameRequestDTO;
import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.dto.request.FranchiseUpdateNameRequestDTO;
import com.franchises.develop.dto.response.ProductWithBranchDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Franchise;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFranchiseService {
    Mono<ResponseDTO<Franchise>> createFranchise(FranchiseRequestDTO franchiseRequestDTO);
}
