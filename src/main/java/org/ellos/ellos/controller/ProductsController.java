package org.ellos.ellos.controller;

import org.ellos.ellos.models.ProductReport;
import org.ellos.ellos.service.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<ProductReport> getProducts(@RequestParam String path, @RequestParam(required = false) String filter) {
        return ResponseEntity.ok().body(productsService.getProducts(path, filter));
    }
}
