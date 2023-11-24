package com.example.travelproject.security.filter;


import com.example.travelproject.security.service.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.travelproject.security.service.CustomUserDetailService;


import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //из req достать jwt tokemn
        Optional<String> token = jwtUtils.getTokenFromHttpRequest(request);
        //если он !=null и валидный то достаем логин -> пароль роль,
        // создаем юзер детэил и кладем в security context
        if (token.isPresent() && jwtUtils.validateToken(token.get())) {
            Optional<String> login = jwtUtils.getLoginFromJwt(token.get());
            if (login.isPresent()) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(login.get());
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null
                        , userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(upat);
                log.info("Authenticated user with login " + login.get());
            }
        }
        filterChain.doFilter(request, response);
    }
}
