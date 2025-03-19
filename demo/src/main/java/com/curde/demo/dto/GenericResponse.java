package com.curde.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Md Toufique Husein
 * @since 19 March 2025
 * */
@Data
@Builder
public class GenericResponse<T> {
    private String message;
    private LocalDateTime responseTime;
    private T data;
}
