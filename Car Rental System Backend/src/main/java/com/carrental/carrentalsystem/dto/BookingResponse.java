package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.BookingStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingResponse {

    private Long id;
    private Long userId;
    private Long carId;
    private String carName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
}
