package com.franchises.develop.service.impl;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.exception.handler.ProductAlreadyExistsException;
import com.franchises.develop.exception.handler.ProductNameAlreadyUpToDateException;
import com.franchises.develop.exception.handler.ProductNotFoundException;
import com.franchises.develop.model.Product;
import com.franchises.develop.repository.ProductRepository;
import com.franchises.develop.service.IProductService;
import com.franchises.develop.util.Constants;
import com.franchises.develop.util.ErrorHandlerUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    /**
     * Updates the name of a specific product.
     *
     * @param productId The unique identifier of the product to be updated.
     * @param newName   The new name to be assigned to the product.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing the updated product details
     *         or an error response if the operation fails.
     */
    @Override
    public Mono<ResponseDTO<Product>> updateProductName(String productId, String newName) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .flatMap(product -> {
                    if (product.getName().equalsIgnoreCase(newName)) {
                        return Mono.error(new ProductNameAlreadyUpToDateException(productId, newName));
                    }
                    return productRepository.findByNameIgnoreCase(newName)
                            .flatMap(existingProduct -> Mono.error(new ProductAlreadyExistsException(newName)))
                            .switchIfEmpty(Mono.defer(() -> {
                                product.setName(newName);
                                return productRepository.save(product);
                            }))
                            .thenReturn(product);
                })
                .map(updatedProduct -> buildResponse(HttpStatus.OK, Constants.PRODUCT_NAME_UPDATED_SUCCESSFULLY, updatedProduct))
                .onErrorResume(ErrorHandlerUtils::handleError);
    }
}
