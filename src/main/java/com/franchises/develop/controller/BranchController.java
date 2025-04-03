package com.franchises.develop.controller;

import com.franchises.develop.dto.request.ProductRequestDTO;
import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Branch;
import com.franchises.develop.model.Product;
import com.franchises.develop.service.IBranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branch controllers", description = "Endpoints for managing branches and their products")
public class BranchController {
}