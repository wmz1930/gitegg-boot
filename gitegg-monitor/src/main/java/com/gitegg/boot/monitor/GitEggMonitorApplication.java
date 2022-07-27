package com.gitegg.boot.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author GitEgg
 * @date 2022/7/16
 */

@EnableAdminServer
@SpringBootApplication
public class GitEggMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitEggMonitorApplication.class, args);
    }
}
