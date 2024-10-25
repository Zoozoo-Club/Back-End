package Zoozoo.ZoozooClub.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthLoginResponseDto {
    private final String nickname;
    private final String token;
}
