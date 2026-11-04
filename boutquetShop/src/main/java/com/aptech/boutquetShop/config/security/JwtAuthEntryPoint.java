package com.aptech.boutquetShop.config.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
	private final HandlerExceptionResolver resolver;

	// Inject the standard handler exception resolver bean
	public JwtAuthEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		System.out.println("eqweqwe");
		System.out.println(authException.getClass().getSimpleName());
		System.out.println(authException.getMessage());
//		response.addHeader("WWW-Authenticate", "");
//		response.addHeader("WWW-Authenticates", "WWW-Authenticates");
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
//		response.getWriter().format("{\"error\":{\"message\":\"%s\"}}", authException.getMessage());
//
//		resolver.resolveException(request, response, null, authException);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", authException.getMessage());
		body.put("path", request.getServletPath());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}

}