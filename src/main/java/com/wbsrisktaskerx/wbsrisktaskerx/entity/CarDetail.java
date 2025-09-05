package com.wbsrisktaskerx.wbsrisktaskerx.entity;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EntityConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = EntityConstant.CAR_DETAIL_TABLE)
public class CarDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "engine_label")
    String engineLabel;

    @Column(name = "displacement")
    Double displacement;

    @Column(name = "max_speed")
    @Min(value = 0)
    Integer maxSpeed;

    @Column(name = "max_power")
    String maxPower;

    @Column(name = "max_torque")
    String maxTorque;

    @Column(name = "acceleration")
    @Min(value = 0)
    Double acceleration;

    @Column(name = "braking_distance")
    @Min(value = 0)
    Double brakingDistance;

    @Column(name = "number_of_cylinders")
    @Min(value = 0)
    Integer numberOfCylinders;

    @Column(name = "valves_of_cylinders")
    @Min(value = 0)
    Integer valvesOfCylinders;

    @Column(name = "transmission_type")
    String transmissionType;

    @Column(name = "fuel_type")
    String fuelType;

    @Column(name = "fuel_tank")
    @Min(value = 0)
    Integer fuelTank;

    @Column(name = "overall_length")
    @Min(value = 0)
    Double overallLength;

    @Column(name = "overall_width")
    @Min(value = 0)
    Double overallWidth;

    @Column(name = "overall_height")
    @Min(value = 0)
    Double overallHeight;

    @Column(name = "wheel_base")
    @Min(value = 0)
    Double wheelBase;

    @Column(name = "front_wheel_tread")
    @Min(value = 0)
    Double frontWheelTread;

    @Column(name = "rear_wheel_tread")
    @Min(value = 0)
    Double rearWheelTread;

    @Column(name = "lightest_curb_weight")
    @Min(value = 0)
    Double lightestCurbWeight;

    @Column(name = "heaviest_curb_weight")
    @Min(value = 0)
    Double heaviestCurbWeight;

    @Column(name = "gross_vehicle_weight")
    @Min(value = 0)
    Double grossVehicleWeight;

    @OneToMany(mappedBy = "carDetail")
    List<Car> cars;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = EntityConstant.CAR_BRANCH_DETAIL_TABLE,
            joinColumns = @JoinColumn(name = "car_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "car_branch_id")
    )
    Set<CarBranch> branches;

    @PreRemove
    void removeCarReferences() {
        if (cars != null) {
            for (Car car : cars) {
                car.setCarDetail(null);
            }
        }
        this.branches.clear();
    }
}

