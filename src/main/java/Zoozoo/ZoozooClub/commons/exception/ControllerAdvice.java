package Zoozoo.ZoozooClub.commons.exception;

import Zoozoo.ZoozooClub.commons.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ZoozooException.class)
    public ResponseEntity<ErrorResponse> handleZoozooException(ZoozooException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getMessage()));
    }
}
