-- -- Insert into p_users table
-- INSERT INTO p_users (user_id, email, user_name, password, role)
-- VALUES ('11111111-1111-1111-1111-111111111111', 'test1@example.com', 'test1', 'test1', 'USER'),
--        ('22222222-2222-2222-2222-222222222222', 'test2@example.com', 'test2', 'test2', 'VENDOR_MANAGER'),
--        ('33333333-3333-3333-3333-333333333333', 'test3@example.com', 'test3', 'test3', 'MASTER');
--
-- -- Insert into p_vendors table
-- INSERT INTO p_vendors (vendor_id, vendor_name, vendor_address, user_id)
-- VALUES ('11111111-1111-1111-1111-111111111111', 'test_vendor1', '123 Test St.', '11111111-1111-1111-1111-111111111111'),
--        ('22222222-2222-2222-2222-222222222222', 'test_vendor2', '456 Test Ave.', '22222222-2222-2222-2222-222222222222');
--
-- -- Insert into p_products table
-- INSERT INTO p_products (product_id, product_name, description, product_price, product_image, is_public, vendor_id)
-- VALUES ('11111111-1111-1111-1111-111111111111', 'test_product1', 'Description for product 1', 19.99, 'product1.jpg', true, '11111111-1111-1111-1111-111111111111'),
--        ('22222222-2222-2222-2222-222222222222', 'test_product2', 'Description for product 2', 29.99, 'product2.jpg', true, '22222222-2222-2222-2222-222222222222');
--
-- -- Insert into p_stocks table
-- INSERT INTO p_stocks (stock_id, stock_quantity, product_id)
-- VALUES ('11111111-1111-1111-1111-111111111111', 100, '11111111-1111-1111-1111-111111111111'),
--        ('22222222-2222-2222-2222-222222222222', 200, '22222222-2222-2222-2222-222222222222');
--
-- -- Insert into p_orders table
-- INSERT INTO p_orders (order_id, order_status, order_price, user_id, delivery_id)
-- VALUES ('11111111-1111-1111-1111-111111111111', 'PENDING', 59.98, '11111111-1111-1111-1111-111111111111', '33333333-3333-3333-3333-333333333333');
--
-- -- Insert into p_order_details table
-- INSERT INTO p_order_details (order_detail_id, total_price, order_quantity, product_id)
-- VALUES ('11111111-1111-1111-1111-111111111111', 59.98, 2, '11111111-1111-1111-1111-111111111111');