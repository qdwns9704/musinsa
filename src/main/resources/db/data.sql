-- Brand 테이블에 샘플 데이터 삽입
INSERT INTO brand (id, name)
VALUES
    (1, 'Nike'),
    (2, 'Adidas'),
    (3, 'Puma'),
    (4, 'Under Armour'),
    (5, 'Reebok'),
    (6, 'New Balance'),
    (7, 'Asics'),
    (8, 'Skechers'),
    (9, 'Converse'),
    (10, 'Vans');

-- Category 테이블에 샘플 데이터 삽입
INSERT INTO category (id, name)
VALUES
    (1, 'Shoes'),
    (2, 'Sportswear'),
    (3, 'Accessories'),
    (4, 'Equipment'),
    (5, 'Casual'),
    (6, 'Running'),
    (7, 'Basketball'),
    (8, 'Fitness'),
    (9, 'Outdoor'),
    (10, 'Lifestyle');

-- Product 테이블에 반복문을 사용하여 만 개의 샘플 데이터 삽입 (MySQL 프로시저 사용)
DELIMITER $$

CREATE PROCEDURE InsertProducts()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE brandId INT;
    DECLARE categoryId INT;
    DECLARE price DECIMAL(10, 2);
    DECLARE createdAt DATETIME;
    DECLARE updatedAt DATETIME;

    WHILE i <= 10000 DO
        SET brandId = FLOOR(1 + RAND() * 10);
        SET categoryId = FLOOR(1 + RAND() * 10);
        SET price = ROUND(RAND() * (500.00 - 10.00) + 10.00, 2);
        SET createdAt = NOW() - INTERVAL FLOOR(RAND() * 100) DAY;
        SET updatedAt = createdAt + INTERVAL FLOOR(RAND() * 30) DAY;

INSERT INTO product (brand_id, category_id, name, price, created_at, updated_at)
VALUES (brandId, categoryId, CONCAT('Product_', i), price, createdAt, updatedAt);

SET i = i + 1;
END WHILE;
END$$

DELIMITER ;

-- 프로시저 호출
CALL InsertProducts();