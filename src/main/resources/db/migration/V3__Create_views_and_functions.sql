-- Create view for customer spending summary
CREATE OR REPLACE VIEW customer_spending_view AS
SELECT
    o.customer_name,
    COUNT(o.id) as total_orders,
    COALESCE(SUM(o.total_amount), 0) as total_spending,
    COALESCE(AVG(o.total_amount), 0) as average_order_value,
    MIN(o.order_date) as first_order_date,
    MAX(o.order_date) as last_order_date
FROM orders o
GROUP BY o.customer_name
ORDER BY total_spending DESC;

-- Create function to get customer ranking
CREATE OR REPLACE FUNCTION get_customer_ranking(customer_name_param VARCHAR)
RETURNS INTEGER AS $$
DECLARE
    customer_rank INTEGER;
BEGIN
    SELECT COUNT(*) + 1
    INTO customer_rank
    FROM (
        SELECT customer_name, SUM(total_amount) as total_spent
        FROM orders
        GROUP BY customer_name
        HAVING SUM(total_amount) > COALESCE(
            (SELECT SUM(total_amount)
             FROM orders
             WHERE customer_name = customer_name_param), 0)
    ) higher_spenders;

    RETURN customer_rank;
END;
$$ LANGUAGE plpgsql;

-- Add comment to tables for documentation
COMMENT ON TABLE orders IS 'Main orders table containing order header information';
COMMENT ON TABLE order_items IS 'Order items table containing product details for each order';
COMMENT ON VIEW customer_spending_view IS 'View showing spending summary per customer';