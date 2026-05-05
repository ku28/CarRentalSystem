package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.dto.CarRequest;
import com.carrental.carrentalsystem.dto.CarResponse;
import com.carrental.carrentalsystem.dto.PageResponse;
import com.carrental.carrentalsystem.entity.Car;
import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import com.carrental.carrentalsystem.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.repository.CarRepository;
import com.carrental.carrentalsystem.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public CarResponse addCar(CarRequest request) {
        Car car = carRepository.save(mapToEntity(request, Car.builder().build()));
        return EntityDtoMapper.toCarResponse(car);
    }

    public CarResponse updateCar(Long id, CarRequest request) {
        Car car = findEntityById(id);
        mapToEntity(request, car);
        return EntityDtoMapper.toCarResponse(carRepository.save(car));
    }

    public void deleteCar(Long id) {
        carRepository.delete(findEntityById(id));
    }

    public PageResponse<CarResponse> getAllCars(int page, int size) {
        Page<Car> cars = carRepository.findAll(PageRequest.of(page, size));
        return EntityDtoMapper.toPageResponse(cars, EntityDtoMapper::toCarResponse);
    }

    public PageResponse<CarResponse> searchCars(String brand,
                                                String model,
                                                String category,
                                                AvailabilityStatus availabilityStatus,
                                                int page,
                                                int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Car> cars = carRepository.searchCars(brand, model, category, availabilityStatus, pageable);
        return EntityDtoMapper.toPageResponse(cars, EntityDtoMapper::toCarResponse);
    }

    public Car findEntityById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + id));
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    private Car mapToEntity(CarRequest request, Car car) {
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setCategory(request.getCategory());
        car.setPricePerDay(request.getPricePerDay());
        car.setAvailabilityStatus(request.getAvailabilityStatus());
        return car;
    }
}
