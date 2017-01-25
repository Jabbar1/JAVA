package config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jabbars on 1/24/2017.
 */
@ComponentScan(basePackages = {"com.shaik.domain"})
@Configuration
@EnableAutoConfiguration
public class TestConfig {

}