package org.example.abc.repository;

import org.example.abc.model.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Integer> {
    List<PaymentRecord> findByUserId(Integer userId);
}