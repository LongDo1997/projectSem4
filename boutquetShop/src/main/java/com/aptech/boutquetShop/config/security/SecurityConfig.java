package com.aptech.boutquetShop.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aptech.boutquetShop.service.impl.AuthService;
import com.aptech.boutquetShop.utils.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthService authService;
//	@Autowired
//	private PermissionService Permission;
	@Autowired
	private JwtAuthEntryPoint authEntryPoint;
	
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/auth/**").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(authEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.httpBasic();
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//		http.build();
		// http.addFilterBefore(jwtAuthenticationFilter(),
		// UsernamePasswordAuthenticationFilter.class);
		// List<Permission> listPermission = this.Permission.getList();
		// http.authorizeRequests().antMatchers("/").permitAll();

//		for (Permission permiss : listPermission) {
//			String listauth = "";
//			for (int i = 0; i < permiss.getListRole().size(); i++) {
//				if(i==0) {
//					listauth+="hasAuthority('"+permiss.getListRole().get(i).getName()+"')";
//				}else {
//					listauth+=" or hasAuthority('"+permiss.getListRole().get(i).getName()+"')";
//				}
//			}
//			http
//				.authorizeRequests()
//				.antMatchers(permiss.getUrl()).access(listauth);
//		}

//		http
//			.authorizeRequests()
//			.anyRequest()			
//			.authenticated();
//		http.formLogin().loginPage("/login")
//			.and()
//			.formLogin()
//			.loginProcessingUrl("/login")
//			.usernameParameter("username")
//			.passwordParameter("password")
//			.and()
//			.formLogin().defaultSuccessUrl("/welcome")
//			.permitAll()
//			.and().logout().logoutSuccessUrl("/logout");
		// .and()
		// .exceptionHandling().accessDeniedHandler(userService);

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService).passwordEncoder(this.passwordEncoder());

	}

//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
