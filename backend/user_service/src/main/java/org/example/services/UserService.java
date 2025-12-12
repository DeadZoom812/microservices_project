package org.example.services;
import org.example.model.User;
import org.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String login, String rawPassword, String firstName, String lastName, String middleName) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("Пользователь с логином '" + login + "' уже существует");
        }

        User user = new User();
        user.setLogin(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setPasswordHash(passwordEncoder.encode(rawPassword)); // хэшируем пароль!
        return userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
}