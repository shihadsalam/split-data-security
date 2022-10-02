
Use master;

CREATE TABLE hibernate_sequence (
  next_val bigint(20)
);
INSERT INTO hibernate_sequence (next_val) VALUES (1);

CREATE TABLE credit_card (
  id bigint(20) NOT NULL,
  card_number_part1 varchar(255) DEFAULT NULL,
  card_type varchar(255) DEFAULT NULL,
  cvv varchar(255) DEFAULT NULL,
  expiry varchar(255) DEFAULT NULL,
  pin_part1 varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE customer (
  id bigint(20) NOT NULL,
  age int(11) NOT NULL,
  name varchar(255) DEFAULT NULL,
  ssn_part1 varchar(255) DEFAULT NULL,
  card_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (card_id) REFERENCES credit_card(id)
);

CREATE TABLE address (
  id bigint(20) NOT NULL,
  house_no varchar(255) DEFAULT NULL,
  pin_code int(11) DEFAULT NULL,
  street varchar(255) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

/*——————————————————————————————*/

Use split;

CREATE TABLE credit_card_part2 (
  id bigint(20) NOT NULL,
  card_number_part2 varchar(255) DEFAULT NULL,
  pin_part2 varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE customer_part2 (
  id bigint(20) NOT NULL,
  ssn_part2 varchar(255) DEFAULT NULL,
  card_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (card_id) REFERENCES credit_card_part2(id)
);

/*——————————————————————————————*/

Use client;

CREATE TABLE customer (
  id bigint(20) NOT NULL,
  age int(11) NOT NULL,
  name varchar(255) DEFAULT NULL,
  ssn varchar(255) DEFAULT NULL,
  card_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (card_id) REFERENCES credit_card(id)
);

CREATE TABLE address (
  id bigint(20) NOT NULL,
  house_no varchar(255) DEFAULT NULL,
  pin_code int(11) DEFAULT NULL,
  street varchar(255) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE credit_card (
  id bigint(20) NOT NULL,
  card_number varchar(255) DEFAULT NULL,
  card_type varchar(255) DEFAULT NULL,
  cvv varchar(255) DEFAULT NULL,
  expiry varchar(255) DEFAULT NULL,
  pin varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

/*—————————DROP TABLE—————————————*/

drop table split.customer_part2;
drop table split.credit_card_part2;
drop table master.hibernate_sequence;
drop table master.address;
drop table master.customer;
drop table master.credit_card;