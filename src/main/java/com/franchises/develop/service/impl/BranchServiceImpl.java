package com.franchises.develop.service.impl;

import com.franchises.develop.dto.request.ProductRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.exception.handler.BranchNotFoundException;
import com.franchises.develop.exception.handler.ProductAlreadyExistsException;
import com.franchises.develop.exception.handler.ProductNameAlreadyUpToDateException;
import com.franchises.develop.exception.handler.ProductNotFoundException;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.BranchRepository;
import com.franchises.develop.repository.ProductRepository;
import com.franchises.develop.service.IBranchService;
import com.franchises.develop.util.Constants;
import com.franchises.develop.util.ErrorHandlerUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BranchServiceImpl implements IBranchService {
}
