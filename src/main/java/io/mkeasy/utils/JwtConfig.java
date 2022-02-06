package io.mkeasy.utils;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class JwtConfig {
    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;
    
    public JwtConfig() {
    	this.Uri = "/auth/**";
    	this.header = "Authorization";
    	this.prefix = "Bearer ";
    	this.expiration = 24*60*60;
    	this.secret = "JwtSecretKey";
    }
	
}
