package com.ghtk.auction.repository;

import com.ghtk.auction.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query(
            value = """
    SELECT 
        p.id,
        p.auction_id,
        p.buyer_id,
        p.status,
        p.created_at,
        p.updated_at,
        p.deposit_amount,
        p.payment_method,
        p.deadline,
        p.date_payment

    FROM payment p
    ORDER BY p.id
    LIMIT :pageSize OFFSET :offset
    """,
            nativeQuery = true
    )
    List<Payment> search(@Param("offset") Long offset, @Param("pageSize") int pageSize);

    @Query(
            value = """
    SELECT 
        p.id,
        p.auction_id,
        p.buyer_id,
        p.status,
        p.created_at,
        p.updated_at,
        p.deposit_amount,
        p.payment_method,
        p.deadline,
        p.date_payment
    FROM payment p
    WHERE p.buyer_id = :buyerId
    ORDER BY p.id
    LIMIT :pageSize OFFSET :offset
    """,
            nativeQuery = true
    )
    List<Payment> searchMyPayment(@Param("buyerId") Long buyerId, @Param("offset") Long offset, @Param("pageSize") int pageSize);

}
