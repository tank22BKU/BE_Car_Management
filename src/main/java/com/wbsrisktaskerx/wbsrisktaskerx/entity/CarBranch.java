package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EntityConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = EntityConstant.CAR_BRANCH_TABLE)
public class CarBranch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "branch_name", nullable = false, unique = true, length = 50)
    String branchName;

    @Column(name = "available_for_sales", nullable = false)
    @Min(value = 0)
    Integer availableForSales;

    @ManyToMany(mappedBy = "branches", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<CarDetail> carDetails;

    @PreRemove
    private void removeBranchFromCarDetails() {
        this.carDetails.clear();
    }
}
