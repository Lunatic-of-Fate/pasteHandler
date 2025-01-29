package org.lunatic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
        SpringApplication.run(Main.class, args);

    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Разрешить все домены
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Разрешенные методы
                        .allowedHeaders("*"); // Разрешенные заголовки
            }
        };
    }
}