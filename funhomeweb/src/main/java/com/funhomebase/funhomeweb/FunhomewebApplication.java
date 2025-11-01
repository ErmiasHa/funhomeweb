package com.funhomebase.funhomeweb;

import com.funhomebase.funhomeweb.model.User;
import com.funhomebase.funhomeweb.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FunhomewebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunhomewebApplication.class, args);
    }

    @Bean
    CommandLineRunner createAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@funhome.se").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@funhome.se");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                repo.save(admin);
                System.out.println("✅ Admin skapad: admin@funhome.se / admin123");
            } else {
                System.out.println("ℹ️ Admin finns redan.");
            }
        };
    }
}
