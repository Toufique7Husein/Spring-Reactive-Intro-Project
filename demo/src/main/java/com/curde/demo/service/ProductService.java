package com.curde.demo.service;

import com.curde.demo.dto.GenericResponse;
import com.curde.demo.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

/**
 * @author Md Toufique Husein
 * @since 19 March 2025
 */

public interface ProductService {
    Mono<GenericResponse<?>> saveProduct(ProductDto productDto);

    Mono<GenericResponse<?>> updateProduct(ProductDto productDto);

    Mono<GenericResponse<?>> deleteProduct(String id);

    Mono<GenericResponse<?>> findProductById(String id);

    Flux<ProductDto> findAllProducts();
}
