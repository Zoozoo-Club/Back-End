package Zoozoo.ZoozooClub.club.entity;

import Zoozoo.ZoozooClub.company.entity.Company;
import Zoozoo.ZoozooClub.stock.entity.Stock;
import Zoozoo.ZoozooClub.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("club")
    private List<User> users;
}
