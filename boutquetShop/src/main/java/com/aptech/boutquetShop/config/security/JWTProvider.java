package com.aptech.boutquetShop.config.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JWTProvider {
	private static final String secretString = "2ca3bf87ff4e2dc9f3c24c286451b9eff9528f137100caa19cd264f07516cd29";
	private static final SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
//	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + SecurityConstant.JWT_EXPIRATION);

		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expireDate).signWith(key, SignatureAlgorithm.HS512).compact();
		System.out.println("New token :");
		System.out.println(token);
		return token;
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			System.out.println(key.getAlgorithm());
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		}  catch (ExpiredJwtException e) {
			System.out.println("ExpiredJwtException");
			System.out.println(e.getMessage());
			return false;
		} catch (MalformedJwtException e) {
			System.out.println("MalformedJwtException");
			System.out.println(e.getMessage());
			return false;
		} catch (SignatureException e) {
			System.out.println("SignatureException");
			System.out.println(e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e.getMessage());
			return false;
		}catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect", ex.fillInStackTrace());
		}
	}
}