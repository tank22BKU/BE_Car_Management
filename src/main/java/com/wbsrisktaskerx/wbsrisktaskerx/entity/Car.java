package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EntityConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = EntityConstant.CAR_TABLE)
public class Car extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, length = 100)
    String model;

    @Column(length = 50, nullable = false)
    String variant;

    @Column(name = "vehicle_type", nullable = false, length = 50)
    String vehicleType;

    @Column(precision = 15, scale = 2, nullable = false)
    @Min(value = 0)
    BigDecimal price;

    @Column(name = "image_url", length = 255)
    String imageUrl;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "car_detail_id", foreignKey = @ForeignKey(name = "fk_car_detail"))
    CarDetail carDetail;
}