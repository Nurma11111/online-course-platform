package com.bekzhanuly.courseplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Application Entry Point
 * Online Course Platform
 * Author: Bekzhanuly Nurmukhamed
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BekzhanulYNurmukhamedApplication {

    public static void main(String[] args) {
        SpringApplication.run(BekzhanulYNurmukhamedApplication.class, args);
    }
}
