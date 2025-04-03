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

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    /**
     * Adds a product to a specific branch.
     *
     * @param branchId       The unique identifier of the branch where the product will be added.
     * @param productRequest The request containing the details of the product to be added.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated branch details
     *         or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Branch>> addProductToBranch(String branchId, ProductRequestDTO productRequest) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch ->
                        productRepository.findByNameIgnoreCase(productRequest.getName())
                                .switchIfEmpty(productRepository.save(Product.builder()
                                        .name(productRequest.getName())
                                        .stock(productRequest.getStock())
                                        .build()))
                                .flatMap(product -> {
                                    if (branch.getProductsId().contains(product.getId())) {
                                        return Mono.error(new ProductAlreadyExistsException(productRequest.getName()));
                                    }

                                    branch.getProductsId().add(product.getId());
                                    return branchRepository.save(branch)
                                            .map(savedBranch -> buildResponse(HttpStatus.CREATED, Constants.PRODUCT_CREATED_SUCCESSFULLY, savedBranch));
                                })
                )
                .onErrorResume(ErrorHandlerUtils::handleError);
    }

    /**
     * Removes a product from a specific branch.
     *
     * @param branchId  The unique identifier of the branch from which the product will be removed.
     * @param productId The unique identifier of the product to be removed.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated branch details
     *         or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Branch>> removeProductFromBranch(String branchId, String productId) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> {
                    if (!branch.getProductsId().contains(productId)) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    branch.getProductsId().remove(productId);
                    return branchRepository.save(branch)
                            .map(updatedBranch -> buildResponse(HttpStatus.OK, Constants.PRODUCT_REMOVED_SUCCESSFULLY, updatedBranch));
                })
                .onErrorResume(ErrorHandlerUtils::handleError);
    }

    /**
     * Updates the stock of a specific product in a given branch.
     *
     * @param branchId  The unique identifier of the branch where the product stock will be updated.
     * @param productId The unique identifier of the product whose stock will be modified.
     * @param newStock  The new stock quantity to be assigned to the product.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated product details
     *         or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Product>> updateProductStock(String branchId, String productId, int newStock) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> {
                    if (!branch.getProductsId().contains(productId)) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }
                    return productRepository.findById(productId)
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                            .flatMap(product -> {
                                product.setStock(newStock);
                                return productRepository.save(product)
                                        .map(updatedProduct -> buildResponse(HttpStatus.OK, Constants.PRODUCT_STOCK_UPDATED_SUCCESSFULLY, updatedProduct));
                            });
                })
                .onErrorResume(ErrorHandlerUtils::handleError);
    }

    /**
     * Builds a standardized response object.
     *
     * @param status  The HTTP status of the response.
     * @param message A descriptive message about the response.
     * @param data    The response payload containing the requested data.
     * @param <T>     The type of the response data.
     * @return A {@link ResponseDTO} containing the provided status, message, and data.
     */
    private <T> ResponseDTO<T> buildResponse(HttpStatus status, String message, T data) {
        return ResponseDTO.<T>builder()
                .code(status.value())
                .message(message)
                .response(data)
                .build();
    }
}
