package com.example.takehome.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */
@EnableWebSecurity
public class SecurityConfig {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${rate.limit.unauthenticated:5}")
    private int unauthenticatedRequestsPerSecond;
    @Value("${rate.limit.authenticated:20}")
    private int authenticatedRequestsPerSecond;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */
        http.csrf().disable();
        http.antMatcher("/findOtherCountries")
                .authorizeRequests()
                .anyRequest().permitAll()
                .and().addFilterAfter(new RateLimitingFilter(authenticatedRequestsPerSecond, unauthenticatedRequestsPerSecond),
                        BasicAuthenticationFilter.class)
                .oauth2ResourceServer()
                .jwt();

         return http.build();
    }

    @Bean
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        // This is where we configure the security required for our actuator endpoints.
        // We want to allow access to the actuator endpoints only from
        /// the authenticated users
        // TODO: we could update below code to allow access to the actuator endpoints only from centain roles
        http.csrf().disable();
        http
                .requestMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().cors()
                .and().oauth2ResourceServer().jwt();
        return http.build();
    }
    @Bean
    JwtDecoder jwtDecoder() {
        /*
        By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
        indeed intended for our app. Adding our own validator is easy to do:
        */
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}