package org.example.init;


import org.example.model.User;
import org.example.Repository.UserRepository;
import org.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserInitializer {

    private static final Logger log = LoggerFactory.getLogger(UserInitializer.class);

    @Bean
    public ApplicationRunner initDemoUsers(UserService userService, UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByLogin("admin")) {
                log.info("Создаём демо-пользователя 'admin'...");

                userService.registerUser("admin", "admin123", "Админ", "Админов", "Админович");

                log.info("Демо-пользователь 'admin' успешно создан.");
            }
            if (!userRepository.existsByLogin("xarek")) {
                log.info("оздаем xarek");

                userService.registerUser("xarek","1234","Игорь", "Владимирович","Непокрытых");

                log.info("Создан пользователь xarek");
            }
            else {
                log.info("Демо-пользователь уже существует — инициализация пропущена.");
            }
        };
    }
}