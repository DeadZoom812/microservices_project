package org.example.hotel_service.service;

import jakarta.transaction.Transactional;
import org.example.hotel_service.model.Booking;
import org.example.hotel_service.repository.BookingRepository;
import org.example.hotel_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RestTemplate restTemplate;

    private boolean userExists(Integer userId) {
        try {
            String url = "http://user_service:8081/api/users/exists/" + userId;
            restTemplate.getForObject(url, Boolean.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void createBooking(Integer userId, Integer roomId, LocalDate checkIn, LocalDate checkOut) {

        if (!isRoomAvailable(roomId, checkIn, checkOut)) {
            throw new RuntimeException("Номер уже забронирован на эти даты");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);

        bookingRepository.save(booking);

    }

    private boolean isRoomAvailable(Integer roomId, LocalDate checkIn, LocalDate checkOut) {
        return bookingRepository.existsByRoomIdAndDates(roomId, checkIn, checkOut) == 0;
    }
}
