package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EntityConstant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = EntityConstant.SPAREPART_DETAIL_TABLE)
public class SparepartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "material", nullable = false)
    String material;

    @Column(name = "material_description", nullable = false)
    String materialDescription;

    @Column(name = "friction_coefficient", nullable = false)
    String frictionCoefficient;

    @Column(name = "friction_coefficient_description", nullable = false)
    String frictionCoefficientDescription;

    @Column(name = "lifespan", nullable = false)
    String lifespan;

    @Column(name = "lifespan_description", nullable = false)
    String lifespanDescription;

    @Column(name = "warranty", nullable = false)
    String warranty;

    @Column(name = "warranty_description", nullable = false)
    String warrantyDescription;

    @Column(name = "weight", nullable = false)
    String weight;

    @Column(name = "weight_description", nullable = false)
    String weightDescription;

    @Column(name = "packaging_size", nullable = false)
    String packagingSize;

    @Column(name = "packaging_size_description", nullable = false)
    String packagingSizeDescription;

    @Column(name = "unit_of_measurement", nullable = false)
    String unitOfMeasurement;

    @Column(name = "unit_of_measurement_description", nullable = false)
    String unitOfMeasurementDescription;

    @Column(name = "thickness", nullable = false)
    String thickness;

    @Column(name = "thickness_description", nullable = false)
    String thicknessDescription;

    @OneToMany(mappedBy = "sparepartDetail")
    Set<Sparepart> sparepart;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "sparepart_branch_detail",
            joinColumns = @JoinColumn(name = "sparepart_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "sparepart_branch_id")
    )
    List<SparepartBranch> sparepartBranches;

    @PreRemove
    void preRemove() {
        // Set detail_id references of spareparts to null when removing their details
        for (Sparepart sparepart : this.sparepart) {
            sparepart.setSparepartDetail(null);
        }
        // clear the immediate records
        this.sparepartBranches.clear();
    }
}
