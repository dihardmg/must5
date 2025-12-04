-- Sync sequence value to avoid ID conflicts between reactive and blocking entities
-- Both ReactiveOrder and Order entities use the same 'orders' table

-- Set orders_SEQ to current max ID + 1 to avoid conflicts
SELECT setval('orders_SEQ', COALESCE((SELECT MAX(id) FROM orders), 0) + 1);

-- Set order_items_SEQ to current max ID + 1 to avoid conflicts
SELECT setval('order_items_SEQ', COALESCE((SELECT MAX(id) FROM order_items), 0) + 1);

-- Sequences are now synchronized and ready for use