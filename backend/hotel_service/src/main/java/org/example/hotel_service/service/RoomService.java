package org.example.hotel_service.service;

import org.example.hotel_service.model.Booking;
import org.example.hotel_service.model.Room;
import org.example.hotel_service.repository.BookingRepository;
import org.example.hotel_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> allRooms = roomRepository.findAll();
        return allRooms.stream().filter(room -> {
            List<Booking> bookings = bookingRepository.findByRoomId(room.getId());
            return bookings.stream().noneMatch(b ->
                    !b.getCheckOut().isBefore(checkIn) && !b.getCheckIn().isAfter(checkOut)
            );
        }).toList();
    }

    public Room findById(Integer id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Map<String, Object>> getAllRooms() {
        LocalDate today = LocalDate.now();
        return roomRepository.findAll().stream()
                .map(room -> buildRoomResponse(room, today))
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildRoomResponse(Room room, LocalDate today) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", room.getId());
        response.put("roomNumber", room.getRoomNumber());
        response.put("imageUrl", room.getImageUrl());

        boolean isOccupiedToday = bookingRepository.findByRoomId(room.getId()).stream()
                .anyMatch(b -> !b.getCheckIn().isAfter(today) && !b.getCheckOut().isBefore(today));

        if (isOccupiedToday) {
            response.put("status", "OCCUPIED");

            LocalDate nextFree = bookingRepository.findByRoomId(room.getId()).stream()
                    .filter(b -> !b.getCheckOut().isBefore(today))
                    .map(Booking::getCheckOut)
                    .min(LocalDate::compareTo)
                    .map(d -> d.plusDays(1))
                    .orElse(null);
            response.put("nextAvailableDate", nextFree);
        } else {
            response.put("status", "AVAILABLE");
            response.put("nextAvailableDate", null);
        }

        return response;
    }

    public Map<String, Object> getRoomAvailability(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        Map<String, Object> response = new HashMap<>();
        response.put("id", room.getId());
        response.put("roomNumber", room.getRoomNumber());
        response.put("imageUrl", room.getImageUrl());

        LocalDate today = LocalDate.now();
        List<Booking> futureBookings = bookingRepository.findByRoomId(roomId).stream()
                .filter(b -> !b.getCheckOut().isBefore(today))
                .collect(Collectors.toList());

        if (futureBookings.isEmpty()) {
            response.put("status", "AVAILABLE");
            response.put("nextAvailableDate", null);
        } else {
            response.put("status", "OCCUPIED");

            LocalDate lastCheckout = futureBookings.stream()
                    .map(Booking::getCheckOut)
                    .max(LocalDate::compareTo)
                    .orElse(today);
            response.put("nextAvailableDate", lastCheckout.plusDays(1));
        }

        return response;
    }
}
