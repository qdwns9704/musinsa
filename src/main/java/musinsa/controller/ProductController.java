package musinsa.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import musinsa.dto.product.Product;
import musinsa.service.ProductService;
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
@RequestMapping(path = "/api/products")
@Slf4j
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("")
    public ResponseEntity<ListResponse<Product>> getProducts(
        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
        @RequestParam(name = "count", required = false, defaultValue = "10") int count,
        @RequestParam(name = "criteria", required = false, defaultValue = "createdAt") String criteria,
        @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort
    ) {
        return ok(productService.getProducts(page, count, criteria, sort));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable(name = "productId") Long productId) {
        return ok(productService.getProduct(productId));
    }

    @PostMapping("")
    public ResponseEntity<Product> createProduct(
        @Valid @RequestBody Product product
    ) {
        /*
         * 벌크 생성을 고려한다면, List<Product>를 받아 처리하는 방식으로 작성
         * 서비스 로직 내부적으로 BrandEntity와 CategoryEntity를 findAllByIds 등으로 일괄 조회하여 로직단에서 맵핑하는 과정 진행
         * 또한 각 벌크 요청의 성공/실패 여부를 나타내야한다면 save 요청을 나누어서 실행하고, 각각의 결과를 메세지에 담아서 진행
         */
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productService.createProduct(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable(name = "productId") Long productId,
        @Valid @RequestBody Product product
    ) {
        /*
         * 생성과 마찬가지로 벌크 수정을 고려한다면, List<Product>를 받아 처리하는 방식으로 작성
         * 서비스 로직 내부적으로 BrandEntity와 CategoryEntity를 findAllByIds 등으로 일괄 조회하여 로직단에서 맵핑하는 과정 진행
         * 또한 각 벌크 요청의 성공/실패 여부를 나타내야한다면 save 요청을 나누어서 실행하고, 각각의 결과를 메세지에 담아서 진행
         */
        return ok(productService.updateProduct(product.setId(productId)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
        return ok().build();
    }
}
