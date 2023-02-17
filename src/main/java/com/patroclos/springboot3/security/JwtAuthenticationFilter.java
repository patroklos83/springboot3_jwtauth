package com.patroclos.springboot3.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.patroclos.springboot3.configuration.WebSecurityConfig;
import com.patroclos.springboot3.service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
			) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		boolean isInvalidToken = false;
		
		if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
			isInvalidToken = true;
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUsername(jwt);
		if (userEmail != null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities()
						);
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
						);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		else
		{
			isInvalidToken = true;
		}
		
		if (isInvalidToken)
		{
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
		}		
		
		filterChain.doFilter(request, response);
	}

	//	@Override
	//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
	//			throws IOException, ServletException {
	//		final String authHeader = ((HttpServletRequest) request).getHeader("Authorization");
	//		final String jwt;
	//		final String userEmail;
	//
	//		String path = ((HttpServletRequest) request).getRequestURI();
	//		String[] allowedPaths = WebSecurityConfig.PUBLIC_REQUEST_MATCHERS;
	//		for (var allowedPath : allowedPaths) {
	//			allowedPath = allowedPath.replace("*", "");
	//			if (path.startsWith(allowedPath)) {
	//				filterChain.doFilter(request, response);
	//				return;
	//			}
	//		}
	//
	//		if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	//			filterChain.doFilter(request, response);
	//			return;
	//		}
	//		jwt = authHeader.substring(7);
	//		userEmail = jwtService.extractUsername(jwt);
	//		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	//			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
	//			if (jwtService.isTokenValid(jwt, userDetails)) {
	//				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	//						userDetails,
	//						null,
	//						userDetails.getAuthorities()
	//						);
	//				authToken.setDetails(
	//						new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request)
	//						);
	//				SecurityContextHolder.getContext().setAuthentication(authToken);
	//			}
	//		}
	//		filterChain.doFilter(request, response);
	//
	//	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request)
			throws ServletException {
		//String path = request.getRequestURI();
		//return "/health".equals(path);

		String path = ((HttpServletRequest) request).getRequestURI();
		String[] allowedPaths = WebSecurityConfig.PUBLIC_REQUEST_MATCHERS;
		for (var allowedPath : allowedPaths) {
			allowedPath = allowedPath.replace("/*", "");
			allowedPath = allowedPath.replace("/**", "");
			if (path.contains(allowedPath)) {
				return true;
			}
		}

		return false;
	}
}
