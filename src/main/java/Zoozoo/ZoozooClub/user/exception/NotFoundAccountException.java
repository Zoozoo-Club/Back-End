package Zoozoo.ZoozooClub.user.exception;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import org.springframework.http.HttpStatus;

public class NotFoundAccountException extends ZoozooException {
    public NotFoundAccountException() {
        super(HttpStatus.BAD_REQUEST, "계좌를 찾을 수 없음");
    }
}
