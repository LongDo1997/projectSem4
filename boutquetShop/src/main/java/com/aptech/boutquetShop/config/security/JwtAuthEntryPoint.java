package com.aptech.boutquetShop.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		System.out.println("eqweqwe");
		System.out.println(authException.getClass().getSimpleName());
		response.addHeader("WWW-Authenticate", "");
		response.addHeader("WWW-Authenticates", "WWW-Authenticates");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().format("{\"error\":{\"message\":\"%s\"}}", authException.getMessage());
	}

}