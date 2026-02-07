package banking.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@Configuration
@ComponentScan("banking.application")
@PropertySource("classpath:application.properties")
public class AppConfiguration {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}
