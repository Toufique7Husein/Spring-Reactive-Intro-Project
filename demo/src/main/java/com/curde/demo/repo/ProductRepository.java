package com.curde.demo.repo;

import com.curde.demo.entity.ProductEntity;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Boolean> saveProduct(ProductEntity merchantSegment);
    Flux<ProductEntity> getAllProduct();
    Mono<ProductEntity> getProduct(ProductEntity productEntity);
}
