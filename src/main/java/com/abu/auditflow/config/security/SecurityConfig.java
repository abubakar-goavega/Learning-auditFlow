package com.abu.auditflow.config.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.abu.auditflow.auth.security.handler.JwtAccessDeniedHandler;
import com.abu.auditflow.auth.security.handler.JwtAuthenticationEntryPoint;
import com.abu.auditflow.auth.security.jwt.JwtAuthenticationFilter;
import com.abu.auditflow.auth.security.jwt.JwtProperties;
import com.abu.auditflow.auth.security.userdetails.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http,
                        AuthenticationProvider authenticationProvider,
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        JwtAuthenticationEntryPoint authenticationEntryPoint,
                        JwtAccessDeniedHandler accessDeniedHandler) throws Exception {

                http

                                /*
                                 * CSRF Protection
                                 *
                                 * Protects browser-based forms against Cross Site Request Forgery.
                                 *
                                 * For REST APIs using:
                                 * - Postman
                                 * - Mobile Apps
                                 * - JWT
                                 *
                                 * it is commonly disabled.
                                 *
                                 * Later:
                                 * JWT -> disabled
                                 * Session Login -> usually enabled
                                 */
                                .csrf(csrf -> csrf.disable())

                                /*
                                 * Spring's default login page:
                                 *
                                 * http://localhost:8080/login
                                 *
                                 * We disable it because this template
                                 * will eventually use a custom API login.
                                 */
                                .formLogin(formLogin -> formLogin.disable())

                                /*
                                 * HTTP Basic Authentication
                                 *
                                 * Browser sends:
                                 *
                                 * Authorization: Basic base64(username:password)
                                 *
                                 * Useful for:
                                 * - Learning
                                 * - Testing
                                 * - Internal tools
                                 *
                                 * Not commonly used for modern APIs.
                                 */
                                .httpBasic(Customizer.withDefaults())

                                /*
                                 * Session Management
                                 *
                                 * IF_REQUIRED:
                                 * Create session only when needed.
                                 *
                                 * Current authentication is stored in:
                                 *
                                 * SecurityContext
                                 *
                                 * Later with JWT:
                                 *
                                 * SessionCreationPolicy.STATELESS
                                 */
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(
                                                                authenticationEntryPoint)
                                                .accessDeniedHandler(
                                                                accessDeniedHandler))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                /*
                                 * Authorization Rules
                                 *
                                 * Determines who may access which URLs.
                                 */
                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/actuator/health",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/",
                                                                "/about",
                                                                "/auth/**"
                                                )
                                                .permitAll()

                                                .requestMatchers("/admin")
                                                .hasRole("ADMIN")

                                                .anyRequest()
                                                .authenticated());

                return http.build();
        }

        @Bean
        AuthenticationProvider authenticationProvider(
                        CustomUserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {

                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

                provider.setUserDetailsService(
                                userDetailsService);

                provider.setPasswordEncoder(
                                passwordEncoder);

                return provider;
        }

        @Bean
        AuthenticationManager authenticationManager(
                        AuthenticationConfiguration configuration)
                        throws Exception {

                return configuration.getAuthenticationManager();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}