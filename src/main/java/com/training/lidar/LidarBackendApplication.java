package com.training.lidar;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.SpringApplication;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LidarBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LidarBackendApplication.class, args);
    }
}
