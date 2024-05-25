-- Drop database if exists superDB;

-- create database if not exists superDB;

use superDB;

SELECT DATABASE();

insert into Clientes(nombre, apellido,telefono, direccion, nit) values
	('Carlos','Perez', '2481-5135','Ciudad', 'CF'),
	('Mario','Estrada', '2481-8264','Ciudad', 'CF');

insert into Distribuidores(nombreDistribuidor, direccionDistribuidor,nitDistribuidor, telefonoDistribuidor, web) values
	('CANOE','Ciudad', '2481-5','415645', 'CF');

insert into CategoriaProductos(nombreCategoria, descripcionCategoria) values
	('Comestibles','Son alimentos que se pueden compar para el consumo propio');

insert into Compras(fechaCompra, totalCompra) values
	('2023-02-15', 156.25);

insert into Empleados(nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId) values
	('Diego', 'Garcia', 502, '12:30', '20:30', 1);
    
/*insert into Productos(nombreProducto, descripcionProducto, precioVentaUnitario, precioVentaMayor, precioCompra, ) values
	('2023-02-15', 156.25);
    */
  -- call sp_listarEmpleados();
  
  select * from Empleados;
  
  