package com.franchises.develop.mapper;

import com.franchises.develop.dto.request.BranchDTO;
import com.franchises.develop.model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IBrancheMapper {

    Branch toBranch(BranchDTO branchDTO);
}
