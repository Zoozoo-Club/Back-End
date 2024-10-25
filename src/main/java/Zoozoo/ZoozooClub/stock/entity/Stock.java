package Zoozoo.ZoozooClub.stock.entity;


import Zoozoo.ZoozooClub.company.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
