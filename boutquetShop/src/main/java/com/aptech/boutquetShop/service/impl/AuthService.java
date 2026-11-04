package com.aptech.boutquetShop.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.aptech.boutquetShop.dto.UserAccountDTO;
import com.aptech.boutquetShop.entities.Users;
import com.aptech.boutquetShop.repositories.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aptech.boutquetShop.dto.UserAccountDTO.UserAccountReqDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO.UserAccountResultDTO;

@Service
public class AuthService implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccountReqDTO userAccountReqDTO = new UserAccountReqDTO();
		userAccountReqDTO.setUserName(username);
		UserAccountResultDTO user = usersRepository.getUserInfor(userAccountReqDTO);
		if(user == null){
			return  null;
		}
//		String roles = user.getRoleList().substring(0, user.getRoleList().length() - 1);
//		List<String> lst = Arrays.asList(roles.split(","));
//		UserDetails us = new User(user.getName(), user.getPassword(), mapRolesToAuthorities(Arrays.asList(roles.split(","))));
		UserDetails us = new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(Collections.singletonList(user.getRole())));
		return us;
	}

	public UserAccountDTO.UserInforResponseDTO getUserInforFromJWT(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String userName = userDetails.getUsername();
		Users userEntity = usersRepository.findByEmail(userName).get();

		return convertToObjectDto(userEntity);
	}

	private Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}

	private UserAccountDTO.UserInforResponseDTO convertToObjectDto(Object o) {
		return mapper.map(o, UserAccountDTO.UserInforResponseDTO.class);
	}
}
