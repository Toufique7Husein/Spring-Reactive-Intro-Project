package com.curde.demo.controller;


import com.curde.demo.entity.ProductEntity;
import com.curde.demo.repo.impl.ProductRepoImpl;
import com.oracle.svm.core.annotate.Delete;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductRepoImpl productRepo;

    @PostMapping("/product")
    public Mono<Boolean> getProduct(@RequestBody ProductEntity product) {
        return productRepo.saveProduct(product);
    }

    @GetMapping("/review/all")
    public Flux<ProductEntity> getProduct(@PathVariable String id) {
        return productRepo.getAllProduct();
    }

    @DeleteMapping("/product/delete/id")
    public Mono<Boolean> updateProduct(@RequestBody ProductEntity updatedProduct) {
        return productRepo.update(updatedProduct);
    }
}
