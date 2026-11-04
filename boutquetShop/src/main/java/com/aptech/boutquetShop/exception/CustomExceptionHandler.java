package com.aptech.boutquetShop.exception;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aptech.boutquetShop.utils.BaseResponse;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> handleSecurityException(Exception ex, HttpServletRequest request) {
		System.out.println("22222222");
		System.out.println(ex);

		BaseResponse baseResponse = new BaseResponse();
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		baseResponse.setTrace(ex.fillInStackTrace().toString());
		if (ex instanceof BadCredentialsException) {
			baseResponse.setMessage("Authentication Failure");
			baseResponse.setCode(401);

			httpStatus = HttpStatus.UNAUTHORIZED;
		}

		if (ex instanceof AccessDeniedException) {
			baseResponse.setMessage("not_authorized");
			baseResponse.setCode(403);
			httpStatus = HttpStatus.FORBIDDEN;
		}

		if (ex instanceof SignatureException) {
			baseResponse.setMessage("JWT Signature not valid");
			baseResponse.setCode(403);
			httpStatus = HttpStatus.FORBIDDEN;
		}
		if (ex instanceof ExpiredJwtException) {
			baseResponse.setMessage("JWT Token already expired !");
			baseResponse.setCode(403);
			httpStatus = HttpStatus.FORBIDDEN;
		}

		if (ex instanceof AuthenticationCredentialsNotFoundException) {
			baseResponse.setMessage("JWT was exprired or incorrect");
			baseResponse.setCode(403);
			httpStatus = HttpStatus.FORBIDDEN;
		}

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("error", "Unauthorized");
		body.put("message", ex.getMessage());
		body.put("path", request.getServletPath());

		baseResponse.setStatus(httpStatus.value());
		baseResponse.setError(httpStatus.getReasonPhrase());
		baseResponse.setPath(request.getServletPath());
		return ResponseEntity.status(httpStatus).body(baseResponse);
		//return new ResponseEntity<>(baseResponse, httpStatus);
	}

}
