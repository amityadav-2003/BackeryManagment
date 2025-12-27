package com.uniquebitehub.ApplicationMain.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//    	 http
//         .csrf(csrf -> csrf.disable())
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers(
//                 "/user/signup",
//                 "/user/login",
//                 // Swagger UI और API docs के लिए permitAll add करें
//                 "/swagger-ui/**",
//                 "/v3/api-docs/**",
//                 "/swagger-ui.html",
//                 "/swagger-resources/**",
//                 "/webjars/**",
//                 "/configuration/ui",
//                 "/configuration/security",
//                 "/error"
//             ).permitAll()
//             .anyRequest().authenticated()
//         )
//         .httpBasic();
    	
    	  http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
              .anyRequest().permitAll()  // सभी को allow करें (temporary)
          );
    	 
     return http.build();
 }
}