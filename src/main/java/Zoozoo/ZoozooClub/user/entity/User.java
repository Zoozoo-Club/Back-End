package Zoozoo.ZoozooClub.user.entity;

import Zoozoo.ZoozooClub.club.entity.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String nickname;

    private String password;

    private String imgUrl;

    private Boolean publicPersonalInfo;

    // FK 추가
    @OneToOne
    @JoinColumn(name = "club_id")
    private Club club;
}
