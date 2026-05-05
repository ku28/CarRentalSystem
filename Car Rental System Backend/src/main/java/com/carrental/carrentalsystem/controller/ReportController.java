package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReport(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        return ResponseEntity.ok(reportService.getRevenueReport(startDate, endDate));
    }

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getBookingsReport(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        return ResponseEntity.ok(reportService.getBookingsReport(startDate, endDate));
    }

    @GetMapping("/usage")
    public ResponseEntity<Map<String, Object>> getCarUsageReport() {
        return ResponseEntity.ok(reportService.getCarUsageReport());
    }
}
