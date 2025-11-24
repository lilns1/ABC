package org.example.abc.repository;

import org.example.abc.model.PaymentRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Integer> {

    @EntityGraph(attributePaths = {"user", "product"})
    List<PaymentRecord> findByUserId(Integer userId);

    Optional<PaymentRecord> findByPaymentRef(String paymentRef);
}