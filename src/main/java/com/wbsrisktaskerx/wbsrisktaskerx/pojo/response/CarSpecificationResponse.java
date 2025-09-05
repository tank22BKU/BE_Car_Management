package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarSpecificationResponse {
    Integer id;
    String engineLabel;
    Double displacement;
    Integer maxSpeed;
    String maxPower;
    String maxTorque;
    Double acceleration;
    Double brakingDistance;
    Integer numberOfCylinders;
    Integer valvesOfCylinders;
    String transmissionType;
    String fuelType;
    Integer fuelTank;
    Double overallLength;
    Double overallWidth;
    Double overallHeight;
    Double wheelBase;
    Double frontWheelTread;
    Double rearWheelTread;
    Double lightestCurbWeight;
    Double heaviestCurbWeight;
    Double grossVehicleWeight;
}
