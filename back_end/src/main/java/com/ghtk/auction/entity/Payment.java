package com.ghtk.auction.entity;

import com.ghtk.auction.enums.PaymentMethod;
import com.ghtk.auction.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payment")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    @JoinColumn(name = "auction_id")
    Auction auction;
    @ManyToOne
    @JoinColumn(name = "buyer_id" )
    User buyer;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    PaymentStatus status;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
    @Column(name = "deposit_amount")
    Long depositAmount;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    PaymentMethod paymentMethod;
    @Column(name = "deadline")
    LocalDateTime deadline;
    @Column(name = "date_payment")
    LocalDateTime datePayment;

}
