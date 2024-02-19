create table tb_goods(
  goods_id varchar(64) primary key,
  goods_name varchar not null,
  unit varchar(16),
  hot int,
  goods_code varchar(32),
  selling_price decimal(10,2),
  buying_price decimal(10,2),
  description varchar,
  create_time timestamp,
  update_time timestamp
);

create table tb_order(
  order_no varchar(32) primary key,
  create_time timestamp,
  total_price decimal(10,2),
  order_date date,
  chinese_price varchar(64),
  customer varchar(128),
  address varchar(255),
  phone varchar(32)
);

create table tb_order_detail(
  detail_id varchar(128) primary key,
  order_no varchar(32),
  goods_id varchar(64),
  goods_name varchar(64),
  unit varchar(16),
  price decimal(10,2),
  amount int
);

create table tb_order_tmp(
  goods_id varchar(64)  primary key,
  amount int,
  num int
);