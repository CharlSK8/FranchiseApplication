package com.franchises.develop.repository;

import com.franchises.develop.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Mono<Product> findByNameIgnoreCase(String name);

}
