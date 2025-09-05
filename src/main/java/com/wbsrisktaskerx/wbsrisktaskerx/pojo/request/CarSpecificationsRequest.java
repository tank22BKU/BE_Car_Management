package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarSpecificationsRequest {
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