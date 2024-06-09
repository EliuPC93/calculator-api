package com.ipalma.calculator.core.jwt;

import com.ipalma.calculator.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.ipalma.calculator.data.entity.User> user = userRepository.findByEmail(username);
        if (! user.isPresent()) {
            //find by username
            Optional<com.ipalma.calculator.data.entity.User> userByUsername = userRepository.findByEmail(username);
            if (userByUsername.isPresent()) {
                return new User(userByUsername.get().getEmail(), userByUsername.get().getPassword(),
                        roles("admin"));
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }
        } else {
            return new User(user.get().getEmail(), user.get().getPassword(),
                    roles("admin"));
        }

    }

    private List<SimpleGrantedAuthority> roles(String roles) {
        return com.ipalma.calculator.data.entity.SecurityRole.getRoles(roles).stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
