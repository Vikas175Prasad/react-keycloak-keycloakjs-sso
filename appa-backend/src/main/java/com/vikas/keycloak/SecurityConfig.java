package com.vikas.keycloak;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.cors(withDefaults())     
		.authorizeHttpRequests(auth-> auth.requestMatchers("/public/**").permitAll().anyRequest().authenticated())  .oauth2ResourceServer(oauth2 -> oauth2
		        .jwt(jwt -> jwt
		                .jwkSetUri("http://localhost:8080/realms/reactsso/protocol/openid-connect/certs")
		              )
		            );
		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder() {
	    NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/reactsso/protocol/openid-connect/certs").build();
	    OAuth2TokenValidator<Jwt> withIssuer =
	        JwtValidators.createDefaultWithIssuer("http://localhost:8080/realms/reactsso");
	    decoder.setJwtValidator(withIssuer);
	    return decoder;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	  CorsConfiguration cfg = new CorsConfiguration();
	  cfg.setAllowedOrigins(List.of("http://localhost:3000")); // your frontend origin
	  cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
	  cfg.setAllowedHeaders(List.of("Authorization","Content-Type"));
	  cfg.setAllowCredentials(true);
	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  source.registerCorsConfiguration("/**", cfg);
	  return source;
	}
	
}
