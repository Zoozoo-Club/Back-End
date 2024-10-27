package Zoozoo.ZoozooClub.club.dto;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class MyClubResponseDto {
    private final Long clubId;
    private final String clubName;
}
