// src/main/java/org/example/abc/dto/PaymentRecordDto.java

package org.example.abc.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.abc.model.PaymentRecord;

import java.time.LocalDateTime;

@Setter
@Getter

public class PaymentRecordDto {

    // Getters and Setters
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer productCount;
    private Float amount;
    private String paymentStatus; // 使用 String 而不是 enum，便于 JSON 序列化
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

        // 安全地获取关联 ID（避免触发懒加载代理的 deep load）
        // Hibernate 代理对象即使未初始化，也能通过 getId() 获取主键（因为 ID 已知）
        this.userId = record.getUser() != null ? record.getUser().getId() : null;
        this.productId = record.getProduct() != null ? record.getProduct().getId() : null;
    }

}