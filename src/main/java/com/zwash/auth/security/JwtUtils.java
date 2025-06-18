package com.zwash.auth.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public static final String SECRET_KEY="GZcRgPBNHHtXgcu3f2dJZzMKtJcZKN5V2UhfH1KrfmQ=";

    private SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));;
//	private  String SECRET_KEY=JwtUtils.SECRET_KEY;;
//
//	  private Key getSigningKey() {
//		  
//	        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//	        return Keys.hmacShaKeyFor(keyBytes);
//	    }
    @PostConstruct
    public void init() {
        if (SECRET_KEY == null || SECRET_KEY.isBlank()) {
            throw new IllegalArgumentException("JWT secret must not be null or blank");
        }
    }

    public String createJWT(String id, String issuer, String subject, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .id(id)
                .issuedAt(now)
                .subject(subject)
                .issuer(issuer)
                .signWith(signingKey);
        	
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            builder.expiration(new Date(expMillis));
        }

        String token = builder.compact();
        logger.debug("JWT created: {}", token);
        return token;
    }

    public Claims verifyJWT(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, IllegalArgumentException {
    	 this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        JwtParser parser = Jwts.parser()
        	      .verifyWith(signingKey)
                .build();

        Jws<Claims> jwsClaims = parser.parseSignedClaims(jwt);
        Claims claims = jwsClaims.getPayload();
        logger.debug("JWT claims: {}", claims);
        return claims;
    }
}
