package Zoozoo.ZoozooClub.club.exception;

import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import org.springframework.http.HttpStatus;

public class NoClubException extends ZoozooException {
    
    public NoClubException() {
        super(HttpStatus.BAD_REQUEST, "일치하는 클럽 없음");
    }
}
