package com.zwash.auth.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


@Component
public class JwtUtils {

	public JwtUtils()
	{

	}
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);



	public String createJWT(String id, String issuer, String subject, long ttlMillis) throws Exception {

	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	//    SecretKey secret = CryptoService.generateSecretKey(); secret.getEncoded().toString()

	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);

	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("TOKEN");
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey);

	    logger.debug("builder created: "+builder);
	    //if it has been specified, let's add the expiration
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	        logger.debug("expiration : "+exp);
	    }


	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}



	public  Claims verifyJWT(String jwt) throws  ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException  {

	    //This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parserBuilder()
			    .setSigningKey("sdTOKEN") // secret key used to sign the token
			    .build()
			    .parseClaimsJws(jwt) // parses the JWT and verifies the signature
			    .getBody(); // retrieves the Claims (payload)
     logger.debug("creating claims: "+claims);

	    return claims;
	}
}