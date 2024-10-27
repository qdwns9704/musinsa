package musinsa.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import musinsa.dto.category.Category;
import musinsa.service.CategoryService;
// import musinsa.service.LowestPriceService;
import musinsa.struct.response.ErrorResponse;
import musinsa.struct.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@RequestMapping("/api/categories")
@Slf4j
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // @Autowired
    // LowestPriceService lowestPriceService;

    @GetMapping("")
    public ResponseEntity<ListResponse<Category>> getCategories(
        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
        @RequestParam(name = "count", required = false, defaultValue = "30") int count,
        @RequestParam(name = "criteria", required = false, defaultValue = "createdAt") String criteria,
        @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort
    ) {
        return ok(categoryService.getCategories(page, count, criteria, sort));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(
        @PathVariable(name = "categoryId") Long categoryId
    ) {
        return ok(categoryService.getCategory(categoryId));
    }

    @PostMapping("")
    public ResponseEntity<Category> createCategory(
        @Valid @RequestBody Category category
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(categoryService.createCategory(category));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
        @PathVariable(name = "categoryId") Long categoryId,
        @Valid @RequestBody Category category
    ) {
        return ok(categoryService.updateCategory(category.setId(categoryId)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
        @PathVariable(name = "categoryId") Long categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return ok().build();
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ignore) {
        return ErrorResponse.of(HttpStatus.CONFLICT, "Name of category is already exists");
    }
}