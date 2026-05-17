package com.jobportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(authz -> authz
        	    .requestMatchers(
        	        "/", 
        	        "/auth/**",
        	        "/jobs",
        	        "/jobs/search",
        	        "/jobs/filter",
        	        "/error",
        	        "/css/**",
        	        "/js/**",
        	        "/images/**",
        	        "/api/chat"
        	    ).permitAll()

        	    .requestMatchers("/jobs/create", "/jobs/*/edit", "/jobs/*/delete")
        	    .hasAnyAuthority("EMPLOYER", "ADMIN")

        	    .requestMatchers("/applications/apply/**")
        	    .hasAnyAuthority("STUDENT", "ADMIN")

        	    .anyRequest().authenticated()
        	)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/chat")
            )
            .formLogin(form -> form
            	    .loginPage("/auth/login")
            	    .loginProcessingUrl("/auth/login")
            	    .usernameParameter("email")
            	    .passwordParameter("password")
            	    .defaultSuccessUrl("/jobs", true)
            	    .failureUrl("/auth/login?error=true")   // 🔥 ADD THIS
            	    .permitAll()
            	)
            .logout(logout -> logout
                .logoutUrl("/auth/logout")                      
                .logoutSuccessUrl("/auth/login?logout=true")    
                .invalidateHttpSession(true)                    // End session after login
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
