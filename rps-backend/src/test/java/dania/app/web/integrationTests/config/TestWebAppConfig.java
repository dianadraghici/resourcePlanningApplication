package dania.app.web.integrationTests.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.TestPropertySource;

@ComponentScan(basePackages = "dania.app.web")
@Configuration
@EnableWebSecurity
@TestPropertySource("classpath:application-test.properties")
public class TestWebAppConfig {
}
