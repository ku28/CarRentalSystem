package com.carrental.carrentalsystem.repository;

import com.carrental.carrentalsystem.entity.Car;
import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("""
            select c from Car c
            where (:brand is null or lower(c.brand) like lower(concat('%', :brand, '%')))
              and (:model is null or lower(c.model) like lower(concat('%', :model, '%')))
              and (:category is null or lower(c.category) like lower(concat('%', :category, '%')))
              and (:availabilityStatus is null or c.availabilityStatus = :availabilityStatus)
            """)
    Page<Car> searchCars(@Param("brand") String brand,
                         @Param("model") String model,
                         @Param("category") String category,
                         @Param("availabilityStatus") AvailabilityStatus availabilityStatus,
                         Pageable pageable);
}
