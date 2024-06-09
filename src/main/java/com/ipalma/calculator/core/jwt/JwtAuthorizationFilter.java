package com.ipalma.calculator.core.jwt;

import com.ipalma.calculator.core.config.props.JwtConfigurationProperties;
import com.ipalma.calculator.core.repository.UserRepository;
import com.ipalma.calculator.data.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.Filter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private JwtConfigurationProperties jwtConfigurationProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                                  UserRepository userRepository, JwtConfigurationProperties jwtConfigurationProperties) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.jwtConfigurationProperties = jwtConfigurationProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(jwtConfigurationProperties.getAuthHeader());

        if (header == null || !header.startsWith(jwtConfigurationProperties.getBearer())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtConfigurationProperties.getAuthHeader());

        if (token != null) {
            // parse the token.
            String user = jwtUtil.getUsernameForToken(token.substring(7));
            Optional<User> optionalUserByUsername = userRepository.findByEmail(user);
            if (user != null && optionalUserByUsername.isPresent()) {
                // new arraylist means authorities
                return new UsernamePasswordAuthenticationToken(user, null);
            }

            return null;
        }

        return null;
    }

}
