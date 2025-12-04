-- Remove failed V10 migration from Flyway history
DELETE FROM flyway_schema_history WHERE version = '10';

-- Check sequences are properly set
SELECT 'orders_seq' as sequence_name, last_value FROM orders_SEQ
UNION ALL
SELECT 'order_items_seq' as sequence_name, last_value FROM order_items_SEQ;