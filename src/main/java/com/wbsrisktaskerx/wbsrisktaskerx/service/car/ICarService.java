package com.wbsrisktaskerx.wbsrisktaskerx.service.car;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CarSpecificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.ActiveRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface ICarService {
    CarDetailResponse getCarDetail(int id);
    List<String> getAllVehicleType();
    Page<CarResponse> searchFilterCar(PagingRequest<SearchFilterCarRequest> request);
    Boolean deleteCarById(int id);
    Boolean addCar(AddCarRequest request);
    Boolean switchCarStatus(ActiveRequest request);
    Boolean updateCarSpecifications(Integer id, CarSpecificationsRequest request);
}
