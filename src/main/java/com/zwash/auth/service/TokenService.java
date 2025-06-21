package com.zwash.auth.service;

import org.springframework.stereotype.Service;

@Service
public interface TokenService {


	public  String createJWT(String id, String issuer, String subject, long ttlMillis) throws Exception;



}
