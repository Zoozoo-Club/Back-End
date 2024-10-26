package Zoozoo.ZoozooClub.ranking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {
    private Long clubId;
    private String clubName;
    private Long profitValue;
    private Long totalAmount;
    private Long userCount;
    private String code;

    public void addTotalAmount(Long amount) {
        this.totalAmount += amount;
    }

    public void addUserCount() {
        userCount++;
    }

    public void addProfitValue(Long profitValue) {
        this.profitValue += profitValue;
    }

}
