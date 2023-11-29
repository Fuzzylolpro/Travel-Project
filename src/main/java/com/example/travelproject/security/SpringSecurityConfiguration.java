package com.example.travelproject.security;

import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // отключаем csrf
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(new AntPathRequestMatcher("/users/{id}", "GET")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users", "GET")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users", "PUT")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users", "POST")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/addFavoriteAttractions", "POST")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/addFavoriteAttractions", "GET")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/{id}", "DELETE")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/{userId}/favoriteAttractions", "GET")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/deleteFavoriteAttractions", "DELETE")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/addFavoriteAttractions", "POST")).hasAnyRole("USER","ADMIN")
                                /*.requestMatchers(HttpMethod.DELETE, "/security/**").hasRole("ADMIN")
                                .requestMatchers("/registration").permitAll()*/
                                .requestMatchers(new AntPathRequestMatcher("/security/registration", "POST")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/security", "POST")).permitAll()

                                .requestMatchers(new AntPathRequestMatcher("/comments/**", "POST")).hasRole("USER")
                                .requestMatchers(new AntPathRequestMatcher("/comments/**", "GET")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/comments/**", "PUT")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/comments/**", "DELETE")).hasRole("USER")
                                .requestMatchers(new AntPathRequestMatcher("/comments/**", "DELETE")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/attractions/**", "GET")).permitAll()

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//отключить сессии
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)//a=фильтр JWT
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
