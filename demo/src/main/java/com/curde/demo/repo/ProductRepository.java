package com.curde.demo.repo;

import com.curde.demo.entity.ProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * @author Md Toufique Husein
 * @since 17 March 2025
 * */


public interface ProductRepository {
    Mono<Boolean> saveProduct(ProductEntity merchantSegment);
    Flux<ProductEntity> getAllProduct();
    Mono<Boolean> updateProduct(ProductEntity productEntity);
    Mono<ProductEntity> getProductByID(String id);
    Mono<Boolean> deleteProductByID(String id);
}