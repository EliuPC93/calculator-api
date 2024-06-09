package com.calculator.core.jwt;

import com.calculator.core.repository.UserRepository;
import com.calculator.data.entity.SecurityRole;
import com.calculator.data.entity.User;
import com.calculator.data.request.AuthenticationRequest;
import com.calculator.data.response.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Override
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

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = jwtUtil.generateToken((UserDetails)auth.getPrincipal());

        User foundUser = userRepository.findByUsername(((UserDetails) auth.getPrincipal()).getUsername()).get();
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .jwt(token)
                .roles(SecurityRole.getRoles(foundUser.getAuthenticationDetail().getRoles())).build();
        String body = new ObjectMapper().writeValueAsString(authenticationResponse);
        res.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(body);
        res.getWriter().flush();
    }
}
