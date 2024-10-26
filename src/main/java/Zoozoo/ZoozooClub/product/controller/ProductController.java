package Zoozoo.ZoozooClub.product.controller;

import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.product.dto.ProductDto;
import Zoozoo.ZoozooClub.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/higher-profit")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "test API")
    public ResponseEntity<List<ProductDto>> getHigherProductsThanUserProfit(@LoginUserId Long userId) {
        return ResponseEntity.ok().body(productService.getHigherProductsThanUserProfit(userId));
    }
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok().body(productService.getAllProducts());
    }
}
