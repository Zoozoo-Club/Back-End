package Zoozoo.ZoozooClub.user.exception;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import org.springframework.http.HttpStatus;

public class NotCorrectPasswordException extends ZoozooException {

    public NotCorrectPasswordException() {
        super(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않음");
    }
}
