-- Fix missing sequences for reactive orders
-- Create sequences that are needed for reactive operations

CREATE SEQUENCE IF NOT EXISTS orders_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS order_items_SEQ START WITH 1 INCREMENT BY 1;