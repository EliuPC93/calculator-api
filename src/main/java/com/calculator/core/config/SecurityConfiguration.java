package com.calculator.core.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.calculator.core.config.props.JwtConfigurationProperties;
import com.calculator.core.jwt.*;
import com.calculator.core.repository.UserRepository;
import com.calculator.data.entity.SecurityRole;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtUserDetailService jwtUserDetailService;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailService);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().userDetailsService(jwtUserDetailService)
                .authorizeRequests().antMatchers("/", "/index", "/index.html", "/security/login", "/calculator-websocket").permitAll()
                .and()
                .authorizeRequests().antMatchers("/operations/*").hasAuthority(SecurityRole.ACTIVE.getRole())
                .and()
                .authorizeRequests().antMatchers("/records/*").hasAuthority(SecurityRole.ACTIVE.getRole())
                .and()
                .authorizeRequests().antMatchers("/users/*").permitAll()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtUtil, userRepository))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtUtil, userRepository, jwtConfigurationProperties))
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
