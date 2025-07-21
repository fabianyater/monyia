package com.fyrdev.monyia.configuration.exceptionhandler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private String url;
    private long timestamp;
}
