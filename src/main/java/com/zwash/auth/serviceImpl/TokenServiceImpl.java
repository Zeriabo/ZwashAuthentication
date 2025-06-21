package com.zwash.auth.serviceImpl;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.zwash.auth.security.JwtUtils;
import com.zwash.auth.service.TokenService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;




@Service
public class TokenServiceImpl implements TokenService {


	private  String SECRET_KEY=JwtUtils.SECRET_KEY;

	  private Key getSigningKey() {

	        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

	@Override
	public String createJWT(String id, String issuer, String subject, long ttlMillis) throws Exception {


	    Key key = getSigningKey();


	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);

	    JwtBuilder builder = Jwts.builder()
	    							.id(id)
	                                .issuedAt(now)
	                                .subject(subject)
	                                .issuer(issuer)
	                                .signWith(key);

	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.expiration(exp);
	    }


	    return builder.compact();
	}

}
