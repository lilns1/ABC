// src/main/java/org/example/abc/dto/PaymentResponse.java
package org.example.abc.dto;

import lombok.Getter;
import org.example.abc.model.PaymentRecord;

import java.time.LocalDateTime;

@Getter
public class PaymentResponse {
    // Getters（无 setters，只读）
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer productCount;
    private Float amount;
    private String paymentStatus;
    private LocalDateTime paymentTime;
    private String paymentRef;

    // 静态工厂方法：从 PaymentRecord 转换
    public static PaymentResponse from(PaymentRecord record) {
        PaymentResponse res = new PaymentResponse();
        res.id = record.getId();
        res.userId = record.getUser().getId();
        res.productId = record.getProduct().getId();
        res.productCount = record.getProductCount();
        res.amount = record.getAmount();
        res.paymentStatus = record.getPaymentStatus().name();
        res.paymentTime = record.getPaymentTime();
        res.paymentRef = record.getPaymentRef();
        return res;
    }

}