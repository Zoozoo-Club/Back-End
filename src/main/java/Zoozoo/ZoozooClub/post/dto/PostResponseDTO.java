package Zoozoo.ZoozooClub.post.dto;

import Zoozoo.ZoozooClub.post.entity.Post;
import Zoozoo.ZoozooClub.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long pv;
    private Long userId;
    private String nickname;
    private String clubName;
    private Long clubId;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public static PostResponseDTO from(Post post, User user) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .pv(post.getPv())
                .userId(post.getUserId())
                .nickname(user.getNickname())
                .clubName(user.getClub() != null ? user.getClub().getCompany().getName() : null)
                .clubId(post.getClubId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}