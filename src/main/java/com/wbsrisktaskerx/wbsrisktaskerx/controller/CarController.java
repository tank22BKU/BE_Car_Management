package com.wbsrisktaskerx.wbsrisktaskerx.controller;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EndpointConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.ApiResult;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.ActiveRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CarSpecificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CarRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.service.car.ICarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(EndpointConstants.CAR)
public class CarController {
    public final ICarService carService;
    public final CarRepository carRepository;

    public CarController(ICarService carService, CarRepository carRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
    }

    @Operation(summary = "Get car detail by ID", description = "Retrieve full specification and branches of a car by its ID")
    @GetMapping(EndpointConstants.ID)
    public ResponseEntity<ApiResult<CarDetailResponse>> getCarDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(ApiResult.success(carService.getCarDetail(id)));
    }

    @Operation(summary = "Get all vehicle types", description = "This API will get all vehicle types in DB")
    @GetMapping(EndpointConstants.VEHICLE_TYPE)
    public ResponseEntity<ApiResult<List<String>>> getAllVehicleType() {
        return ResponseEntity.ok(ApiResult.success(carService.getAllVehicleType()));
    }

    @PostMapping(EndpointConstants.SEARCH_FILTER)
    public ResponseEntity<ApiResult<Page<CarResponse>>> getListCars(
            @RequestBody @Valid PagingRequest<SearchFilterCarRequest> request) {
        return ResponseEntity.ok(ApiResult.success(carService.searchFilterCar(request)));
    }

    @Operation(summary = "Delete car by ID", description = "This API will delete a car by its ID")
    @DeleteMapping(EndpointConstants.ID)
    public ResponseEntity<ApiResult<Boolean>> deleteCarById(@PathVariable int id) {
        return ResponseEntity.ok(ApiResult.success(carService.deleteCarById(id)));
    }

    @Operation(summary = "Add a new car", description = "Creates a new car entry with the given model, variant, vehicle type, price, and status. If status is null, it automatically sets true.")
    @PostMapping
    public ResponseEntity<ApiResult<Boolean>> addCar(@RequestBody @Valid AddCarRequest request) {
        return ResponseEntity.ok(ApiResult.success(carService.addCar(request)));
    }

    @Operation(summary = "Switch Car status", description = "This API will get Request Body then check if the Car with Id is exists then switch between active and inactive.")
    @PutMapping(EndpointConstants.STATUS)
    public ResponseEntity<ApiResult<Boolean>> switchCarStatus(@RequestBody @Valid ActiveRequest request) {
        return ResponseEntity.ok(ApiResult.success(carService.switchCarStatus(request)));
    }

    @Operation(summary = "Update car specifications", description = "Update specifications of a car by its ID")
    @PutMapping(EndpointConstants.ID + EndpointConstants.SPECIFICATIONS)
    public ResponseEntity<ApiResult<Boolean>> updateCarSpecifications(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CarSpecificationsRequest request) {
        return ResponseEntity.ok(ApiResult.success(carService.updateCarSpecifications(id, request)));
    }
}
