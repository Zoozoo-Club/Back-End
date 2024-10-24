package Zoozoo.ZoozooClub.user.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthLoginRequestDto {
    private final String userId;
    private final String password;
}
