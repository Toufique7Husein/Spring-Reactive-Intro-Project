package com.curde.demo.error;

import com.curde.demo.constant.enums.ExceptionType;
import reactor.core.publisher.Mono;

import java.awt.geom.IllegalPathStateException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystemNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;

public class ExceptionFactory {
    private ExceptionFactory() {
    }

    public static <T> Mono<T> getException(ExceptionType exceptionType, String errorCode) {
        return Mono.error(assemble(exceptionType, errorCode));
    }

    private static Throwable assemble(ExceptionType exceptionType, String errorCode) {
         if (ExceptionType.ARITHMETIC.equals(exceptionType)) {

            return new ArithmeticException(errorCode);
        } else if (ExceptionType.ILLEGAL_ARGUMENT.equals(exceptionType)) {

            return new IllegalArgumentException(errorCode);
        } else if (ExceptionType.ILLEGAL_PATH_STATE.equals(exceptionType)) {

            return new IllegalPathStateException(errorCode);
        } else if (ExceptionType.NO_SUCH_ELEMENT.equals(exceptionType)) {

            return new NoSuchElementException(errorCode);
        } else if (ExceptionType.NULL_POINTER.equals(exceptionType)) {

            return new NullPointerException(errorCode);
        } else if (ExceptionType.PROVIDER.equals(exceptionType)) {

            return new ProviderException(errorCode);
        } else if (ExceptionType.REJECTED_EXECUTION.equals(exceptionType)) {

            return new RejectedExecutionException(errorCode);
        } else if (ExceptionType.FILE_SYSTEM.equals(exceptionType)) {

            return new FileSystemException(errorCode);
        } else if (ExceptionType.FILE_SYSTEM_ALREADY_EXISTS.equals(exceptionType)) {

            return new FileSystemAlreadyExistsException(errorCode);
        } else if (ExceptionType.FILE_SYSTEM_NOT_FOUND.equals(exceptionType)) {

            return new FileSystemNotFoundException(errorCode);
        } else if (ExceptionType.SECURITY.equals(exceptionType)) {

            return new SecurityException(errorCode);
        } else if (ExceptionType.UNSUPPORTED_OPERATION.equals(exceptionType)) {

            return new UnsupportedOperationException(errorCode);
        } else if (ExceptionType.DYNAMO_DB.equals(exceptionType)) {

            return new DynamoDbException(errorCode);
        } else if (ExceptionType.NO_SUCH_ALGORITHM.equals(exceptionType)) {

            return new NoSuchAlgorithmException(errorCode);
        } else {
            return new RuntimeException(errorCode);
        }
    }

}
