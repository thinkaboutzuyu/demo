package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.BookingService;
import com.example.demo.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SessionServiceImpl sessionService;
    private final BookingRepository bookingRepository;

    @Override
    public AbstractResponse booking(BookingDto bookingDto) {

        User doctor = userRepository.findUserById(bookingDto.getDoctorId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        if(doctor == null){
            return new AbstractResponse("FAILED", "DOCTOR_NOT_FOUND", 404);
        }

        Booking booking = new Booking(user, doctor, bookingDto.getBookedAt(), bookingDto.getBookedTime(), bookingDto.getContent());

        bookingRepository.save(booking);

        return new AbstractResponse();
    }
}

