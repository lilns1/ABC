// src/main/java/org/example/abc/service/impl/PaymentServiceImpl.java
package org.example.abc.service.impl;

import org.example.abc.dto.CreatePaymentRequest;
import org.example.abc.dto.PaymentResponse;
import org.example.abc.exception.PaymentException;
import org.example.abc.model.PaymentRecord;
import org.example.abc.model.Product;
import org.example.abc.model.User;
import org.example.abc.repository.PaymentRecordRepository;
import org.example.abc.repository.ProductRepository;
import org.example.abc.repository.UserRepository;
import org.example.abc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        // 1. 验证用户和商品是否存在
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new PaymentException("用户不存在"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new PaymentException("商品不存在"));

        // 2. 计算金额（示例：单价 × 数量）
        Float amount = product.getPrice() * request.getProductCount();

        // 3. 生成唯一支付流水号（paymentRef）
        String paymentRef = request.getPaymentRef() != null ?
                request.getPaymentRef() : "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 4. 创建支付记录（状态为 Pending）
        PaymentRecord record = new PaymentRecord();
        record.setUser(user);
        record.setProduct(product);
        record.setProductCount(request.getProductCount());
        record.setAmount(amount);
        record.setPaymentStatus(PaymentRecord.PaymentStatus.Pending);
        record.setPaymentTime(LocalDateTime.now());
        record.setPaymentRef(paymentRef);

        // 5. 保存到数据库
        PaymentRecord saved = paymentRecordRepository.save(record);
        return PaymentResponse.from(saved);
    }

    @Override
    @Transactional
    public PaymentResponse completePayment(String paymentRef) {
        PaymentRecord record = paymentRecordRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new PaymentException("支付记录不存在"));

        if (record.getPaymentStatus() == PaymentRecord.PaymentStatus.Completed) {
            throw new PaymentException("支付已完成");
        }
        if (record.getPaymentStatus() == PaymentRecord.PaymentStatus.Refunded) {
            throw new PaymentException("支付已退款，无法完成");
        }

        record.setPaymentStatus(PaymentRecord.PaymentStatus.Completed);
        // 注意：paymentTime 不更新（因为 updatable = false）
        PaymentRecord updated = paymentRecordRepository.save(record);
        return PaymentResponse.from(updated);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(String paymentRef) {
        PaymentRecord record = paymentRecordRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new PaymentException("支付记录不存在"));

        if (record.getPaymentStatus() == PaymentRecord.PaymentStatus.Refunded) {
            throw new PaymentException("已退款");
        }
        if (record.getPaymentStatus() == PaymentRecord.PaymentStatus.Pending) {
            throw new PaymentException("未支付，无法退款");
        }

        record.setPaymentStatus(PaymentRecord.PaymentStatus.Refunded);
        PaymentRecord updated = paymentRecordRepository.save(record);
        return PaymentResponse.from(updated);
    }
}