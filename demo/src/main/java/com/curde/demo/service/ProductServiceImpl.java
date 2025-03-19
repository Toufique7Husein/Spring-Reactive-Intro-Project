package com.curde.demo.service;

import com.curde.demo.dto.GenericResponse;
import com.curde.demo.dto.ProductDto;
import com.curde.demo.entity.ProductEntity;
import com.curde.demo.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

/**
 * @author Md Toufique Husein
 * @since 19 March 2025
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Mono<GenericResponse<?>> saveProduct(ProductDto productDto) {
        var saveProduct = productRepository.saveProduct(
                ProductEntity.builder()
                        .id(productDto.getId())
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .weight(productDto.getWeight())
                        .build()
        );
        return Mono.just(GenericResponse.builder()
                .data(saveProduct)
                .message("Product saved successfully")
                .build());
    }

    @Override
    public Mono<GenericResponse<?>> updateProduct(ProductDto productDto) {
        var saveProduct = productRepository.updateProduct(
                ProductEntity.builder()
                        .id(productDto.getId())
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .weight(productDto.getWeight())
                        .build()
        );
        return Mono.just(GenericResponse.builder()
                .data(saveProduct)
                .message("Product saved successfully")
                .build());
    }

    @Override
    public Mono<GenericResponse<?>> deleteProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<GenericResponse<?>> findProductById(String id) throws ExecutionException, InterruptedException {
        var product = productRepository.getProductByID(id);
        var productDto = product.subscribe(
                productEntity -> {
                    ProductDto.builder().id(productEntity.getId())
                            .name(productEntity.getName())
                            .price(productEntity.getPrice())
                            .weight(productEntity.getWeight())
                            .build();
                }
        );
        return Mono.just(GenericResponse.builder()
                        .data(productDto)
                        .message("Product found successfully")
                .build());
    }

    @Override
    public Flux<ProductDto> findAllProducts() {
        return productRepository.getAllProduct().map(this::toDto);
    }

    private ProductDto toDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .weight(productEntity.getWeight())
                .build();
    }
}
