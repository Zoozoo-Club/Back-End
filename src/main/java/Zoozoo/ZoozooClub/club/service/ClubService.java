package Zoozoo.ZoozooClub.club.service;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.club.dto.ClubResponseDto;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.club.exception.NoClubException;
import Zoozoo.ZoozooClub.club.repository.ClubRepository;
import Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.company.entity.Company;
import Zoozoo.ZoozooClub.company.exception.NoStockException;
import Zoozoo.ZoozooClub.stock.entity.Stock;
import Zoozoo.ZoozooClub.stock.service.StockService;
import Zoozoo.ZoozooClub.user.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final AuthService authService;
    private final StockService stockService;
    private final KoreaInvestmentApiService koreaInvestmentApiService;
    public ClubResponseDto getClubInfo(Long id) {
        Company company = clubRepository.findById(id).orElseThrow(NoClubException::new).getCompany();
        List<Stock> stocks = company.getStocks();


        if(stocks.isEmpty()) throw new NoStockException();


        // todo Redis에서 Account 정보 조회해서 Club에 속한 유저의 포트폴리오를 다 더해서 클럽 포트폴리오를 만드는 로직이 필요함.
        return ClubResponseDto.builder().companyInfo(ClubResponseDto.CompanyInfo.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .logoId(stocks.get(0).getCode())
                .websiteUrl(company.getHomepage())
                .build())
                .clubPortfolio(
                        null
                ).build();
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public double getProfitByUserId(Long userId) {
        Club club = authService.getClubById(userId);
        double profit = 0;
        // TODO: club에 해당하는 수익률을 리턴!
        return profit;
    }

    public StockPriceResponseDTO getClubCurrentPriceById(Long clubId, Account account){
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        Company company = club.getCompany();
        Stock stock = stockService.getStockByCompanyId(company.getId());
        double price =  koreaInvestmentApiService.getCurrentPrice(stock.getCode(),account);
        return new StockPriceResponseDTO(stock.getCode(), (long) price);
    }


}
