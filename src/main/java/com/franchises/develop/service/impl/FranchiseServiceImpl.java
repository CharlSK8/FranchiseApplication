package com.franchises.develop.service.impl;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.dto.request.BranchUpdateNameRequestDTO;
import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.dto.request.FranchiseUpdateNameRequestDTO;
import com.franchises.develop.exception.handler.*;
import com.franchises.develop.util.ErrorHandlerUtils;
import com.franchises.develop.dto.response.ProductWithBranchDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.mapper.IBrancheMapper;
import com.franchises.develop.mapper.IFranchiseMapper;
import com.franchises.develop.model.Franchise;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.BranchRepository;
import com.franchises.develop.repository.FranchiseRepository;
import com.franchises.develop.repository.ProductRepository;
import com.franchises.develop.service.IFranchiseService;
import com.franchises.develop.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class FranchiseServiceImpl implements IFranchiseService {
}
