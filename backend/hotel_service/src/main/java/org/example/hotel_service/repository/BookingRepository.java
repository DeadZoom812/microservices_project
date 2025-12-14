package org.example.hotel_service.repository;

import org.example.hotel_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByRoomId(Integer roomId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.roomId = :roomId AND " +
            "NOT (b.checkOut <= :checkIn OR b.checkIn >= :checkOut)")
    int existsByRoomIdAndDates(@Param("roomId") Integer roomId,
                               @Param("checkIn") LocalDate checkIn,
                               @Param("checkOut") LocalDate checkOut);
}
