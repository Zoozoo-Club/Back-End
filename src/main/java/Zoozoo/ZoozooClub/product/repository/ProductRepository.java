package Zoozoo.ZoozooClub.product.repository;

import Zoozoo.ZoozooClub.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByProfitGreaterThanOrderByProfitDesc(double profit);
}
