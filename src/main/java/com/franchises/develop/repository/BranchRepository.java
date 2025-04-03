package com.franchises.develop.repository;

import com.franchises.develop.model.Branch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends ReactiveMongoRepository<Branch, String> {

    Mono<Object> findByName(String name);
}
