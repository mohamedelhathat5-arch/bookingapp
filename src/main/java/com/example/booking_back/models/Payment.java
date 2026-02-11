package com.example.booking_back.models;

import com.example.booking_back.models.enums.PaymentMethod;
import com.example.booking_back.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;       // BANK_TRANSFER, CASH, CARD, etc.

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;       // PENDING, COMPLETED, REFUNDED, FAILED

    private LocalDateTime paymentDate;

    private String transactionReference;   // from Stripe, bank, etc.

    private String notes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
