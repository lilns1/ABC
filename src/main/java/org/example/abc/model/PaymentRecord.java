package org.example.abc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_records",
        uniqueConstraints = @UniqueConstraint(columnNames = "payment_ref"),
        indexes = {
                @Index(name = "idx_user_payment", columnList = "user_id"),
                @Index(name = "idx_product_payment", columnList = "product_id")
        })
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_count", nullable = false)
    private Integer productCount;

    @Column(nullable = false)
    private Float amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.Pending;

    @Column(name = "payment_time", updatable = false)
    private LocalDateTime paymentTime = LocalDateTime.now();

    @Column(name = "payment_ref", nullable = false)
    private String paymentRef;

    public enum PaymentStatus {
        Pending, Completed, Refunded
    }

    // Getters & Setters...
}