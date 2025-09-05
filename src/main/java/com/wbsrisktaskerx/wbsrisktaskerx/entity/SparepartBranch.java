package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EntityConstant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = EntityConstant.SPAREPART_BRANCH_TABLE)
public class SparepartBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "branch_name", nullable = false)
    String branchName;

    @Column(name = "location_code", nullable = false)
    String locationCode;

    @Column(name = "current_stock", nullable = false)
    Long currentStock;

    @Column(name = "retail_price", nullable = false)
    Long retailPrice;

    @Column(name = "last_restock_date", nullable = false)
    OffsetDateTime lastRestockDate;

    @ManyToMany(mappedBy = "sparepartBranches", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<SparepartDetail> sparepartDetails;

    @PreRemove
    void preRemove() {
        //clear the immediate records
        this.sparepartDetails.clear();
    }
}
