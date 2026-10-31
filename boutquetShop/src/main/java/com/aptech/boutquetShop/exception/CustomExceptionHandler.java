package com.aptech.boutquetShop.exception;

import java.security.SignatureException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aptech.boutquetShop.utils.BaseResponse;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> handleSecurityException(Exception ex) {
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

		return new ResponseEntity<>(baseResponse, httpStatus);
	}

}
