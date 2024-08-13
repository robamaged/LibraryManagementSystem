package com.example.library.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    public static final String SECRET_KEY="Hqj4ZSrMAhMvjEpAQMQb2FiHqeMNmJi7mvT5xArPVzdhVfYHvqL2ZnwWzjJ+mbb2";

    public String extractUsername(String token) {///the email
        return extractClaim(token,Claims::getSubject);
    }
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSigningKey()).
                build().
                parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails) {
        return   generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(Map<String,Object> extraClaims,

                                UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);

    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
