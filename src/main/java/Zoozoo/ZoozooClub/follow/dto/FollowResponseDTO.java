package Zoozoo.ZoozooClub.follow.dto;

import Zoozoo.ZoozooClub.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDTO {

    private Long userId;
    private String nickname;
    private String clubName;

    public static FollowResponseDTO from(User user) {
        return FollowResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .clubName(user.getClub() != null ? user.getClub().getCompany().getName() : null)
                .build();
    }
}
