package com.ipalma.calculator.core.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipalma.calculator.core.repository.UserRepository;
import com.ipalma.calculator.data.entity.User;
import com.ipalma.calculator.data.request.AuthenticationRequest;
import com.ipalma.calculator.data.response.AuthenticationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String LOGIN_PATH = "/security/login";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl(LOGIN_PATH);
    }

    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AuthenticationRequest credentials = OBJECT_MAPPER
                    .readValue(req.getInputStream(), AuthenticationRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = jwtUtil.generateToken((UserDetails)auth.getPrincipal());

        User foundUser = userRepository.findByEmail(((UserDetails) auth.getPrincipal()).getUsername()).get();
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .jwt(token).build();
        String body = new ObjectMapper().writeValueAsString(authenticationResponse);
        res.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(body);
        res.getWriter().flush();
    }
}
