package Zoozoo.ZoozooClub.commons.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ZoozooException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;

    public ZoozooException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}