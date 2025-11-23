// src/main/java/org/example/abc/controller/PaymentController.java
package org.example.abc.controller;

import jakarta.validation.Valid;
import org.example.abc.dto.CreatePaymentRequest;
import org.example.abc.dto.PaymentResponse;
import org.example.abc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{paymentRef}/complete")
    public ResponseEntity<PaymentResponse> completePayment(@PathVariable String paymentRef) {
        PaymentResponse response = paymentService.completePayment(paymentRef);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentRef}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable String paymentRef) {
        PaymentResponse response = paymentService.refundPayment(paymentRef);
        return ResponseEntity.ok(response);
    }
}