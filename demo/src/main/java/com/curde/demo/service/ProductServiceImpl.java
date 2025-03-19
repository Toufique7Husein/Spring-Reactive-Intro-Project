package com.curde.demo.service;

import com.curde.demo.dto.GenericResponse;
import com.curde.demo.dto.ProductDto;
import com.curde.demo.entity.ProductEntity;
import com.curde.demo.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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
        return productRepository.saveProduct(
                convertToEntity(productDto)
        ).map(isSaved -> convertToGenericResponse(isSaved, "product saved successfully"));
    }

    @Override
    public Mono<GenericResponse<?>> updateProduct(ProductDto productDto) {
        return productRepository.updateProduct(
                convertToEntity(productDto)
        ).map(isSaved -> convertToGenericResponse(isSaved, "update successfully"));
    }

    @Override
    public Mono<GenericResponse<?>> deleteProduct(String id) {
        return productRepository.deleteProductByID(id).map(isSaved -> convertToGenericResponse(isSaved, "delete successfully"));
    }

    @Override
    public Mono<GenericResponse<?>> findProductById(String id) {
        return productRepository.getProductByID(id)
                .map(product -> convertToGenericResponse(product, "product found"));
    }

    @Override
    public Flux<ProductDto> findAllProducts() {
        return productRepository.getAllProduct().map(this::convertToDto);
    }

    private ProductDto convertToDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .weight(productEntity.getWeight())
                .build();
    }

    private ProductEntity convertToEntity(ProductDto productDto) {
        return ProductEntity.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .weight(productDto.getWeight())
                .build();
    }

    private <T> GenericResponse<T> convertToGenericResponse(T t, String message) {
        return GenericResponse.<T>builder()
                .data(t)
                .message(message)
                .responseTime(LocalDateTime.now())
                .build();
    }
}
