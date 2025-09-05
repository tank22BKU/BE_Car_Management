package com.wbsrisktaskerx.wbsrisktaskerx.mapper;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.Car;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.CarBranch;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.CarDetail;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CarSpecificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarBranchResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarSpecificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarStructMapper {
    @Mapping(target = "id", source = "car.id")
    @Mapping(target = "model", source = "car.model")
    @Mapping(target = "variant", source = "car.variant")
    @Mapping(target = "vehicleType", source = "car.vehicleType")
    @Mapping(target = "price", source = "car.price")
    @Mapping(target = "imageUrl", source = "car.imageUrl")
    @Mapping(target = "isActive", source = "car.isActive")
    @Mapping(target = "carSpecificationResponse", source = "specificationResponse")
    @Mapping(target = "carBranchResponse", source = "brands")
    CarDetailResponse carDetailBranchMapper(Car car, CarSpecificationResponse specificationResponse, List<CarBranchResponse> brands);

    CarSpecificationResponse toCarSpecificationResponse(CarDetail carDetail);

    CarBranchResponse toCarBranchResponse(CarBranch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Car toCar(AddCarRequest request);

    CarResponse toCarResponse(Car car);
    void updateCarSpecifications(@MappingTarget CarDetail carDetail, CarSpecificationsRequest request);
}
