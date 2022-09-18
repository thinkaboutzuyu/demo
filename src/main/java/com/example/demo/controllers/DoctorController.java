package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.BookingDto;
import com.example.demo.dtos.InteractDto;
import com.example.demo.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final BookingService bookingService;

    @PostMapping("/booking/gets")
    public ResponseEntity<AbstractResponse> getBookingList() {
        return ResponseEntity.ok().body(bookingService.getBookingListOfDoctor());
    }

    @PostMapping("/booking/approve/{id}")
    public ResponseEntity<AbstractResponse> approveBooking(@PathVariable Integer id, @RequestBody InteractDto interactDto) {
        return ResponseEntity.ok().body(bookingService.approveOrDisapproveBooking(id, interactDto));
    }
}