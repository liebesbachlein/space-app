package app.service.jwt;

import app.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtUtil {
    @Getter
    private static String jwtExpiration;
    @Getter
    private static String jwtSecret;

    @Value("${jwt.secret}")
    public void setJwtSecret(String name) {
        jwtSecret = name;
    }

    @Value("${jwt.expiration-ms}")
    public void setJwtExpiration(String name) {
        jwtExpiration = name;
    }

    private static SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(getJwtSecret());
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    public static String generateToken(User user) {
        Boolean isAdmin = user.getRole() == User.Role.ROLE_ADMIN;
        String name = user.getName();
        Long id = user.getId();

        return Jwts.builder()
                .claim("id", id)
                .claim("role", user.getRole())
                .claim("name", name)
                .claim("admin", isAdmin)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Integer.parseInt(getJwtExpiration())))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public static Claims parseToken(String token) throws JwtException  {
        JwtParser parser = Jwts
                .parser()
                .verifyWith(key)
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }

    public static String extractEmail(String token) {
        return parseToken(token).getSubject();
    }

    public static boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}
