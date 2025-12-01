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

        // 2. 检查库存是否足够
        if (product.getStock() < request.getProductCount()) {
            throw new PaymentException("库存不足");
        }

        // 3. 扣减库存（预占）
        product.setStock(product.getStock() - request.getProductCount());
        productRepository.save(product); // 触发更新

        // 4. 计算金额
        Float amount = product.getPrice() * request.getProductCount();

        // 5. 生成唯一支付流水号
        String paymentRef = request.getPaymentRef() != null ?
                request.getPaymentRef() : "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 6. 创建支付记录（状态为 Pending）
        PaymentRecord record = new PaymentRecord();
        record.setUser(user);
        record.setProduct(product);
        record.setProductCount(request.getProductCount());
        record.setAmount(amount);
        record.setPaymentStatus(PaymentRecord.PaymentStatus.Pending);
        record.setPaymentTime(LocalDateTime.now());
        record.setPaymentRef(paymentRef);

        // 7. 保存支付记录
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

        // 只更新状态为 Completed
        record.setPaymentStatus(PaymentRecord.PaymentStatus.Completed);
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

        // 允许 Pending（取消订单）和 Completed（真实退款）都走退款流程
        if (record.getPaymentStatus() == PaymentRecord.PaymentStatus.Pending ||
                record.getPaymentStatus() == PaymentRecord.PaymentStatus.Completed) {

            // 恢复库存
            Product product = record.getProduct();
            product.setStock(product.getStock() + record.getProductCount());
            productRepository.save(product);

            // 更新状态为 Refunded
            record.setPaymentStatus(PaymentRecord.PaymentStatus.Refunded);
            PaymentRecord updated = paymentRecordRepository.save(record);
            return PaymentResponse.from(updated);
        }

        // 理论上不会走到这里，但保留防御性
        throw new PaymentException("无效的支付状态，无法退款");
    }
}