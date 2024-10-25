package Zoozoo.ZoozooClub.account.entity;

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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String acctNo;

    private String appKey;

    private String secretKey;

    private String accessToken;
}
