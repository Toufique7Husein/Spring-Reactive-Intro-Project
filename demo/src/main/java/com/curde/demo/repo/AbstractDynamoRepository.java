package com.curde.demo.repo;
import com.curde.demo.error.DynamoDbException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.curde.demo.constant.DynamoDbConstant.MAX_BATCH_ITEM_COUNT;
import static com.curde.demo.constant.DynamoDbConstant.MAX_BATCH_READ_ITEM_COUNT;

@Slf4j
public abstract class AbstractDynamoRepository<T> {
    private final DynamoDbAsyncTable<T> mappedTable;
    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedClient;
    private final Class<T> tClazz;

    protected AbstractDynamoRepository(String tableName, DynamoDbEnhancedAsyncClient dynamoDbEnhancedClient, Class<T> tClass) {
        log.info("Init table mapping for table {}", tableName);
        this.mappedTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(tClass));
        log.info("Table mapping for table {}", tableName);
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.tClazz = tClass;
    }

    public CompletableFuture<T> getItemWithPartitionKey(String partitionKey) {

        Key key = Key.builder()
                .partitionValue(partitionKey)
                .build();

        return mappedTable.getItem(r -> r.key(key));
    }

    public CompletableFuture<T> getItemWithPartitionAndSortKey(String partitionKey, String sortKey) {

        Key key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        return mappedTable.getItem(r -> r.key(key));
    }

    public CompletableFuture<T> getItemWithPartitionAndSortKey(String partitionKey, long sortKey) {

        Key key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        return mappedTable.getItem(r -> r.key(key));
    }

    public PagePublisher<T> queryItemWithOnlyPartitionKey(String partitionKey) {

        var queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(partitionKey)
                        .build());

        return mappedTable.query(r -> r.queryConditional(queryConditional));
    }

    public PagePublisher<T> queryItemWithOnlyPartitionKeySortedInDescOrder(String partitionKey) {


        var queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(partitionKey)
                        .build());

        return mappedTable.query(r -> r.queryConditional(queryConditional).scanIndexForward(false));


    }

    public SdkPublisher<T> getItemBySortKeyGreaterThanTheValue(String partitionKey, long sortKey, int limit) {

        return getItemBySortKeyGreaterThanTheValue(partitionKey, sortKey).limit(limit);
    }

    public SdkPublisher<T> getItemBySortKeyGreaterThanTheValue(String partitionKey, long sortKey) {

        return mappedTable.query(QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.sortGreaterThan(Key.builder()
                        .partitionValue(partitionKey)
                        .sortValue(sortKey)
                        .build()))
                .scanIndexForward(false)
                .build()).items();
    }

    public SdkPublisher<T> getItemBySortKeyStartWith(String partitionKey, String sortKey) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build());

        QueryEnhancedRequest build = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();

        return mappedTable.query(build).items();
    }

    public CompletableFuture<Void> saveItem(T t) {

        return mappedTable.putItem(t);
    }

    public Mono<Boolean> save(T t) {
        return Mono.fromFuture(mappedTable.putItem(t)
                .thenApply(saved -> true));
    }

    public Mono<Boolean> update(T t) {
        return Mono.fromFuture(mappedTable.updateItem(UpdateItemEnhancedRequest.builder(tClazz)
                        .item(t)
                        .ignoreNulls(true)
                        .build())
                .thenApply(saved -> true));
    }

    public PagePublisher<T> findAllItems() {

        return mappedTable.scan(ScanEnhancedRequest.builder().consistentRead(true).build());
    }

    public CompletableFuture<T> deleteItem(T t) {

        return mappedTable.deleteItem(t);
    }

    protected SdkPublisher<T> getByPartitionKey(String partitionKey, String sortKey, int limit) {
        QueryConditional queryConditional = QueryConditional.sortGreaterThan(Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build());

        var queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();

        return mappedTable.query(queryRequest).items().limit(limit);
    }

    public SdkPublisher<T> getWithGsi(String partitionKey, String gsiValue, String index) {

        return getWithGsi(Key.builder()
                        .partitionValue(partitionKey)
                        .sortValue(gsiValue)
                        .build(),
                index);

    }

    public SdkPublisher<T> getWithGsi(Key key, String indexName) {

        DynamoDbAsyncIndex<T> secondaryIndex = mappedTable.index(indexName);

        var queryConditional = QueryConditional.keyEqualTo(key);

        return secondaryIndex.query(
                QueryEnhancedRequest.builder()
                        .queryConditional(queryConditional)
                        .scanIndexForward(false)
                        .build()
        ).flatMapIterable(Page::items);
    }

    public SdkPublisher<T> getByGsiKeyEqualsTo(Key key, String indexName, int limit) {

        DynamoDbAsyncIndex<T> secondaryIndex = mappedTable.index(indexName);

        var queryConditional = QueryConditional.keyEqualTo(key);
        var queryEnhancedRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();

        return secondaryIndex.query(queryEnhancedRequest)
                .flatMapIterable(Page::items)
                .limit(limit);
    }

    public Flux<T> getByGsiGreaterThanOrEqualTo(Key key, String indexName, int limit) {
        DynamoDbAsyncIndex<T> secondaryIndex = mappedTable.index(indexName);

        var queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.sortGreaterThanOrEqualTo(key))
                .scanIndexForward(false)
                .limit(limit)
                .build();

        return Mono.fromDirect(secondaryIndex.query(queryRequest))
                .flatMapMany(page -> Flux.fromIterable(page.items()));
    }

    public Mono<List<T>> saveBatch(List<T> tList) {
        return partitionBatches(tList)
                .flatMap(batchPart -> Mono.fromFuture(batchWrite(batchPart))
                        .map(batchWriteResult -> batchWriteResult.unprocessedPutItemsForTable(mappedTable))
                ).concatMapIterable(failedList -> failedList)
                .collectList();

    }

    protected SdkPublisher<T> query(QueryEnhancedRequest request, String index) {
        return mappedTable.index(index).query(request).flatMapIterable(Page::items);
    }

    private CompletableFuture<BatchWriteResult> batchWrite(List<T> tList) {

        validateBatchItemCount(tList.size());

        var writeBatchBuilder = WriteBatch.builder(this.tClazz)
                .mappedTableResource(mappedTable);

        tList.forEach(writeBatchBuilder::addPutItem);
        return dynamoDbEnhancedClient
                .batchWriteItem(BatchWriteItemEnhancedRequest
                        .builder()
                        .writeBatches(writeBatchBuilder.build())
                        .build()
                );
    }

    protected Flux<T> batchReadInChunks(List<T> tList) {
        List<List<T>> partitions = Lists.partition(tList, MAX_BATCH_READ_ITEM_COUNT);

        return Flux.fromIterable(partitions)
                .flatMap(this::batchRead);
    }

    private Flux<T> batchRead(List<T> tList) {
        validateBatchReadItemCount(tList.size());

        var readBatchBuilder = ReadBatch.builder(this.tClazz)
                .mappedTableResource(mappedTable);

        tList.forEach(readBatchBuilder::addGetItem);

        var readBatch = readBatchBuilder.build();

        var batchResults = dynamoDbEnhancedClient.batchGetItem(
                BatchGetItemEnhancedRequest.builder()
                        .readBatches(readBatch)
                        .build());

        return Flux.from(batchResults.resultsForTable(mappedTable));
    }

    private void validateBatchReadItemCount(int itemCount) {
        if (itemCount > MAX_BATCH_READ_ITEM_COUNT) {
            log.error("Maximum batch read item count {} exceeded", MAX_BATCH_READ_ITEM_COUNT);
            throw new DynamoDbException(String.format("Maximum batch read item count %d exceeded", MAX_BATCH_READ_ITEM_COUNT));
        }
    }

    private void validateBatchItemCount(int itemCount) {
        if (itemCount > MAX_BATCH_ITEM_COUNT) {
            log.error("Maximum batch item count {} exceeded", MAX_BATCH_ITEM_COUNT);
            throw new DynamoDbException(String.format("Maximum batch item count %d exceeded", MAX_BATCH_ITEM_COUNT));
        }
    }

    private Flux<List<T>> partitionBatches(List<T> tList) {
        int batchCount = (tList.size() + MAX_BATCH_ITEM_COUNT - 1) / MAX_BATCH_ITEM_COUNT;

        return Flux.fromIterable(
                IntStream.range(0, batchCount)
                        .mapToObj(batchIndex -> {
                            int startIndex = batchIndex * MAX_BATCH_ITEM_COUNT;
                            int endIndex = Math.min(startIndex + MAX_BATCH_ITEM_COUNT, tList.size());
                            return tList.subList(startIndex, endIndex);
                        })
                        .toList()
        );
    }
}