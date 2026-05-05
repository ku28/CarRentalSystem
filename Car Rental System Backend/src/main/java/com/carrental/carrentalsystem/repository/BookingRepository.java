package com.carrental.carrentalsystem.repository;

import com.carrental.carrentalsystem.entity.Booking;
import com.carrental.carrentalsystem.enums.BookingStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    boolean existsByCarIdAndStatusIn(Long carId, List<BookingStatus> statuses);
}
