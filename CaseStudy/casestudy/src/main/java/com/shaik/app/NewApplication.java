package com.shaik.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by jabbars on 1/23/2017.
 */


@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.shaik","com.shaik.rest"})
@SpringBootApplication
public class NewApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(NewApplication.class, args);
    }
}
