package dania.app.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication(scanBasePackages = { "dania.app.web" })
@PropertySource("classpath:application.${spring.profiles.active}.properties")
@EnableResourceServer
public class InitApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(InitApplication.class, args);
    }

}
