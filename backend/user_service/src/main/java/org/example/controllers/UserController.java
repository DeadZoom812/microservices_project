package org.example.controllers;

import org.example.model.User;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(
                    request.getLogin(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getMiddleName()
            );
            return ResponseEntity.ok("Пользователь '" + request.getLogin() + "' успешно создан.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable Integer id) {
        boolean exists = userService.findById(id).isPresent();
        return ResponseEntity.ok(exists);
    }


    public static class RegisterRequest {
        private String login;
        private String password;
        private String firstName;
        private String lastName;
        private String middleName;

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getMiddleName() { return middleName; }
        public void setMiddleName(String middleName) { this.middleName = middleName; }
    }
}