package dania.app.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Value("${cors.origin}")
    private String ngClientCorsOrigin;

    public WebSecurityConfig() {
        super();
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter() throws Exception {
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
        userAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        userAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        userAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
        });
        userAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        return userAuthenticationFilter;
    }

    @Bean
    public UserAuthenticationProviderRO userAuthenticationProvider() {
        return new UserAuthenticationProviderRO();
    }

    @Override
    @Resource
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder
                .authenticationProvider(userAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/version").permitAll()
                .anyRequest()
//                .authenticated()
//                .and()
//
//                .formLogin()
//                .usernameParameter("user")
//                .passwordParameter("credential")
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .httpBasic()
//                .and()
//
//                .addFilterBefore(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//
//                .logout()
//                .logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
//                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {})
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ngClientCorsOrigin)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.AUTHORIZATION)
                .maxAge(3600);
    }
}