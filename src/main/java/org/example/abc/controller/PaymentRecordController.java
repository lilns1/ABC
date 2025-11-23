package org.example.abc.controller;

import org.example.abc.model.PaymentRecord;
import org.example.abc.repository.PaymentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-records")
public class PaymentRecordController {

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    // 查询所有支付记录
    @GetMapping
    public ResponseEntity<List<PaymentRecord>> getAllPaymentRecords() {
        List<PaymentRecord> records = paymentRecordRepository.findAll();
        return ResponseEntity.ok(records);
    }

    // 根据 ID 查询单个支付记录
    @GetMapping("/{id}")
    public ResponseEntity<PaymentRecord> getPaymentRecordById(@PathVariable Integer id) {
        Optional<PaymentRecord> record = paymentRecordRepository.findById(id);
        return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 按用户 ID 查询支付记录
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentRecord>> getPaymentRecordsByUserId(@PathVariable Integer userId) {
        List<PaymentRecord> records = paymentRecordRepository.findByUserId(userId);
        return ResponseEntity.ok(records);
    }
}