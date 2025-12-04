-- Fix missing sequences for reactive orders
-- Execute this in PostgreSQL to create missing sequences

CREATE SEQUENCE IF NOT EXISTS orders_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS order_items_SEQ START WITH 1 INCREMENT BY 1;

-- Verify sequences were created
SELECT sequence_name FROM information_schema.sequences
WHERE sequence_schema = 'public' AND sequence_name IN ('orders_SEQ', 'order_items_SEQ');