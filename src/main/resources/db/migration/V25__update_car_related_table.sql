-- Remove unused field
ALTER TABLE `cars`
    DROP FOREIGN KEY cars_ibfk_1,
    DROP FOREIGN KEY cars_ibfk_2;

ALTER TABLE `cars`
    DROP COLUMN brand_id,
    DROP COLUMN manufacture_year,
    DROP COLUMN description,
    ADD COLUMN is_deleted boolean NOT NULL DEFAULT false,
    CHANGE COLUMN version_name variant VARCHAR(50),
    CHANGE COLUMN category_id vehicle_type VARCHAR(50) NOT NULL;

-- DROP TABLE IF EXISTS `car_brands`;

-- Create a car detail table
CREATE TABLE `car_details`(
    id INT PRIMARY KEY AUTO_INCREMENT,
    engine_label VARCHAR(50),
    displacement DOUBLE PRECISION,
    max_speed INT,
    max_power VARCHAR(50),
    max_torque VARCHAR(50),
    acceleration DOUBLE PRECISION,
    braking_distance DOUBLE PRECISION,
    number_of_cylinders INT,
    valves_of_cylinders INT,
    transmission_type VARCHAR(50),
    fuel_type VARCHAR(50),
    fuel_tank INT,
    overall_length DOUBLE PRECISION,
    overall_width DOUBLE PRECISION,
    overall_height DOUBLE PRECISION,
    wheel_base DOUBLE PRECISION,
    front_wheel_tread DOUBLE PRECISION,
    rear_wheel_tread DOUBLE PRECISION,
    lightest_curb_weight DOUBLE PRECISION,
    heaviest_curb_weight DOUBLE PRECISION,
    gross_vehicle_weight DOUBLE PRECISION,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create car branch table
CREATE TABLE `car_branches` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    branch_name VARCHAR(50) NOT NULL UNIQUE,
    available_for_sales INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create car branch_detail table
CREATE TABLE IF NOT EXISTS `car_branch_detail`
(
    car_detail_id INTEGER NOT NULL,
    car_branch_id INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (car_detail_id, car_branch_id),
    FOREIGN KEY (car_detail_id) REFERENCES car_details (id) ON DELETE CASCADE,
    FOREIGN KEY (car_branch_id) REFERENCES car_branches (id) ON DELETE CASCADE
);

-- Add reference detail to car
ALTER TABLE `cars`
    ADD COLUMN car_detail_id INT DEFAULT NULL,
    ADD CONSTRAINT fk_car_detail
        FOREIGN KEY (car_detail_id) REFERENCES `car_details` (id) ON DELETE SET NULL;
