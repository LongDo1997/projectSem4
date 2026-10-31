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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
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

	private Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}

	private Users convertToObjectUserEntity(Object o) {
		return mapper.map(o, Users.class);
	}

}
