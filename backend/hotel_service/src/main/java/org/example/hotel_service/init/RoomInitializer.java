package org.example.hotel_service.init;

import org.example.hotel_service.model.Booking;
import org.example.hotel_service.model.Room;
import org.example.hotel_service.repository.BookingRepository;
import org.example.hotel_service.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomInitializer {

    private static final Logger log = LoggerFactory.getLogger(RoomInitializer.class);

    @Bean
    public ApplicationRunner initDemoRooms(RoomRepository roomRepository) {
        return args -> {
            if (roomRepository.count() == 0) {
                log.info("Добавление демо-комнат...");

                roomRepository.save(new Room("101", "AVAILABLE", "https://media-cdn.tripadvisor.com/media/photo-s/07/81/1f/65/classic.jpg"));
                roomRepository.save(new Room("102", "AVAILABLE", "https://360travel.by/images/gallery/hotels/542/19953131.jpg"));

                log.info("Демо-комнаты успешно добавлены.");
            } else {
                log.info("Демо-данные уже существуют — инициализация пропущена.");
            }

        };
    }
}