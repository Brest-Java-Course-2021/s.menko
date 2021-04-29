DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS OrderDetail;
DROP TABLE IF EXISTS productOrder;


CREATE TABLE category 
(
id int NOT NULL AUTO_INCREMENT,
name varchar(255) NOT NULL,
sort_order int NOT NULL DEFAULT 0,
status int NOT NULL DEFAULT 1,
PRIMARY KEY (id)) ENGINE=InnoDB AUTO_INCREMENT=1;


CREATE TABLE product (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255)  NOT NULL,
  category_id int NOT NULL,
  price float NOT NULL,
  availability int NOT NULL,
  brand varchar(255)  NOT NULL,
  image varchar(255)  NOT NULL DEFAULT 'sdsd',
  description text  NOT NULL,
  is_new int NOT NULL DEFAULT 0,
  status int NOT NULL DEFAULT 1,
  code varchar(45) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY `FK_DETAIL_idx` (`category_id`),
  CONSTRAINT `FK_DETAIL` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE user (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  role varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1;





CREATE TABLE productOrder (
  id INT(11) NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(255) NOT NULL,
  user_phone VARCHAR(255) NOT NULL,
  user_comment TEXT NULL,
  user_id INT(11) NULL,
  data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status INT(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  KEY `FK1` (`user_id`),
  CONSTRAINT `FK_DETAIL1` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE=InnoDB AUTO_INCREMENT=1;
  
  
  CREATE TABLE OrderDetail (
  id INT(11) NOT NULL AUTO_INCREMENT,
  order_id INT(11) NOT NULL,
  product_id INT(11) NOT NULL,
  quantity INT(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY `FK_DETAIL_idx3` (`order_id`),
  CONSTRAINT `FK_DETAIL3` FOREIGN KEY (`order_id`) 
  REFERENCES `productOrder` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  KEY `FK_DETAIL_idx1` (`product_id`),
  CONSTRAINT `dsasa` FOREIGN KEY (`product_id`) 
  REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE=InnoDB AUTO_INCREMENT=1;

