package br.org.apae.api.auth.infrastructure.security;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.domain.exceptions.TokenVerificationException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health",
            "/actuator/health/**",
            "/actuator/info",
            "/error"
    );
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityFilter(JwtProvider jwtProvider, UserService userService,
                          RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = getRequestPathWithinServlet(request);
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String getRequestPathWithinServlet(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestUri = request.getRequestURI();

        String path = requestUri;
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        if (servletPath != null && !servletPath.isEmpty() && path.startsWith(servletPath)) {
            path = path.substring(servletPath.length());
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        return path;
    }

    String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        if (authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return authHeader.substring(7).trim();
        }

        return null;
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
            } catch (TokenVerificationException | UserNotFoundException ex) {
                SecurityContextHolder.clearContext();
                log.warn("Falha de autenticação: {}", ex.getClass().getSimpleName());
                authenticationEntryPoint.commence(request, response, new AuthenticationException(ex.getMessage(), ex) {});
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}