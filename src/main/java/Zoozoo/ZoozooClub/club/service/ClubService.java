package Zoozoo.ZoozooClub.club.service;

import Zoozoo.ZoozooClub.club.dto.ClubResponseDto;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.club.exception.NoClubException;
import Zoozoo.ZoozooClub.club.repository.ClubRepository;
import Zoozoo.ZoozooClub.company.entity.Company;
import Zoozoo.ZoozooClub.company.exception.NoStockException;
import Zoozoo.ZoozooClub.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

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

}
