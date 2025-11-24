package org.example.abc.controller;

import org.example.abc.dto.PaymentRecordDto;
import org.example.abc.model.PaymentRecord;
import org.example.abc.repository.PaymentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentRecordDto>> getPaymentRecordsByUserId(@PathVariable Integer userId) {
        List<PaymentRecord> records = paymentRecordRepository.findByUserId(userId);
        List<PaymentRecordDto> dtos = records.stream()
                .map(PaymentRecordDto::new) // 直接引用构造方法
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}