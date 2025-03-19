package com.curde.demo.entity;


import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * @author Md Toufique Husein
 * @since 13/3/25
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
@Builder
public class ProductEntity {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String id;
    private String name;
    private double price;
    private double weight;
}
