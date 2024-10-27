package Zoozoo.ZoozooClub.stock.service;

import Zoozoo.ZoozooClub.stock.entity.Stock;
import Zoozoo.ZoozooClub.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    public Stock getStockByCompanyId(Long companyId) {
        return stockRepository.getStockByCompanyId(companyId);
    }

}
