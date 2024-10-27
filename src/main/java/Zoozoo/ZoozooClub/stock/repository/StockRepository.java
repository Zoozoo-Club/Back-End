package Zoozoo.ZoozooClub.stock.repository;

import Zoozoo.ZoozooClub.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

        Stock findByCode(String stockCode);

        Stock getStockByCompanyId(Long companyId);
}
