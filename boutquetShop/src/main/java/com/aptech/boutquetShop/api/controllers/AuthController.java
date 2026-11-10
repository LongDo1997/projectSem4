package com.aptech.boutquetShop.api.controllers;

import com.aptech.boutquetShop.repositories.UsersRepository;
import com.aptech.boutquetShop.service.impl.TokenBlacklistService;
import com.aptech.boutquetShop.service.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.boutquetShop.config.security.JWTProvider;
import com.aptech.boutquetShop.dto.UserAccountDTO.AuthResponseDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO.UserAccountRegDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO.UserAccountReqDTO;
import com.aptech.boutquetShop.utils.Constants;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/auth")
public class AuthController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private JWTProvider jwtProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsersService usersService;

	@Autowired
	TokenBlacklistService tokenBlacklistService;


	private final ConcurrentHashMap<String, Boolean> expiredTokensBlocklist = new ConcurrentHashMap<>();

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(UserAccountReqDTO loginReq) {
		UsernamePasswordAuthenticationToken stoken = new UsernamePasswordAuthenticationToken(loginReq.getUserName(), loginReq.getPassword());
		System.out.println(stoken.toString());
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUserName(), loginReq.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);

		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String tokenHeader) {
		try {
			if (tokenHeader == null ) {
				return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
			}
			if (tokenHeader.startsWith("Bearer ")) {
				tokenHeader =  tokenHeader.substring(7);
			}
			if(!jwtProvider.validateToken(tokenHeader)){
				return ResponseEntity.badRequest().body("JWT was exprired or incorrect");
			}
			tokenBlacklistService.blacklistToken(tokenHeader,jwtProvider.getExpirationFromJWT(tokenHeader));
			return ResponseEntity.ok("Logout successful. Token has been expired.");
        } catch (Exception e){
			return ResponseEntity.badRequest().body("Log Out Fail!\n" + e.toString());
		}
	}

	@PostMapping("register/admin")
	public ResponseEntity<String> register(UserAccountRegDTO registerDto) {
		try {
			usersService.reqRegistUserAdmin(registerDto);
        } catch (BadCredentialsException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("User registered success!", HttpStatus.OK);
	}

	@PostMapping("/register/customer")
	public ResponseEntity<String> registerCustomer(UserAccountRegDTO registerDto) {
		try {
			usersService.reqRegistUserCustomer(registerDto);
			return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký tài khoản thành công.");
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
