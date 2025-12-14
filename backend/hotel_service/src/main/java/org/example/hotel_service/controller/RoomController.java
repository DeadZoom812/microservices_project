package org.example.hotel_service.controller;

import org.example.hotel_service.model.Room;
import org.example.hotel_service.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Room> getAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        return roomService.findAvailableRooms(checkIn, checkOut);
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable Integer id) {
        return roomService.findById(id);
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}/availability")
    public Map<String, Object> getRoomAvailability(@PathVariable Integer id) {
        return roomService.getRoomAvailability(id);
    }
}
