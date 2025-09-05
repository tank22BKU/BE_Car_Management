-- Create sparepart_branch table
CREATE TABLE IF NOT EXISTS `sparepart_branch`
(
    id                INTEGER PRIMARY KEY AUTO_INCREMENT,
    branch_name       VARCHAR(255) NOT NULL,
    location_code     VARCHAR(255) NOT NULL,
    current_stock     BIGINT       NOT NULL,
    retail_price      BIGINT       NOT NULL,
    last_restock_date DATETIME     NOT NULL
);


CREATE TABLE IF NOT EXISTS `sparepart_detail`
(
    id                                INTEGER PRIMARY KEY AUTO_INCREMENT,
    material                          VARCHAR(255) NOT NULL,
    material_description              VARCHAR(255) NOT NULL,
    friction_coefficient              VARCHAR(255) NOT NULL,
    friction_coefficient_description  VARCHAR(255) NOT NULL,
    lifespan                          VARCHAR(255) NOT NULL,
    lifespan_description              VARCHAR(255) NOT NULL,
    warranty                          VARCHAR(255) NOT NULL,
    warranty_description              VARCHAR(255) NOT NULL,
    weight                            VARCHAR(255) NOT NULL,
    weight_description                VARCHAR(255) NOT NULL,
    packaging_size                    VARCHAR(255) NOT NULL,
    packaging_size_description        VARCHAR(255) NOT NULL,
    unit_of_measurement               VARCHAR(255) NOT NULL,
    unit_of_measurement_description   VARCHAR(255) NOT NULL,
    thickness                         VARCHAR(255) NOT NULL,
    thickness_description             VARCHAR(255) NOT NULL
);

-- Create join table for ManyToMany relationship between SparepartDetail and SparepartBranch
CREATE TABLE IF NOT EXISTS `sparepart_branch_detail`
(
    sparepart_detail_id INTEGER NOT NULL,
    sparepart_branch_id INTEGER NOT NULL,
    PRIMARY KEY (sparepart_detail_id, sparepart_branch_id),
    FOREIGN KEY (sparepart_detail_id) REFERENCES sparepart_detail (id) ON DELETE CASCADE,
    FOREIGN KEY (sparepart_branch_id) REFERENCES sparepart_branch (id) ON DELETE CASCADE
);

-- Check and drop 'description' column if it exists
SET @column_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'spare_parts'
    AND COLUMN_NAME = 'description'
    AND TABLE_SCHEMA = DATABASE()
);

SET @sql = IF(@column_exists, 'ALTER TABLE spare_parts DROP COLUMN description', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and drop 'created_at' column if it exists
SET @column_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'spare_parts'
    AND COLUMN_NAME = 'created_at'
    AND TABLE_SCHEMA = DATABASE()
);

SET @sql = IF(@column_exists, 'ALTER TABLE spare_parts DROP COLUMN created_at', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and drop 'updated_at' column if it exists
SET @column_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'spare_parts'
    AND COLUMN_NAME = 'updated_at'
    AND TABLE_SCHEMA = DATABASE()
);

