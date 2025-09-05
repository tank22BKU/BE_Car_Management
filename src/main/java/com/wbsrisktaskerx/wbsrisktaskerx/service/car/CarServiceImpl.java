package com.wbsrisktaskerx.wbsrisktaskerx.service.car;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.Car;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.QCar;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.CarStructMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.ActiveRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CarSpecificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CarJpaQueryRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements ICarService {

    private final CarRepository carRepository;
    private final CarJpaQueryRepository carJpaQueryRepository;
    private final CarStructMapper carStructMapper;
    private final QCar qCar = QCar.car;

    public CarServiceImpl(
            CarRepository carRepository,
            CarJpaQueryRepository carJpaQueryRepository, CarStructMapper carStructMapper) {
        this.carJpaQueryRepository = carJpaQueryRepository;
        this.carRepository = carRepository;
        this.carStructMapper = carStructMapper;
    }

    @Override
    public CarDetailResponse getCarDetail(int id) {
        return carJpaQueryRepository.getCarDetail(id);
    }

    @Override
    public List<String> getAllVehicleType() {
        if (carRepository.findAll().isEmpty()) {
            return new ArrayList<>();
        }
        return carJpaQueryRepository.getAllVehicleType();
    }

    @Override
    public Page<CarResponse> searchFilterCar(PagingRequest<SearchFilterCarRequest> request) {
        return carJpaQueryRepository.getSearchFilterResult(request, qCar);
    }

    @Override
    public Boolean deleteCarById(int id) {
        Car car = carJpaQueryRepository.getCarById(id);
        car.setIsDeleted(Boolean.TRUE);
        carRepository.save(car);
        return Boolean.TRUE;
    }

    @Override
    public Boolean addCar(AddCarRequest request) {
        Car newCar = carStructMapper.toCar(request);
        carRepository.save(newCar);
        return Boolean.TRUE;
    }

    @Override
    public Boolean switchCarStatus(ActiveRequest request) {
        Car car = carJpaQueryRepository.getCarById(request.getId());
        car.setIsActive(request.getIsActive());
        carRepository.save(car);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateCarSpecifications(Integer id, CarSpecificationsRequest request) {
        Car car = carJpaQueryRepository.getCarById(id);
        carStructMapper.updateCarSpecifications(car.getCarDetail(), request);
        carRepository.save(car);
        return Boolean.TRUE;
    }
}
