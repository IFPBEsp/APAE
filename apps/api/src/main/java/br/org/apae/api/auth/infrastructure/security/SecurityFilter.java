package br.org.apae.api.auth.infrastructure.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.common.exceptions.types.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  JwtProvider jwtProvider;
  UserService userService;
  ObjectMapper objectMapper;

  public SecurityFilter(JwtProvider jwtProvider, UserService userService, ObjectMapper objectMapper) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");

    if (authHeader == null) {
      return null;
    }

    return authHeader.replace("Bearer ", "");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var token = this.recoverToken(request);

    if (token != null) {
      try {
        var username = jwtProvider.validateToken(token);
        UserDetails user = userService.findUserByUsername(username);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception ex) {
        this.sendUnauthorized(request, response, "Token inválido ou expirado.");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private void sendUnauthorized(HttpServletRequest request, HttpServletResponse response, String message)
      throws IOException {
    var errorResponse = new ErrorResponse(
        HttpServletResponse.SC_UNAUTHORIZED,
        "Unauthorized",
        message,
        request.getRequestURI());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}