package com.ipalma.calculator.core.jwt;

import com.ipalma.calculator.core.config.props.JwtConfigurationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JwtUtil {

    private JwtConfigurationProperties jwtConfigurationProperties;

    public String getUsernameForToken(String token) {
        return getClaimsForToken(token, Claims::getSubject);
    }

    private <T> T getClaimsForToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsForToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsForToken(String token) {
        return Jwts.parser().setSigningKey(jwtConfigurationProperties.getSecret()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimsForToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfigurationProperties.getValidity() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtConfigurationProperties.getSecret()).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameForToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
