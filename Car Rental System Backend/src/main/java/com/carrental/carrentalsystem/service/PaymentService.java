package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.dto.PaymentResponse;
import com.carrental.carrentalsystem.entity.Payment;
import com.carrental.carrentalsystem.entity.Rental;
import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.enums.PaymentStatus;
import com.carrental.carrentalsystem.exception.BadRequestException;
import com.carrental.carrentalsystem.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.messaging.NotificationProducer;
import com.carrental.carrentalsystem.repository.PaymentRepository;
import com.carrental.carrentalsystem.util.EntityDtoMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalService rentalService;
    private final NotificationProducer notificationProducer;
    private final EmailService emailService;

    @Transactional
    public PaymentResponse simulatePayment(Long rentalId) {
        Rental rental = rentalService.findEntityById(rentalId);
        if (rental.getTotalCost() == null) {
            throw new BadRequestException("Rental must be returned before payment can be processed");
        }

        Payment payment = paymentRepository.findByRentalId(rentalId)
                .orElseGet(() -> Payment.builder()
                        .rental(rental)
                        .amount(rental.getTotalCost())
                        .status(PaymentStatus.PENDING)
                        .paymentDate(LocalDateTime.now())
                        .build());

        payment.setAmount(rental.getTotalCost());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);

        User user = rental.getBooking().getUser();
        notificationProducer.send(user.getId(), "Payment completed for rental " + rentalId);
        
        String emailBody = "Dear " + user.getName() + ",\n\nYour payment of $" + savedPayment.getAmount() + " for rental " + rentalId + " has been successfully processed.";
        emailService.sendEmail(user.getEmail(), "Payment Confirmation", emailBody);

        return EntityDtoMapper.toPaymentResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse updateStatus(Long paymentId, PaymentStatus status) {
        Payment payment = findEntityById(paymentId);
        payment.setStatus(status);
        payment.setPaymentDate(LocalDateTime.now());
        return EntityDtoMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    public PaymentResponse getByRentalId(Long rentalId) {
        return paymentRepository.findByRentalId(rentalId)
                .map(EntityDtoMapper::toPaymentResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for rental id: " + rentalId));
    }

    public Payment findEntityById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
    }
}
