package com.curde.demo.error;

import com.curde.demo.constant.enums.ExceptionType;
import reactor.core.publisher.Mono;

public class ExceptionFactory {
    private ExceptionFactory() {
    }

    public static <T> Mono<T> getException(ExceptionType exceptionType, String errorCode) {
        return Mono.error(assemble(exceptionType, errorCode));
    }

    private static Throwable assemble(ExceptionType exceptionType, String errorCode) {
        if (ExceptionType.DYNAMO_DB.equals(exceptionType)) {
            return new DynamoDbException(errorCode);
        } else {
            return new RuntimeException(errorCode);
        }
    }

}
