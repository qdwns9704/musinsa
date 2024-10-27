package musinsa.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import musinsa.service.BrandCategoryService;
import musinsa.service.PriceService;
import musinsa.struct.response.LowestHighestPriceCategoryResponse;
import musinsa.struct.response.LowestPriceBrandResponse;
import musinsa.struct.response.LowestPriceCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/api/prices")
@Slf4j
public class PriceController {
    @Autowired
    private PriceService priceService;

    @GetMapping("/lowest/categories")
    public ResponseEntity<LowestPriceCategoryResponse> getLowestPriceByCategories(
        @RequestParam(name = "categoryIds", required = false) List<Long> categoryIds
    ) {
        return ok(priceService.getLowestPriceByCategories(categoryIds));
    }

    @GetMapping("/lowest/brands")
    public ResponseEntity<LowestPriceBrandResponse> getLowestPriceBrand(
    ) {
        return ok(priceService.getLowestPriceBrand());
    }

    @GetMapping("/lowest-highest")
    public ResponseEntity<LowestHighestPriceCategoryResponse> getLowestHighestPriceCategory(
        @RequestParam(name = "categoryName") String name
    ) {
        return ok(priceService.getLowestHighestPriceCategory(name));
    }
}
