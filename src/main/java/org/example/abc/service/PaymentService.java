// src/main/java/org/example/abc/service/PaymentService.java
package org.example.abc.service;

import org.example.abc.dto.CreatePaymentRequest;
import org.example.abc.dto.PaymentResponse;
import org.example.abc.model.PaymentRecord;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
    PaymentResponse completePayment(String paymentRef);
    PaymentResponse refundPayment(String paymentRef);
}