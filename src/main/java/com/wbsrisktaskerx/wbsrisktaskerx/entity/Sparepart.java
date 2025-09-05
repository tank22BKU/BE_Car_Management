package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EntityConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = EntityConstant.SPAREPART_TABLE)
public class Sparepart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name", length = 50, nullable = false)
    String fullName;

    @Column(name = "manufacturer", length = 50, nullable = false)
    String manufacturer;

    @Column(name = "compatible_vehicle", length = 50, nullable = false)
    String compatibleVehicleType;

    @Column(name = "quantity", nullable = false)
    @Min(0)
    Long quantity;

    @Column(name = "price", nullable = false)
    @Min(0)
    Double price;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Column(name = "is_deleted", nullable = false)
    Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "sparepart_detail_id", referencedColumnName = "id" ,nullable = true)
    SparepartDetail sparepartDetail;
}
