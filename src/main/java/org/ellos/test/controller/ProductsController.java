package org.ellos.test.controller;

import org.ellos.test.models.ResponseObject;
import org.ellos.test.service.ProductsService;
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
    public ResponseEntity<ResponseObject> getProducts(@RequestParam String path, @RequestParam(required = false) String filter) {
        // TODO: Skapa model för JSON som ska returneras (behöver en lista typ med Produkter och Float för genomsnitts pris)
        // TODO: Skapa Service som returnerar Objekt med bara produkterna och snittpris
        // TODO: Skapa Test som gör detta så vi inte spmammar API

        return ResponseEntity.ok().body(productsService.getProducts(path, filter));
    }
}
