package Zoozoo.ZoozooClub.commons.auth;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JWTUtil jwtUtil;

    public LoginInterceptor(final JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflight(request) || isPassRequest(request)) {
            return true;
        }

        String token = AuthorizationExtractor.extractAccessToken(request);
        if(jwtUtil.isExpired(token)) {
            throw new ZoozooException(HttpStatus.UNAUTHORIZED, "토큰 만료됨");
        }
        return true;
    }

    private boolean isPassRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars") || uri.contains("auth") || uri.contains("ranking") || uri.contains("clubs");
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }
}