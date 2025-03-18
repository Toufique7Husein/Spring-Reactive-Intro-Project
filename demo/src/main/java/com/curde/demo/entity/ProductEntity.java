package com.curde.demo.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * @author Md Toufique Husein
 * @since 13/3/25
 * */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ProductEntity {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String id;
    private String name;
    private double price;
    private double weight;
}