SET @sql = IF(@column_exists, 'ALTER TABLE spare_parts DROP COLUMN updated_at', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE `spare_parts`
    MODIFY COLUMN name VARCHAR(50) NOT NULL,
    MODIFY COLUMN manufacturer VARCHAR(50) NOT NULL,
    MODIFY COLUMN price DOUBLE NOT NULL,
    ADD COLUMN compatible_vehicle VARCHAR(50) NOT NULL DEFAULT 'Unknown',
    ADD COLUMN quantity BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN sparepart_detail_id INTEGER,
    ADD COLUMN is_active BOOL NOT NULL DEFAULT FALSE,
    ADD COLUMN is_deleted BOOL NOT NULL DEFAULT FALSE,
    ADD CONSTRAINT fk_sparepart_detail
    FOREIGN KEY (sparepart_detail_id) REFERENCES sparepart_detail(id) ON DELETE SET NULL;

-- ALTER TABLE `spare_parts`
--     DROP COLUMN IF EXISTS description,
--     DROP COLUMN IF EXISTS created_at,
--     DROP COLUMN IF EXISTS updated_at;
-- Optional: Add CHECK constraints for MySQL 8.0+
ALTER TABLE spare_parts
    ADD CONSTRAINT check_quantity CHECK (quantity >= 0),
    ADD CONSTRAINT check_price CHECK (price >= 0);

INSERT INTO sparepart_branch (branch_name, location_code, current_stock, retail_price, last_restock_date)
VALUES ('Quy Nhon Branch', 'QN001', 100, 2999, '2025-06-30 10:41:00'),
       ('Da Nang Branch', 'DN002', 100, 2999, '2025-06-30 10:41:00'),
       ('Ho Chi Minh Branch', 'HCM003', 100, 2999, '2025-06-30 10:41:00');

INSERT INTO sparepart_detail (material, material_description, friction_coefficient, friction_coefficient_description,
                              lifespan, lifespan_description, warranty,
                              warranty_description, weight, weight_description, packaging_size,
                              packaging_size_description, unit_of_measurement, unit_of_measurement_description,
                              thickness, thickness_description)
VALUES ('Steel', 'High-grade steel', '0.5', 'Moderate friction', '24 months',
        'Two-year lifespan', '1 year', 'Standard warranty', '2.5 kg', 'Lightweight', '10x10x5 cm', 'Compact packaging',
        'kg', 'Kilograms', '5 mm', 'Thin profile');

INSERT INTO sparepart_branch_detail (sparepart_detail_id, sparepart_branch_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);

INSERT INTO sparepart_branch (branch_name, location_code, current_stock, retail_price, last_restock_date)
VALUES
    ('Hanoi Branch', 'HN004', 150, 3200, '2025-07-01 09:00:00'),
    ('Hai Phong Branch', 'HP005', 120, 3100, '2025-07-01 10:00:00'),
    ('Can Tho Branch', 'CT006', 80, 3000, '2025-07-01 11:00:00'),
    ('Nha Trang Branch', 'NT007', 90, 3150, '2025-07-01 12:00:00'),
    ('Vung Tau Branch', 'VT008', 110, 3050, '2025-07-01 13:00:00'),
    ('Hue Branch', 'HU009', 100, 2980, '2025-07-01 14:00:00'),
    ('Buon Ma Thuot Branch', 'BMT010', 95, 3100, '2025-07-01 15:00:00'),
    ('Da Lat Branch', 'DL011', 85, 3200, '2025-07-01 16:00:00'),
    ('Thanh Hoa Branch', 'TH012', 130, 2950, '2025-07-01 17:00:00'),
    ('Vinh Branch', 'VI013', 100, 3000, '2025-07-01 18:00:00'),
    ('Phu Quoc Branch', 'PQ014', 70, 3250, '2025-07-01 19:00:00'),
    ('Bien Hoa Branch', 'BH015', 140, 3050, '2025-07-01 20:00:00');


INSERT INTO sparepart_detail (material, material_description, friction_coefficient, friction_coefficient_description,
                              lifespan, lifespan_description, warranty,
                              warranty_description, weight, weight_description, packaging_size,
                              packaging_size_description, unit_of_measurement, unit_of_measurement_description,
                              thickness, thickness_description)
VALUES
    ('Aluminum', 'Lightweight aluminum alloy', '0.4', 'Low friction', '36 months', 'Three-year lifespan', '18 months', 'Extended warranty', '1.8 kg', 'Ultra-light', '12x8x4 cm', 'Small packaging', 'kg', 'Kilograms', '4 mm', 'Extra thin'),
    ('Carbon Fiber', 'High-strength carbon fiber', '0.6', 'High friction', '48 months', 'Four-year lifespan', '2 years', 'Premium warranty', '1.2 kg', 'Featherweight', '15x10x5 cm', 'Standard packaging', 'kg', 'Kilograms', '3 mm', 'Ultra-thin profile'),
    ('Titanium', 'Durable titanium alloy', '0.45', 'Moderate friction', '30 months', 'Two-and-a-half-year lifespan', '1 year', 'Standard warranty', '2.0 kg', 'Lightweight', '10x10x6 cm', 'Compact design', 'kg', 'Kilograms', '6 mm', 'Medium thickness'),
    ('Ceramic', 'High-performance ceramic', '0.7', 'Very high friction', '60 months', 'Five-year lifespan', '3 years', 'Long-term warranty', '3.0 kg', 'Moderate weight', '14x12x5 cm', 'Medium packaging', 'kg', 'Kilograms', '7 mm', 'Thick profile'),
    ('Stainless Steel', 'Corrosion-resistant steel', '0.5', 'Moderate friction', '24 months', 'Two-year lifespan', '1 year', 'Standard warranty', '2.8 kg', 'Medium weight', '11x9x5 cm', 'Compact packaging', 'kg', 'Kilograms', '5 mm', 'Standard thickness'),
    ('Brass', 'High-durability brass', '0.55', 'Moderate friction', '36 months', 'Three-year lifespan', '18 months', 'Extended warranty', '2.3 kg', 'Lightweight', '12x10x4 cm', 'Small packaging', 'kg', 'Kilograms', '4.5 mm', 'Thin profile'),
    ('Copper', 'Conductive copper alloy', '0.65', 'High friction', '30 months', 'Two-and-a-half-year lifespan', '1 year', 'Standard warranty', '2.7 kg', 'Moderate weight', '13x11x5 cm', 'Standard packaging', 'kg', 'Kilograms', '6 mm', 'Medium thickness'),
    ('Plastic Composite', 'Reinforced plastic', '0.3', 'Low friction', '18 months', 'One-and-a-half-year lifespan', '1 year', 'Standard warranty', '1.5 kg', 'Ultra-light', '10x8x4 cm', 'Compact design', 'kg', 'Kilograms', '3 mm', 'Ultra-thin'),
    ('Rubber', 'High-grip rubber compound', '0.8', 'Very high friction', '12 months', 'One-year lifespan', '6 months', 'Short-term warranty', '1.0 kg', 'Featherweight', '8x8x3 cm', 'Small packaging', 'kg', 'Kilograms', '2 mm', 'Very thin'),
    ('Graphite', 'Lubricated graphite', '0.2', 'Very low friction', '24 months', 'Two-year lifespan', '1 year', 'Standard warranty', '1.7 kg', 'Lightweight', '12x9x5 cm', 'Compact packaging', 'kg', 'Kilograms', '4 mm', 'Thin profile'),
    ('Alloy Steel', 'High-strength alloy steel', '0.5', 'Moderate friction', '36 months', 'Three-year lifespan', '2 years', 'Extended warranty', '2.4 kg', 'Medium weight', '11x10x5 cm', 'Standard packaging', 'kg', 'Kilograms', '5.5 mm', 'Medium thickness'),
    ('Polymer', 'Durable polymer', '0.35', 'Low friction', '18 months', 'One-and-a-half-year lifespan', '1 year', 'Standard warranty', '1.3 kg', 'Ultra-light', '10x9x4 cm', 'Small packaging', 'kg', 'Kilograms', '3.5 mm', 'Thin profile');

-- Insert relationships into sparepart_branch_detail
-- Assuming sparepart_detail_id starts from 2 (after your initial 1) and sparepart_branch_id starts from 4 (after your initial 3)
INSERT INTO sparepart_branch_detail (sparepart_detail_id, sparepart_branch_id)
VALUES
    (2, 4), (2, 5), (2, 6), -- Aluminum in Hanoi, Hai Phong, Can Tho
    (3, 4), (3, 7), (3, 8), -- Carbon Fiber in Hanoi, Nha Trang, Vung Tau
    (4, 5), (4, 6), (4, 9), -- Titanium in Hai Phong, Can Tho, Hue
    (5, 7), (5, 8), (5, 10), -- Ceramic in Nha Trang, Vung Tau, Buon Ma Thuot
    (6, 6), (6, 9), (6, 11), -- Stainless Steel in Can Tho, Hue, Da Lat
    (7, 4), (7, 10), (7, 12), -- Brass in Hanoi, Buon Ma Thuot, Thanh Hoa
    (8, 5), (8, 11), (8, 13), -- Copper in Hai Phong, Da Lat, Vinh
    (9, 6), (9, 12), (9, 14), -- Plastic Composite in Can Tho, Thanh Hoa, Phu Quoc
    (10, 7), (10, 13), (10, 15), -- Rubber in Nha Trang, Vinh, Bien Hoa
    (11, 8), (11, 14), (11, 4), -- Graphite in Vung Tau, Phu Quoc, Hanoi
    (12, 9), (12, 15), (12, 5), -- Alloy Steel in Hue, Bien Hoa, Hai Phong
    (13, 10), (13, 11), (13, 6); -- Polymer in Buon Ma Thuot, Da Lat, Can Tho

INSERT INTO `spare_parts` VALUES (1, "TYT123", 100, "Toyota Manu" , "TOYOTA", 10, 11,true, FALSE), (2, "TYT223",100 ,"Toyota Manu2" , "TOYOTA2", 10, 12, true, FALSE);

INSERT INTO spare_parts (`name`, `price`, `manufacturer`, `compatible_vehicle`, `quantity`, `sparepart_detail_id`,
                         `is_active`, `is_deleted`)
VALUES
    ('Brake Pad', 29.99, 'Bosch', 'Toyota Camry', 100, 4, TRUE, FALSE),
    ('Oil Filter', 15.50, 'Mann-Filter', 'Honda Civic', 50, 5, TRUE, FALSE),
    ('Air Filter', 12.75, 'K&N', 'Ford Mustang', 75, 7, TRUE, FALSE),
    ('Spark Plug', 8.99, 'NGK', 'Hyundai Tucson', 200, 8, TRUE, FALSE),
    ('Alternator', 120.00, 'Denso', 'Nissan Altima', 30, 6, FALSE, FALSE),
    ('Radiator', 89.95, 'Valeo', 'Chevrolet Cruze', 25, 1, TRUE, FALSE),
    ('Clutch Kit', 150.50, 'Sachs', 'Volkswagen Golf', 15, 2, TRUE, FALSE),
    ('Timing Belt', 45.00, 'Gates', 'Mitsubishi Lancer', 60, 3, TRUE, FALSE),
    ('Battery', 99.99, 'Bosch', 'BMW X5', 40, 9, FALSE, FALSE),
    ('Shock Absorber', 65.25, 'Monroe', 'Jeep Wrangler', 80, 10, TRUE, FALSE);