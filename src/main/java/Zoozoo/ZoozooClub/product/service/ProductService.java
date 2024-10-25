package Zoozoo.ZoozooClub.product.service;

import Zoozoo.ZoozooClub.balance.service.BalanceService;
import Zoozoo.ZoozooClub.product.dto.ProductDto;
import Zoozoo.ZoozooClub.product.entity.Product;
import Zoozoo.ZoozooClub.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BalanceService balanceService;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductDto> productDtos = products.stream()
                .map(product -> ProductDto.builder()
                        .name(product.getName())
                        .category(product.getCategory())
                        .profit(product.getProfit())
                        .risk(product.getRisk())
                        .build())
                .collect(Collectors.toList());

        return productDtos;
    }


    public List<ProductDto> getHigherProductsThanUserProfit(Long userId){
        double profit = balanceService.getProfitByUserId(userId);

        List<ProductDto> higerProducts = new ArrayList<>();
        List<Product> products = productRepository.findByProfitGreaterThanOrderByProfitDesc(profit);

        for (Product product : products) {
            higerProducts.add(ProductDto.builder()
                            .category(product.getCategory())
                            .name(product.getName())
                            .profit(product.getProfit())
                            .risk(product.getRisk())
                            .build());
        }

        return higerProducts;
    }
}
