package com.sefaunal.umbrellachat.Exception;

/**
 * @author github.com/sefaunal
 * @since 2023-12-28
 */
public class OkHttpRequestException extends RuntimeException {
    public OkHttpRequestException(String message) {
        super(message);
    }

    public OkHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
