package com.gitegg.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * GitEggBootApplication 启动类
 * @author GitEgg
 */
@ComponentScan(basePackages = {"com.gitegg"})
@MapperScan(basePackages  = {"com.gitegg.**.mapper.**"})
@SpringBootApplication
public class GitEggBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggBootApplication.class,args);
    }

}
