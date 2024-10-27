package Zoozoo.ZoozooClub.ranking.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class InClubDto implements Comparable<InClubDto>{
    private Long userId;
    private String nickname;
    private Double roi;

    @Override
    public int compareTo(InClubDto o) {
        if(Objects.equals(this.roi, o.roi)) return 0;
        return (this.roi - o.roi) < 0 ? -1 : 1;
    }
}
