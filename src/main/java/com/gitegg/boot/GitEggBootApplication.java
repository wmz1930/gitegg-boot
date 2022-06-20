package com.gitegg.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * GitEggBootApplication 启动类
 * @author GitEgg
 */
@ComponentScan(basePackages = "com.gitegg")
@SpringBootApplication
public class GitEggBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggBootApplication.class,args);
    }

}
