package com.carrental.carrentalsystem.messaging;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingMessageDTO implements Serializable {

    private Long bookingId;
    private Long userId;
    private Long carId;
    private String message;
}
