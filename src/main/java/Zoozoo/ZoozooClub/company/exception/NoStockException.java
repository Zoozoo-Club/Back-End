package Zoozoo.ZoozooClub.company.exception;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import org.springframework.http.HttpStatus;

public class NoStockException extends ZoozooException {
    
    public NoStockException() {
        super(HttpStatus.BAD_REQUEST, "회사와 일치하는 주식이 없음");
    }
}
