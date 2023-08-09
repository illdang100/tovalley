package kr.ac.kumoh.illdang100.tovalley.handler.ex;

import lombok.Getter;

import java.util.Map;

@Getter
public class OpenApiException extends RuntimeException {

    private Map<String, String> errorMap;

    public OpenApiException(String message, Map<String, String>errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
