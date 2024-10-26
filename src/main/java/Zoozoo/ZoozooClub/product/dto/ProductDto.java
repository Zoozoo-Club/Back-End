package Zoozoo.ZoozooClub.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    double profit;
    String name;
    String category;
    int risk;
    String url;

    @Builder
    public ProductDto(String name, String category, double profit, int risk, String url){
        this.name = name;
        this.category = category;
        this.profit = profit;
        this.risk = risk;
        this.url = url;
    }
}
