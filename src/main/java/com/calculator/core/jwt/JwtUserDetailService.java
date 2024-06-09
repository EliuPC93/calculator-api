package com.calculator.core.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.calculator.core.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.calculator.data.entity.User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            return new User(userByUsername.get().getUsername(), userByUsername.get().getPassword(),
                    roles(userByUsername.get().getAuthenticationDetail().getRoles()));
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }

    private List<SimpleGrantedAuthority> roles(String roles) {
        return com.calculator.data.entity.SecurityRole.getRoles(roles).stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
