// src/main/java/org/example/abc/exception/PaymentException.java
package org.example.abc.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}