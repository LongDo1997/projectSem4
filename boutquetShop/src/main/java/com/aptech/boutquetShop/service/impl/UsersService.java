package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.UserAccountDTO;
import com.aptech.boutquetShop.entities.Users;
import com.aptech.boutquetShop.repositories.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsersService {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	PasswordEncoder passwordEncoder;


	@Autowired
	private ModelMapper mapper;

	public Users reqRegistUserAdmin(UserAccountDTO.UserAccountRegDTO registerDto) throws UsernameNotFoundException {
		if (usersRepository.findByEmail(registerDto.getEmail()).isPresent()) {
			throw new BadCredentialsException("Username is taken!");
		}

		Users user = convertToObjectUserEntity(registerDto);
		user.setPasswordHash(passwordEncoder.encode((registerDto.getPassword())));
		user.setRole("Admin");

		return usersRepository.save(user);
	}

	public Users reqRegistUserCustomer(UserAccountDTO.UserAccountRegDTO registerDto) {
		if (registerDto == null
				|| !StringUtils.hasText(registerDto.getFirstName())
				|| !StringUtils.hasText(registerDto.getLastName())
				|| !StringUtils.hasText(registerDto.getEmail())
				|| !StringUtils.hasText(registerDto.getPassword())) {
			throw new IllegalArgumentException("Vui lòng nhập họ, tên, email và mật khẩu.");
		}

		String email = registerDto.getEmail().trim().toLowerCase(Locale.ROOT);
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException("Email không hợp lệ.");
		}
		if (registerDto.getPassword().length() < 8) {
			throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự.");
		}
		if (usersRepository.findByEmail(email).isPresent()) {
			throw new BadCredentialsException("Email đã được đăng ký.");
		}

		Users user = convertToObjectUserEntity(registerDto);
		user.setFirstName(registerDto.getFirstName().trim());
		user.setLastName(registerDto.getLastName().trim());
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(registerDto.getPassword()));
		user.setRole("Customer");
		user.setCreatedAt(null);

		return usersRepository.save(user);
	}

	private Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}

	private Users convertToObjectUserEntity(Object o) {
		return mapper.map(o, Users.class);
	}

}
