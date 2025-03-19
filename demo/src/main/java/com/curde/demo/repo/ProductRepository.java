package com.curde.demo.repo;

import com.curde.demo.entity.ProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface ProductRepository {
    Mono<Boolean> saveProduct(ProductEntity merchantSegment);
    Flux<ProductEntity> getAllProduct();
    Mono<Boolean> updateProduct(ProductEntity productEntity);
    Mono<ProductEntity> getProductByID(String id) throws ExecutionException, InterruptedException;
    Mono<Boolean> deleteProductByID(String id);
}