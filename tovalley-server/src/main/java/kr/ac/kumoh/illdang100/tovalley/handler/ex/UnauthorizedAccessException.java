package kr.ac.kumoh.illdang100.tovalley.handler.ex;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
