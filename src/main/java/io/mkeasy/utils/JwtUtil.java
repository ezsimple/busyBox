package io.mkeasy.utils;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

    public static String generateToken(Authentication auth, SignatureAlgorithm signatureAlgorithm, JwtConfig jwtConfig) {

		Long now = System.currentTimeMillis();
		
		String token = Jwts.builder()
				.setSubject(auth.getName())	
				// Convert to list of strings. 
				// This is important because it affects the way we get them back in the Gateway.
				.claim("authorities", auth.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
				.signWith(signatureAlgorithm, jwtConfig.getSecret())
				.compact();

        return token;
    }

    public static Claims getClaims(String token, JwtConfig jwtConfig){

		Claims claims = Jwts.parser()
				.setSigningKey(jwtConfig.getSecret())
				.parseClaimsJws(token)
				.getBody();

		return claims;
    }

//    public static String getSubject(HttpServletRequest request, String cookieName, String signingKey){
//        String token = CookieUtil.getValue(request, cookieName);
//        if(token == null) 
//        	return null;
//
//        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
//    }

}
