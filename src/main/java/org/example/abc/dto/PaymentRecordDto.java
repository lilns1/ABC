// src/main/java/org/example/abc/dto/PaymentRecordDto.java

package org.example.abc.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.abc.model.PaymentRecord;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentRecordDto {

    private Integer id;
    private Integer userId;
    private String userName;      // 👈 新增
    private Integer productId;
    private String productName;   // 👈 新增
    private Integer productCount;
    private Float amount;
    private String paymentStatus;
    private LocalDateTime paymentTime;
    private String paymentRef;

    public PaymentRecordDto() {}

    public PaymentRecordDto(PaymentRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("PaymentRecord cannot be null");
        }
        this.id = record.getId();
        this.productCount = record.getProductCount();
        this.amount = record.getAmount();
        this.paymentStatus = record.getPaymentStatus() != null ? record.getPaymentStatus().name() : null;
        this.paymentTime = record.getPaymentTime();
        this.paymentRef = record.getPaymentRef();

        // 安全获取 user 信息（因为用了 EntityGraph，user 已被 eager 加载）
        if (record.getUser() != null) {
            this.userId = record.getUser().getId();
            this.userName = record.getUser().getName(); // 假设 User 有 getName()
        }

        // 安全获取 product 信息（同样已 eager 加载）
        if (record.getProduct() != null) {
            this.productId = record.getProduct().getId();
            this.productName = record.getProduct().getName(); // 假设 Product 有 getName()
        }
    }
}