package com.curde.demo.controller;


import com.curde.demo.dto.GenericResponse;
import com.curde.demo.dto.ProductDto;
import com.curde.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

import static com.curde.demo.constant.UrlConstants.PRODUCTS_URL;

/**
 * @author MD Toufique Husein
 * @since 17 March 2025
 * */


@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(PRODUCTS_URL)
    public Mono<GenericResponse<?>> saveProduct(@RequestBody ProductDto product) {
        return productService.saveProduct(product);
    }

    @GetMapping(PRODUCTS_URL)
    public Flux<ProductDto> getProduct() {
        return productService.findAllProducts();
    }

    @PutMapping(PRODUCTS_URL)
    public Mono<GenericResponse<?>> updateProduct(@RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @GetMapping(PRODUCTS_URL + "/{id}")
    public Mono<GenericResponse<?>> getProductById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return productService.findProductById(id);
    }
}
