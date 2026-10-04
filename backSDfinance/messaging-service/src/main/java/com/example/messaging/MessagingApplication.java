package com.example.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import com.example.messaging.service.DiscussionService;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.messaging", "com.example.common"})
public class MessagingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessagingApplication.class, args);
    }

    // Ensure global discussion exists at startup
    @Bean
    public CommandLineRunner initGlobalDiscussion(DiscussionService discussionService) {
        return args -> {
            discussionService.ensureGlobalDiscussion();
            System.out.println("Global discussion ensured.");
        };
    }
}
