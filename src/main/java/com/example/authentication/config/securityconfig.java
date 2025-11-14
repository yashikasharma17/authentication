package com.example.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class securityconfig {
@Bean
    public DefaultSecurityFilterChain securityWebFilterChain(HttpSecurity http ) throws Exception {
    http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth->auth
                    .requestMatchers("/login","/register","send-reset-otp","/reset-password")
                    .permitAll().anyRequest().authenticated())
            .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(AbstractHttpConfigurer::disable);

    return http.build();
}
@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
}
public CorsFilter corsFilter(){
    return new CorsFilter(corsConfigurationSource());
}
public CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration config=new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5073"));

    config.setAllowedMethods(List.of("GET","PUT","POST","DELETE","PATCH","OPTIONS"));
    config.addAllowedHeader(String.valueOf(List.of("Authorization","Content-type")));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**",config);
    return source;
}
}
