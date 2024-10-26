package Zoozoo.ZoozooClub.commons.kis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String stockCode;        // 종목 코드
    private String orderSide;        // 매매구분 (01:매도, 02:매수)
}

