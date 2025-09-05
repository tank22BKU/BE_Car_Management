package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.data.PaymentMethods;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.data.PaymentOptions;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethods paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_option", nullable = false)
    PaymentOptions paymentOption;

    @Column(name = "invoice", nullable = false)
    String invoice;

    @Column(name = "price", nullable = false)
    Float price;

    @Column(name = "initial_payment", nullable = false)
    Float initialPayment;

    @Column(name = "payment_date", nullable = false)
    OffsetDateTime paymentDate;
}
