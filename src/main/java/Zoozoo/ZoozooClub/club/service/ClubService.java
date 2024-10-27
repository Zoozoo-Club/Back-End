package Zoozoo.ZoozooClub.club.service;

import Zoozoo.ZoozooClub.balance.service.BalanceService;
import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.club.dto.ClubResponseDto;
import Zoozoo.ZoozooClub.club.dto.ClubResponseDto.ClubPortfolio.StockHoldings;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.club.exception.NoClubException;
import Zoozoo.ZoozooClub.club.repository.ClubRepository;
import Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.company.entity.Company;
import Zoozoo.ZoozooClub.company.exception.NoStockException;
import Zoozoo.ZoozooClub.ranking.entity.Ranking;
import Zoozoo.ZoozooClub.stock.entity.Stock;
import Zoozoo.ZoozooClub.stock.service.StockService;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final AuthService authService;
    private final BalanceService balanceService;
    private final StockService stockService;
    private final KoreaInvestmentApiService koreaInvestmentApiService;

    public ClubResponseDto getClubInfo(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(NoClubException::new);
        Company company = club.getCompany();
        List<Stock> stocks = company.getStocks();
        List<User> users = club.getUsers();
        Map<Long, List<Map<String, Object>>> allKeyAndBalances = balanceService.getAllKeyAndBalances();

        if(stocks.isEmpty()) throw new NoStockException();

        long totalInvestmentAmount = 0L;
        Map<String, StockInfo> stockInfoMap = new HashMap<>();

        for(User user : users) {
            Map<String, Object> balances = allKeyAndBalances.get(user.getAccount().getId()).get(0);
            List<Map<String, Object>> output1s = (List<Map<String, Object>>) balances.get("output1");
            Map<String, Object> output2 = (Map<String, Object>) balances.get("output2");

            Long total = Long.parseLong((String) output2.get("pchsAmtSmtlAmt"));
            totalInvestmentAmount += total;

            for(Map<String, Object> output1 : output1s) {
                String stockCode = (String) output1.get("stockCode");
                String stockName = (String) output1.get("stockName");
                Long evluAmt = Long.parseLong((String) output1.get("evluAmt"));
                Long evluPflsAmt = Long.parseLong((String) output1.get("evluPflsAmt"));

                StockInfo stockInfos = stockInfoMap.getOrDefault(stockCode, new StockInfo(stockCode, stockName, 0L, 0L, 0));
                stockInfos.amount += evluAmt;
                stockInfos.profit += evluPflsAmt;

                stockInfos.addParticipant();
                stockInfoMap.put(stockCode, stockInfos);
            }

        }

        List<StockHoldings> stockHoldings = new ArrayList<>();
        PriorityQueue<StockInfo> pq = new PriorityQueue<>();

        // pq에 담아서 정렬 후, ClubPortfolio에 넣기
        pq.addAll(stockInfoMap.values());

        while(!pq.isEmpty()) {
            StockInfo stockInfo = pq.poll();
            stockHoldings.add(StockHoldings.builder()
                            .holdingMembers(stockInfo.participant)
                            .stockCode(stockInfo.code)
                            .stockName(stockInfo.name)
                            .holdingRatio(((double) stockInfo.amount / totalInvestmentAmount) * 100)
                            .roi(((double) stockInfo.profit / stockInfo.amount))
                    .build());
        }

        return ClubResponseDto.builder().companyInfo(ClubResponseDto.CompanyInfo.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .logoId(stocks.get(0).getCode())
                .websiteUrl(company.getHomepage())
                .build())
                .clubPortfolio(
                        ClubResponseDto.ClubPortfolio.builder()
                                .totalMembers(users.size())
                                .totalInvestmentAmount(totalInvestmentAmount)
                                .stockHoldings(stockHoldings)
                                .build()
                ).build();
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public double getProfitByUserId(Long userId) {
        Club club = authService.getClubById(userId);
        List<Ranking> roiRanking = balanceService.getRoiRanking();
        double profit = 0;
        // Club 수익률 반환 하도록 구현 완료
        for(int i = 0; i < roiRanking.size(); i++) {
            Ranking ranking = roiRanking.get(i);
            if(Objects.equals(club.getId(), ranking.getClubId())) return ((double) ranking.getProfitValue() / ranking.getTotalAmount()) * 100;
        }

        return profit;
    }

    public StockPriceResponseDTO getClubCurrentPriceById(Long clubId, Account account){
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        Company company = club.getCompany();
        Stock stock = stockService.getStockByCompanyId(company.getId());

        double price =  koreaInvestmentApiService.getCurrentPrice(stock.getCode());
        return new StockPriceResponseDTO(stock.getCode(), stock.getName(), (long)price);
    }

    @Getter
    @AllArgsConstructor
    public class StockInfo implements Comparable<StockInfo> {
        private String code;
        private String name;
        private Long amount;
        private Long profit;
        private Integer participant;

        @Override
        public int compareTo(StockInfo o) {
            return (int) (o.amount - this.amount);
        }

        public void addParticipant() {
            participant++;
        }
    }


}
