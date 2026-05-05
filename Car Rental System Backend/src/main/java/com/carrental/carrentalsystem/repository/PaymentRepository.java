package com.carrental.carrentalsystem.repository;

import com.carrental.carrentalsystem.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRentalId(Long rentalId);
}
