package com.franchises.develop.controller;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.model.Product;
import com.franchises.develop.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/products")
@Tag(name = "Products Controller", description = "Endpoints for managing products")
public class ProductController {
}
