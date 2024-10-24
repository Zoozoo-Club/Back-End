package Zoozoo.ZoozooClub.commons.auth;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Enumeration;

@NoArgsConstructor
public class AuthorizationExtractor {

    private static final String AUTHENTICATION_TYPE = "Bearer";
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final int START_TOKEN_INDEX = 6;

    public static String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION_HEADER_KEY);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(AUTHENTICATION_TYPE.toLowerCase())) {
                String extractToken = value.trim().substring(START_TOKEN_INDEX);
                validateExtractToken(extractToken);
                return extractToken;
            }
        }
        throw new ZoozooException(HttpStatus.UNAUTHORIZED, "토큰 없음");
    }

    private static void validateExtractToken(String extractToken) {
        if (extractToken.isBlank()) {
           throw new ZoozooException(HttpStatus.UNAUTHORIZED, "토큰 만료됨");
        }
    }
}