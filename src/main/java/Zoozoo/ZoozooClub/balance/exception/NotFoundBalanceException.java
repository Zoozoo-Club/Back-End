package Zoozoo.ZoozooClub.balance.exception;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import org.springframework.http.HttpStatus;

public class NotFoundBalanceException extends ZoozooException {
    public NotFoundBalanceException() {
        super(HttpStatus.BAD_REQUEST, "잔고를 찾을 수 없음");
    }
}
