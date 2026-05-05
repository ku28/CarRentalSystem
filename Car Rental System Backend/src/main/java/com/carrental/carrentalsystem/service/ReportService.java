package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.repository.BookingRepository;
import com.carrental.carrentalsystem.repository.PaymentRepository;
import com.carrental.carrentalsystem.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final RentalRepository rentalRepository;

    public ReportService(PaymentRepository paymentRepository, BookingRepository bookingRepository, RentalRepository rentalRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.rentalRepository = rentalRepository;
    }

    public Map<String, Object> getRevenueReport(String startDate, String endDate) {
        //  add logic to generate revenue report
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalRevenue", 10000.00);
        return report;
    }

    public Map<String, Object> getBookingsReport(String startDate, String endDate) {
        //  add logic to generate bookings report
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalBookings", 50);
        return report;
    }

    public Map<String, Object> getCarUsageReport() {
        //  add logic to generate car usage report
        Map<String, Object> report = new HashMap<>();
        report.put("totalCars", 20);
        report.put("rentedCars", 15);
        report.put("availableCars", 5);
        return report;
    }
}
