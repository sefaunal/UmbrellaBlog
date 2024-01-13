package com.sefaunal.umbrellagateway.Exception;

/**
 * @author github.com/sefaunal
 * @since 2024-12-23
 */
public class InvalidTokenFormatException extends RuntimeException {
    public InvalidTokenFormatException(String message) {
        super(message);
    }
}