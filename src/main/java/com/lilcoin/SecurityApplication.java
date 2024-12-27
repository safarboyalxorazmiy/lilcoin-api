package com.lilcoin;

import com.lilcoin.auth.AuthenticationService;
import com.lilcoin.auth.RegisterRequest;
import com.lilcoin.level.levelType.LevelTypeEntity;
import com.lilcoin.level.levelType.LevelTypeRepository;
import com.lilcoin.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
    AuthenticationService service,
    LevelTypeRepository levelTypeRepository
  ) {
    return args -> {
      var admin = RegisterRequest.builder()
        .firstname("Admin")
        .lastname("Admin")
        .email("admin@mail.com")
        .password("password")
        .role(Role.ADMIN)
        .build();
      System.out.println("Admin token: " + service.register(admin).getAccessToken());

      var manager = RegisterRequest.builder()
        .firstname("Admin")
        .lastname("Admin")
        .email("manager@mail.com")
        .password("password")
        .role(Role.MANAGER)
        .build();
      System.out.println("Manager token: " + service.register(manager).getAccessToken());

      String[] levels = {"Beginner", "Basic", "Average", "Trained", "Skilled", "Expert", "Master", "Epic", "Legendary", "God"};

      int i = 1;
      for (String level : levels) {
        LevelTypeEntity levelType = new LevelTypeEntity();
        levelType.setId(i);
        levelType.setLevelTitle(level);
        levelType.setLevelPrice(6L);
        levelTypeRepository.save(levelType);
        i++;
      }
    };
  }
}
