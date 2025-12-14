package org.example.hotel_service.controller;

import org.example.hotel_service.model.Booking;
import org.example.hotel_service.repository.BookingRepository;
import org.example.hotel_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody CreateBookingRequest request) {
        if (bookingRepository.existsByRoomIdAndDates(
                request.getRoomId(), request.getCheckIn(), request.getCheckOut()) > 0) {
            return ResponseEntity.badRequest().body("Номер уже забронирован на эти даты");
        }
        try {
            bookingService.createBooking(
                    request.getUserId(),
                    request.getRoomId(),
                    request.getCheckIn(),
                    request.getCheckOut()
            );
            return ResponseEntity.ok("Бронирование успешно создано");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/room/{roomId}")
    public List<Booking> getBookingsByRoom(@PathVariable Integer roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public static class CreateBookingRequest {
        private Integer userId;
        private Integer roomId;
        private LocalDate checkIn;
        private LocalDate checkOut;

        public Integer getUserId() {
            return userId;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public LocalDate getCheckIn() {
            return checkIn;
        }

        public LocalDate getCheckOut() {
            return checkOut;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        public void setCheckIn(LocalDate checkIn) {
            this.checkIn = checkIn;
        }

        public void setCheckOut(LocalDate checkOut) {
            this.checkOut = checkOut;
        }
    }
}
