package com.school.warehouse.config;

import com.school.warehouse.entity.*;
import com.school.warehouse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Admin yaradılır
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Sistem Administratoru")
                    .email("admin@school.az")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Admin yaradıldı → username: admin | password: admin123");
        }

        // Direktor yaradılır
        if (!userRepository.existsByUsername("direktor")) {
            User director = User.builder()
                    .username("direktor")
                    .password(passwordEncoder.encode("direktor123"))
                    .fullName("Məktəb Direktoru")
                    .email("director@school.az")
                    .role(Role.DIRECTOR)
                    .build();
            userRepository.save(director);
            System.out.println("✅ Direktor yaradıldı → username: direktor | password: direktor123");
        }

        // Anbar otağı yaradılır
        if (!roomRepository.existsByName("Anbar")) {
            Room warehouse = Room.builder()
                    .name("Anbar")
                    .description("Əsas anbar otağı")
                    .build();
            roomRepository.save(warehouse);
            System.out.println("✅ Anbar otağı yaradıldı");
        }
    }
}