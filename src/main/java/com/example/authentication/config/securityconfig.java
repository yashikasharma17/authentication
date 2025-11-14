package com.example.authentication.config;

import com.example.authentication.service.Appuserdetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityconfig {
    @Autowired
    private Appuserdetails app;
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
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5073"));

    config.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"));
    config.addAllowedHeader(String.valueOf(List.of("Authorization", "Content-type")));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
    @Bean
    public AuthenticationManager authenticationManager(){
DaoAuthenticationProvider daoAuthenticationProvider1=new DaoAuthenticationProvider();
daoAuthenticationProvider1.setUserDetailsService((UserDetailsService) app);
daoAuthenticationProvider1.setPasswordEncoder(bCryptPasswordEncoder());
return new ProviderManager(daoAuthenticationProvider1);

}
}
