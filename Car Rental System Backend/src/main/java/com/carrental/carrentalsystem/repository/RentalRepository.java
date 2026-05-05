package com.carrental.carrentalsystem.repository;

import com.carrental.carrentalsystem.entity.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental> findByBookingId(Long bookingId);

    List<Rental> findByReturnTimeIsNull();
}
