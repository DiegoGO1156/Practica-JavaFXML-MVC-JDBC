-- Drop database if exists superDB;

create database if not exists superDB;

use superDB;

insert into Clientes(nombre, apellido,telefono, direccion, nit) values
('Carlos','Perez', '2481-5135','Ciudad', 'CF'),
('Mario','Estrada', '2481-8264','Ciudad', 'CF');

