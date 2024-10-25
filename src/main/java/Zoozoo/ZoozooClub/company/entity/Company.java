package Zoozoo.ZoozooClub.company.entity;

import Zoozoo.ZoozooClub.stock.entity.Stock;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    // 설립일자
    private Date estDt;
    private String corpCls;
    private String homepage;
    private String description;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties("company")
    private List<Stock> stocks;

}
