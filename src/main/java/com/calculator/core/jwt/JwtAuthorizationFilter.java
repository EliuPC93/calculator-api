package com.calculator.core.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.calculator.core.config.props.JwtConfigurationProperties;
import com.calculator.core.repository.UserRepository;
import com.calculator.data.entity.SecurityRole;
import com.calculator.data.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            Optional<User> optionalUserByUsername = userRepository.findByUsername(user);
            if (user != null && optionalUserByUsername.isPresent()) {
                // new arraylist means authorities
                return new UsernamePasswordAuthenticationToken(user, null, getAuthorities(optionalUserByUsername.get()));
            }

            return null;
        }

        return null;
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        return SecurityRole.getRoles(user.getAuthenticationDetail().getRoles()).stream()
                .map(sr -> new SimpleGrantedAuthority(sr)).collect(Collectors.toList());
    }
}
