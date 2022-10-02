package com.zenmid.sds.config;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.nimbusds.jose.shaded.json.JSONObject;

import lombok.var;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.application.name}")
	private String appName;

	@Value("${security.enabled:true}")
	private boolean securityEnabled;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().authorizeRequests().antMatchers("/customer/*").hasAuthority("USER").anyRequest()
				.authenticated().and().oauth2ResourceServer().jwt();

		// http.cors().and().authorizeRequests().antMatchers("/customer/*").fullyAuthenticated().anyRequest()
		// .authenticated().and().oauth2ResourceServer().jwt();

		// http.csrf().disable().authorizeRequests().antMatchers("/customer/*").hasAuthority("USER").anyRequest()
		// .authenticated().and().oauth2ResourceServer().jwt();

		// http.authorizeRequests().antMatchers("*").hasRole("USER").anyRequest().permitAll();
		// http.oauth2Login().and().logout().addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		if (!securityEnabled) {
			web.ignoring().antMatchers("/**");
		}
	}

	@Bean
	@Primary
	@SuppressWarnings("unchecked")
	public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
		Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
			JSONObject json = jwt.getClaim("resource_access");
			Map<String, Collection<String>> roleMap = (Map<String, Collection<String>>) json.get(appName);
			Collection<String> roles = roleMap.get("roles");
			return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		};

		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}

	// NOT USED
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}

}
