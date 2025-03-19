package com.curde.demo.error;


public class DynamoDbException extends RuntimeException {
    public DynamoDbException() {
    }

    public DynamoDbException(String message) {
        super(message);
    }
}