package musinsa.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import musinsa.dto.brand.Brand;
import musinsa.service.BrandService;
import musinsa.struct.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/brands")
@Slf4j
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("")
    public ResponseEntity<ListResponse<Brand>> getBrands(
        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
        @RequestParam(name = "count", required = false, defaultValue = "30") int count,
        @RequestParam(name = "criteria", required = false, defaultValue = "createdAt") String criteria,
        @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort
    ) {
        return ok(brandService.getBrands(page, count, criteria, sort));
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<Brand> getBrand(
        @PathVariable(name = "brandId") Long brandId
    ) {
        return ok(brandService.getBrand(brandId));
    }

    @PostMapping("")
    public ResponseEntity<Brand> createBrand(
        @Valid @RequestBody Brand brand
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(brandService.createBrand(brand));
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<Brand> updateBrand(
        @PathVariable(name = "brandId") Long brandId
        , @Valid @RequestBody Brand brand
    ) {
        return ok(brandService.updateBrand(brand.setId(brandId)));
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "brandId") Long brandId) {
        brandService.deleteBrand(brandId);
        return ok().build();
    }
}
