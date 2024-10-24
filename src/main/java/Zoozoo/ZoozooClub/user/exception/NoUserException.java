package Zoozoo.ZoozooClub.user.exception;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import org.springframework.http.HttpStatus;

public class NoUserException extends ZoozooException {

    public NoUserException() {
        super(HttpStatus.BAD_REQUEST, "일치하는 유저가 없음");
    }
}
