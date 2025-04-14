package com.student.management.controller;

import com.student.management.model.User;
import com.student.management.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SessionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session
    ) {
        Optional<User> optionalUser = userRepository.findByName(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }

        // Cria uma sessão e armazena o nome do usuário
        session.setAttribute("username", user.getName());
        return ResponseEntity.ok("Login realizado com sucesso");
    }

    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Object username = session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(401).body("Não autenticado");
        }
        return ResponseEntity.ok(username);
    }
}
