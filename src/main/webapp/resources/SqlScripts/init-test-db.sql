INSERT INTO category (name,sort_order) VALUES ( 'Платья',1);
INSERT INTO category (name,sort_order) VALUES ( 'Футболки',2);
INSERT INTO category (name,sort_order) VALUES ( 'Чемоданы',3);
INSERT INTO category (name,sort_order) VALUES ( 'Брюки',4);
INSERT INTO category (name,sort_order) VALUES ( 'Пиджаки',5);
INSERT INTO category (name,sort_order) VALUES ( 'Галстуки',6);

INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№1', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№2', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№3', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№4', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№5', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№6', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№7', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№8', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№9', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);
INSERT INTO product (name, category_id, price, availability, brand, description, is_new, status, code) 
VALUES ('Платье№10', 1, 234, 1, 'Nike', 'dsadasd', 1, 1, 1123);

INSERT INTO user (name, email, password, role) VALUES ('stas', 'sm@mail.ru',
'$2a$10$NU.U7G6yiF9FNikSD.XmcuTd49INdVPnkcAq30zGWHBUOfbBSq0SG', 'admin');
