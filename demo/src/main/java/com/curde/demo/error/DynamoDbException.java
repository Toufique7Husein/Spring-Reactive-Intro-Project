package com.curde.demo.error;


public class DynamoDbException extends RuntimeException {
    public DynamoDbException() {
    }

    public DynamoDbException(String message) {
        super(message);
    }

    public DynamoDbException(Throwable cause) {
        super(cause);
    }

    public DynamoDbException(String message, Throwable cause) {
        super(message, cause);
    }
}