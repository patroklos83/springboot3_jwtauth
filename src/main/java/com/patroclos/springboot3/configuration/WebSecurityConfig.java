package com.patroclos.springboot3.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.patroclos.springboot3.security.JwtAuthenticationFilter;
import com.patroclos.springboot3.service.JwtService;

@Configuration
public class WebSecurityConfig {

	//@Autowired
	//private JwtAuthenticationFilter jwtAuthenticationFilter;	
	public final static String[] PUBLIC_REQUEST_MATCHERS = { "/api/v1/auth/**", "/api-docs/**", "/swagger-ui/**" };

	@Bean
	@Order(1000)
	public JwtAuthenticationFilter authenticationJwtTokenFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {	
		http
		.cors()
		.and()
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.requestMatchers(PUBLIC_REQUEST_MATCHERS).permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
		// this disables session creation on Spring Security
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

	@Bean
	public UserDetailsService users() {
		// The builder will ensure the passwords are encoded before saving in memory
		UserBuilder users = User.builder();
		UserDetails user = users
				.username("user")
				.password(passwordEncoder().encode("password"))
				.roles("USER")
				.build();
		UserDetails admin = users
				.username("admin")
				.password(passwordEncoder().encode("password"))
				.roles("USER", "ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.setAllowedOrigins(Arrays.asList("localhost:8080"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST"));
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

}