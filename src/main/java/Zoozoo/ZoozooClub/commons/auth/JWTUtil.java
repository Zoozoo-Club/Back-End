package Zoozoo.ZoozooClub.commons.auth;

import java.security.Key;
import java.util.Date;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
    private final String secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = secret;
    }
    public Claims getAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token).getBody();
        } catch(ExpiredJwtException e) {
            throw new ZoozooException(HttpStatus.BAD_REQUEST, "토큰이 만료됨");
        } catch(SignatureException e) {
            throw new ZoozooException(HttpStatus.BAD_REQUEST, "토큰 서명 이상");
        }
    }

    public String createToken(Long userId, Long clubId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 24 * 60 * 60 * 1000);

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            System.out.println("fdsfds");
            throw new ZoozooException(HttpStatus.BAD_REQUEST, "토큰 만료");
        } catch (JwtException e) {
            throw new ZoozooException(HttpStatus.BAD_REQUEST, "로그인 만료");
        }
    }

    public Long getUserId(String token) {
        return getAllClaims(token).get("userId", Long.class);
    }

    public boolean isExpired(String token) {
        try {
            return getAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

}