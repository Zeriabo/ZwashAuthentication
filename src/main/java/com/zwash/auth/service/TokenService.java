package com.zwash.auth.service;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public interface TokenService {


	public  String createJWT(String id, String issuer, String subject, long ttlMillis) throws Exception;



}
