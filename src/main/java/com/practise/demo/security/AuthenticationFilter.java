package com.practise.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practise.demo.dto.UserDetailsDTO;
import com.practise.demo.dto.UserRequestLogin;
import com.practise.demo.service.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UsersService usersService;
    private Environment environment;


    public AuthenticationFilter(AuthenticationManager authenticationManager, UsersService usersService, Environment environment) {
        super(authenticationManager);
        this.usersService = usersService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserRequestLogin userRequestLogin = null;
        try {
            userRequestLogin = new ObjectMapper().readValue(request.getInputStream(),
                    UserRequestLogin.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestLogin.getUserName(), userRequestLogin.getPassword()
                            , new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDetailsDTO userDetailsDTO = usersService.getUserById(username);
        Long tokenEpiration = Long.valueOf(environment.getProperty("token.expiration_time"));
        String tokenSecret = environment.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        String token = Jwts.builder().subject(userDetailsDTO.getUserId())
                .expiration(Date.from(Instant.now().plusSeconds(tokenEpiration)))
                .issuedAt(Date.from(Instant.now()))
                .signWith(secretKey)
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDetailsDTO.getUserId());
        //super.successfulAuthentication(request, response, chain, authResult);
    }
}
