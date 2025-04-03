package com.franchises.develop.repository;

import com.franchises.develop.model.Branch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends ReactiveMongoRepository<Branch, String> {

}
