package com.externship.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"com.externship.appointment.Person_storage", 
                           "com.externship.appointment.Doctor_storage", 
                           "com.externship.appointment.Appointment_storage"})
@EnableJpaRepositories(basePackages = {"com.externship.appointment.Person_storage", 
                                     "com.externship.appointment.Doctor_storage", 
                                     "com.externship.appointment.Appointment_storage"})
@ComponentScan(basePackages = "com.externship.appointment")
public class AppointmentApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(AppointmentApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application failed to start: " + e.getMessage());
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/**")
                        .addResourceLocations("classpath:/static/", "classpath:/img/");
            }
        };
    }
}
