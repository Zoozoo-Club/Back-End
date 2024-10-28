package Zoozoo.ZoozooClub.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OtherUserResponseDto {
    private final String clubName;
    private final String userName;
}
