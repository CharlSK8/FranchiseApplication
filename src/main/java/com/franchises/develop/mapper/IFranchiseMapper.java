package com.franchises.develop.mapper;

import com.franchises.develop.dto.request.FranchiseRequestDTO;
import com.franchises.develop.model.Franchise;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IFranchiseMapper {

    Franchise toFranchise(FranchiseRequestDTO franchiseRequestDTO);
}
