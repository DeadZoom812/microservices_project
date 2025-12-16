package org.example.hotel_service.init;

import org.example.hotel_service.model.Booking;
import org.example.hotel_service.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class BookingInitializer {

    private static final Logger log = LoggerFactory.getLogger(BookingInitializer.class);

    @Bean
    public ApplicationRunner initDemoBookings(BookingRepository bookingRepository) {
        return args -> {
            if (bookingRepository.count() == 0) {
                log.info("Создаём демо-бронирования...");

                // Бронь для пользователя id=1 (admin) на комнату id=1 (101)
                Booking booking1 = new Booking();
                booking1.setUserId(1);
                booking1.setRoomId(1);
                booking1.setCheckIn(LocalDate.now());
                booking1.setCheckOut(LocalDate.now().plusDays(3));
                bookingRepository.save(booking1);

                // Вторая бронь
                Booking booking2 = new Booking();
                booking2.setUserId(1);
                booking2.setRoomId(2);
                booking2.setCheckIn(LocalDate.now().plusDays(5));
                booking2.setCheckOut(LocalDate.now().plusDays(7));
                bookingRepository.save(booking2);

                log.info("Создано 2 демо-бронирования.");
            } else {
                log.info("Демо-бронирования уже существуют — пропускаем инициализацию.");
            }
        };
    }
}