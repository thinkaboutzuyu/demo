package com.example.demo;

import com.cloudinary.Cloudinary;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new UsernameAuditorAware();
    }

    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary;
        Map config = new HashMap();
        config.put("cloud_name", "annb");
        config.put("api_key", "544826756355157");
        config.put("api_secret", "KOi-qJgeNjS8kuWTggHAydUtl3g");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    //	@Bean
//    CommandLineRunner runner(RoleRepository roleRepo, PostRepository postRepository, UserRepository userRepository) {
//		return args -> {
//			Role admin = new Role(1L, "admin");
//			Role doctor = new Role(2L, "doctor");
//			Role user = new Role(3L, "user");
//			roleRepo.save(admin);
//			roleRepo.save(doctor);
//			roleRepo.save(user);
//
//            User user1 = new User("exampleEmail@gmail.com", "$2a$12$Xh19B4FekngEuDuzZAF5v.8JnavUWhu7frbZHFvt1SP1c/8RLwaMq", "An", 22, 2, "0941506499", admin);
//            userRepository.save(user1);
//		};
//	}
    @Bean
    CommandLineRunner runner(RoleRepository roleRepo, UserRepository userRepository) {
        return args -> {
            Role admin = new Role(1, "admin");
            Role doctor = new Role(2, "doctor");
            Role user = new Role(3, "user");
            roleRepo.save(admin);
            roleRepo.save(doctor);
            roleRepo.save(user);

            if(userRepository.findByEmail("admin@gmail.com") == null){
                User user1 = new User("admin@gmail.com", "admin", admin);
                userRepository.save(user1);
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:1010");
            }
        };
    }
}
