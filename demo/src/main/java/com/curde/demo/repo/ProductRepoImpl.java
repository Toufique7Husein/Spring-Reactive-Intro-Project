package com.curde.demo.repo;

import com.curde.demo.constant.enums.ExceptionType;
import com.curde.demo.entity.ProductEntity;
import com.curde.demo.error.ExceptionFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author Md Toufique Husein
 * @since 19 March 2025
 */


@Slf4j
@Repository
public class ProductRepoImpl extends AbstractDynamoRepository<ProductEntity> implements ProductRepository {

    private final DynamoDbAsyncClient dynamoDbClient;
    private static final String TABLE_NAME = "ProductEntity";

    protected ProductRepoImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedClient, DynamoDbAsyncClient dynamoDbClient) {
        super(TABLE_NAME, dynamoDbEnhancedClient, ProductEntity.class);
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    public void ensureTableExists() {
        checkIfTableExists()
                .thenCompose(exists -> {
                    if (!exists) {
                        log.info("Table '{}' does not exist. Creating...", TABLE_NAME);
                        return createTable();
                    }
                    log.info("Table '{}' already exists.", TABLE_NAME);
                    return CompletableFuture.completedFuture(null);
                })
                .exceptionally(ex -> {
                    log.error("Error checking/creating table: {}", ex.getMessage());
                    return null;
                });
    }

    private CompletableFuture<Boolean> checkIfTableExists() {
        return dynamoDbClient.listTables()
                .thenApply(response -> response.tableNames().contains(TABLE_NAME));
    }

    private CompletableFuture<Void> createTable() {
        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(TABLE_NAME)
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        return dynamoDbClient.createTable(request)
                .thenAccept(result -> log.info("Table '{}' created successfully.", TABLE_NAME));
    }

    @Override
    public Mono<Boolean> saveProduct(ProductEntity productEntity) {
        return save(productEntity)
                .switchIfEmpty(ExceptionFactory.getException(ExceptionType.DYNAMO_DB, "OPERATION_FAILED"));
    }

    @Override
    public Flux<ProductEntity> getAllProduct() {
        return Flux.from(findAllItems().flatMapIterable(Page::items));
    }

    @Override
    public Mono<Boolean> updateProduct(ProductEntity productEntity) {
        return save(productEntity)
                .switchIfEmpty(ExceptionFactory.getException(ExceptionType.DYNAMO_DB, "OPERATION_FAILED"));
    }

    @Override
    public Mono<ProductEntity> getProductByID(String id) {
        return Mono.fromFuture(() -> getItemWithPartitionKey(id));
    }

    @Override
    public Mono<Boolean> deleteProductByID(String id) {
        return getProductByID(id).map(this::convertMonoToEntity).map(v -> true);
    }

    private Mono<Boolean> convertMonoToEntity(ProductEntity productEntity) {
        return Mono.fromFuture(() -> deleteItem(productEntity)).map(item -> true);
    }
}
