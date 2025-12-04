-- Clean and simple sample data insertion

-- Reset the database first (for testing purposes)
-- TRUNCATE TABLE order_items RESTART IDENTITY CASCADE;
-- TRUNCATE TABLE orders RESTART IDENTITY CASCADE;

-- Insert individual customers with orders and items
-- Budi Santoso - Gaming Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Budi Santoso', '2024-01-15', 15500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
RETURNING id;

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Laptop Gaming ASUS ROG', 1, 15000000.00, (SELECT id FROM orders WHERE customer_name = 'Budi Santoso' AND order_date = '2024-01-15')),
('Mouse Gaming Razer', 1, 500000.00, (SELECT id FROM orders WHERE customer_name = 'Budi Santoso' AND order_date = '2024-01-15'));

-- Siti Nurhaliza - Office Equipment
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Siti Nurhaliza', '2024-01-16', 8500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Laptop Dell Inspiron', 1, 8000000.00, (SELECT id FROM orders WHERE customer_name = 'Siti Nurhaliza' AND order_date = '2024-01-16')),
('Mouse Logitech', 1, 500000.00, (SELECT id FROM orders WHERE customer_name = 'Siti Nurhaliza' AND order_date = '2024-01-16'));

-- Ahmad Fauzi - Professional Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Ahmad Fauzi', '2024-01-17', 22500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('MacBook Pro 16', 1, 22000000.00, (SELECT id FROM orders WHERE customer_name = 'Ahmad Fauzi' AND order_date = '2024-01-17')),
('Magic Mouse', 1, 500000.00, (SELECT id FROM orders WHERE customer_name = 'Ahmad Fauzi' AND order_date = '2024-01-17'));

-- Dewi Lestari - Student Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Dewi Lestari', '2024-01-18', 6500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('iPad Air', 1, 6500000.00, (SELECT id FROM orders WHERE customer_name = 'Dewi Lestari' AND order_date = '2024-01-18'));

-- Rizki Pratama - Creative Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Rizki Pratama', '2024-01-19', 18000000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('iMac 24', 1, 18000000.00, (SELECT id FROM orders WHERE customer_name = 'Rizki Pratama' AND order_date = '2024-01-19'));

-- Maya Putri - Mobile Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Maya Putri', '2024-01-20', 9500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('iPhone 15', 1, 9000000.00, (SELECT id FROM orders WHERE customer_name = 'Maya Putri' AND order_date = '2024-01-20')),
('AirPods', 1, 500000.00, (SELECT id FROM orders WHERE customer_name = 'Maya Putri' AND order_date = '2024-01-20'));

-- Eko Widodo - Enterprise Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Eko Widodo', '2024-01-21', 32000000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('ThinkPad X1 Carbon', 1, 25000000.00, (SELECT id FROM orders WHERE customer_name = 'Eko Widodo' AND order_date = '2024-01-21')),
('Monitor ThinkVision', 2, 3500000.00, (SELECT id FROM orders WHERE customer_name = 'Eko Widodo' AND order_date = '2024-01-21'));

-- Indah Permata - Budget Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Indah Permata', '2024-01-22', 4250000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Chromebook', 1, 4250000.00, (SELECT id FROM orders WHERE customer_name = 'Indah Permata' AND order_date = '2024-01-22'));

-- Bambang Sutrisno - Developer Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Bambang Sutrisno', '2024-01-23', 12750000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Linux Laptop HP', 1, 12000000.00, (SELECT id FROM orders WHERE customer_name = 'Bambang Sutrisno' AND order_date = '2024-01-23')),
('Mechanical Keyboard', 1, 750000.00, (SELECT id FROM orders WHERE customer_name = 'Bambang Sutrisno' AND order_date = '2024-01-23'));

-- Ratna Sari - Home Setup
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Ratna Sari', '2024-01-24', 7800000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Samsung Galaxy Tab', 1, 7800000.00, (SELECT id FROM orders WHERE customer_name = 'Ratna Sari' AND order_date = '2024-01-24'));

-- Repeat customers for testing analytics
-- Budi Santoso second order
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Budi Santoso', '2024-01-25', 6500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Monitor LG 27', 1, 6500000.00, (SELECT id FROM orders WHERE customer_name = 'Budi Santoso' AND order_date = '2024-01-25'));

-- Siti Nurhaliza second order
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at)
VALUES ('Siti Nurhaliza', '2024-01-26', 3200000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Webcam Logitech', 1, 1200000.00, (SELECT id FROM orders WHERE customer_name = 'Siti Nurhaliza' AND order_date = '2024-01-26')),
('Headset Jabra', 1, 2000000.00, (SELECT id FROM orders WHERE customer_name = 'Siti Nurhaliza' AND order_date = '2024-01-26'));

-- International customers for testing special characters
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at) VALUES
('José García', '2024-02-01', 3500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Müller GmbH', '2024-02-02', 2800000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('International Product 1', 1, 3500000.00, (SELECT id FROM orders WHERE customer_name = 'José García' AND order_date = '2024-02-01')),
('International Product 2', 1, 2800000.00, (SELECT id FROM orders WHERE customer_name = 'Müller GmbH' AND order_date = '2024-02-02'));

-- Small businesses for testing
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at) VALUES
('PT Teknologi Maju', '2024-02-05', 125000000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
('Laptop HP ProBook', 5, 25000000.00, (SELECT id FROM orders WHERE customer_name = 'PT Teknologi Maju' AND order_date = '2024-02-05'));

-- Add some simple orders for pagination testing
INSERT INTO orders (customer_name, order_date, total_amount, created_at, updated_at) VALUES
('Test User 1', '2024-02-10', 1000000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Test User 2', '2024-02-11', 1100000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Test User 3', '2024-02-12', 1200000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Test User 4', '2024-02-13', 1300000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Test User 5', '2024-02-14', 1400000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert items for test users
DO $$
DECLARE
    i INTEGER;
BEGIN
    FOR i IN 1..5 LOOP
        INSERT INTO order_items (product_name, quantity, price, order_id) VALUES
        ('Test Product ' || i, 1, 1000000.00 + (i * 100000),
         (SELECT id FROM orders WHERE customer_name = 'Test User ' || i));
    END LOOP;
END $$;