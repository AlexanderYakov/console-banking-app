package banking.application.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("banking.application")
@PropertySource("classpath:application.properties")
public class AppConfiguration {
}
