package Zoozoo.ZoozooClub.ranking.controller;

import Zoozoo.ZoozooClub.balance.service.BalanceService;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.commons.exception.ZoozooException;
import Zoozoo.ZoozooClub.ranking.dto.InClubDto;
import Zoozoo.ZoozooClub.ranking.dto.RankingDto;
import Zoozoo.ZoozooClub.ranking.entity.InClubRanking;
import Zoozoo.ZoozooClub.ranking.entity.Ranking;
import Zoozoo.ZoozooClub.ranking.service.RankingService;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;
    private final BalanceService balanceService;
    private final AuthService authService;

    @GetMapping("/ranks/test")
    public ResponseEntity<String> test() {
        rankingService.updateRanking();
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/rankings/{clubId}")
    public ResponseEntity<List<InClubDto>> getInClubRanking(@PathVariable Long clubId) {
        List<User> users = authService.getAllUser();

        PriorityQueue<InClubDto> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());

        Map<Long, List<Map<String, Object>>> allBalance = balanceService.getAllKeyAndBalances();


        for(User user : users) {

            if(!Objects.equals(user.getClub().getId(), clubId)) continue;
            Map<String, Object> balances = allBalance.get(user.getAccount().getId()).get(0);
            Map<String, Object> output2 = (Map<String, Object>) balances.get("output2");

            Long total = Long.parseLong((String) output2.get("pchsAmtSmtlAmt"));
            Long profit = Long.parseLong((String)output2.get("evluPflsSmtlAmt"));

            InClubDto inClubDto = InClubDto.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .roi(((double) profit / (double) total) * 100)
                    .build();

            priorityQueue.add(inClubDto);
        }
        List<InClubDto> ans = new ArrayList<>();
        while(!priorityQueue.isEmpty()) {
            ans.add(priorityQueue.poll());
        }

        return ResponseEntity.ok(ans);
    }

    @GetMapping("/rankings")
    public ResponseEntity<List<RankingDto>> getRanking(@RequestParam("type") String type) {
        if(type.equals("user")) {
            return getListResponseEntity(balanceService.getUserRanking());
        } else if(type.equals("roi")) {
            return getListResponseEntity(balanceService.getRoiRanking());
        } else if(type.equals("amount")) {
            return getListResponseEntity(balanceService.getTotalRanking());
        }

        throw new ZoozooException(HttpStatus.BAD_REQUEST, "타입 제대로 입력해주세요");
    }

    private ResponseEntity<List<RankingDto>> getListResponseEntity(List<Ranking> userRanking) {
        List<RankingDto> list = new ArrayList<>();
        for(Ranking ranking : userRanking) {
            list.add(RankingDto.builder()
                    .clubId(ranking.getClubId())
                    .clubName(ranking.getClubName())
                    .profitValue(ranking.getProfitValue())
                    .roi(((double) ranking.getProfitValue() / ranking.getTotalAmount()) * 100)
                    .totalAmount(ranking.getTotalAmount())
                    .userCount(ranking.getUserCount())
                    .build());
        }

        return ResponseEntity.ok().body(list);
    }

}
