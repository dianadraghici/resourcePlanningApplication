package dania.app.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new BadCredentialsException("Authentication method not supported: " + request.getMethod());
        }
        LoginDto loginDto;
        try {
            loginDto = OBJECT_MAPPER.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Could not read user credentials from request");
        }

        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUser(), loginDto.getCredential()));
    }
}