
use superDB;

-- CRUD Clientes

-- Agregar
Delimiter $$
create procedure sp_AgregarClientes(in nom varchar(30), in ape varchar (30), in tel varchar(15), in dir varchar (200), in ni varchar(11))
	Begin
		insert into Clientes(nombre, apellido, telefono, direccion, nit) values
			(nom, ape, tel, dir, ni);
	End$$
Delimiter ;

call sp_AgregarClientes('Javier','Aguilar', '2481-1282','Ciudad', 'CF');

-- Listar
Delimiter $$
create procedure sp_listarClientes()
	begin
		select
			Clientes.clienteId,
            Clientes.nombre,
            Clientes.apellido,
            Clientes.telefono,
            Clientes.nit,
            Clientes.direccion
				from Clientes;
	end $$
Delimiter ;

-- Eliminar
Delimiter $$
create procedure sp_EliminarCliente(in cliId int)
	begin 
		delete from Clientes
			where clienteId = cliId;
	end $$
Delimiter ;

call sp_EliminarCliente(4);

-- Buscar
Delimiter $$
create procedure sp_BuscarCliente(in cliId int)
	begin
		select
            Clientes.clienteId,
            Clientes.nombre,
            Clientes.apellido,
            Clientes.telefono,
            Clientes.nit,
            Clientes.direccion
				from Clientes
					where clienteId = cliId;
	end $$
delimiter ;
     
-- call sp_BuscarCliente(1);     
     
-- Editar
Delimiter $$
create procedure sp_EditarCliente(in cliId int, in nom varchar(30), in ape varchar (30), in tel varchar(15), in dir varchar (200), in ni varchar(11))
	begin
		update Clientes
			set
				nombre = nom,
                apellido = ape,
                telefono = tel,
                direccion = dir,
                nit = ni
					where clienteId = cliId;
	end $$
Delimiter ;

call sp_listarClientes();

-- Cargos

-- Agregar
Delimiter $$
create procedure sp_AgregarCargo(in nomCar varchar(30), in descripCar varchar (100))
	Begin
		insert into Cargos(nombreCargo, descripcioCargo) values
			(nomCar, descripCar);
	End$$
Delimiter ;

-- call sp_AgregarClientes('Javier','Aguilar', '2481-1282','Ciudad', 'CF');

-- Listar
Delimiter $$
create procedure sp_ListarCargo()
	begin
		select
			Cargos.cargoId,
			Cargos.nombreCargo,
            Cargos.descripcionCargo
				from Clientes;
	End $$
Delimiter ;

-- Eliminar
Delimiter $$
create procedure sp_EliminarCargo(in cargId int)
	begin 
		delete from Cargos
			where cargoId = cargId;
	End $$
Delimiter ;

-- call sp_EliminarCargo(4);

-- Buscar
Delimiter $$
create procedure sp_BuscarCargo(in cargId int)
	begin
		select
			Cargos.cargoId,
            Cargos.nombreCargo,
            Cargos.descripcionCargo
				from Cargos
					where cargoId = cargId;
	End $$
delimiter ;
     
-- call sp_BuscarCargo(1);     
     
-- Editar
Delimiter $$
create procedure sp_EditarCargo(in nomCar varchar(30), in descripCar varchar (100))
	begin
		update Cargos
			set
				nombreCargo = nomCar,
                descripcionCargo = descripCar
					where cargoId = cargId;
	End $$
Delimiter ;

-- call sp_listarCargo();

-- Categorias Producto

-- Agregar
Delimiter $$
create procedure sp_AgregarCategoriaProducto(in nomCateg varchar(30), in descripCateg varchar (100))
	Begin
		insert into CategoriaProductos(nombreCategoria, descripcioCategoria) values
			(nomCateg, descripCateg);
	End$$
Delimiter ;

-- call sp_AgregarClientes('Javier','Aguilar', '2481-1282','Ciudad', 'CF');

-- Listar
Delimiter $$
create procedure sp_ListarCategoriaProducto()
	begin
		select
			CategoriaProductos.categoriaProductosId,
			CategoriaProductos.nombreCategoria,
            CategoriaProductos.descripcionCategoria
				from CategoriaProductos;
	End $$
Delimiter ;

-- Eliminar
Delimiter $$
create procedure sp_EliminarCategoriaProducto(in categId int)
	begin 
		delete from CategoriaProductos
			where categoriaProductosId = categId;
	End $$
Delimiter ;

-- call sp_EliminarCategoriaProducto(4);

-- Buscar
Delimiter $$
create procedure sp_BuscarCategoriaProducto(in categId int)
	begin
		select
			CategoriaProductos.categoriaProductosId,
            CategoriaProductos.nombreCategoria,
            CategoriaProductos.descripcionCategoria
				from CategoriaProductos
					where categoriaProductosId = categId;
	End $$
delimiter ;
     
-- call sp_BuscarCategoriaProducto(1);     
     
-- Editar
Delimiter $$
Create procedure sp_EditarCategoriaProducto(in nomCateg varchar(30), in descripCateg varchar (100))
	Begin
		Update CategoriaProductos
			set
				nombreCategoria = nomCateg,
                descripcionCateg = descripCateg
					where categoriaProductosId = categId;
	End $$
Delimiter ;

-- call sp_listarCategoriaProducto();

-- Distribuidores

-- Agregar
Delimiter $$
create procedure sp_AgregarDistribuidor(in nombDis varchar(30), in dirDis varchar (200), in nitDis varchar(15), in telDis varchar(15), in we varchar(50))
	Begin
		insert into Distribuidores(nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web) values
			(nombDis, dirDis, nitDis, telDis, we);
	End$$
Delimiter ;

-- call sp_AgregarClientes('Javier','Aguilar', '2481-1282','Ciudad', 'CF');

-- Listar
Delimiter $$
create procedure sp_ListarDistribuidor()
	begin
		select
			Distribuidores.distribuidorId,
			Distribuidores.nombreDistribuidor,
			Distribuidores.direccionDistribuidor,
            Distribuidores.nitDistribuidor,
            Distribuidores.telefonoDistribuidor, 
            Distribuidores.web
				from Distribuidores;
	End $$
Delimiter ;

-- Eliminar
Delimiter $$
create procedure sp_EliminarDistribuidores(in disId int)
	begin 
		delete from Distribuidores
			where distribuidorId = disId;
	End $$
Delimiter ;

-- call sp_EliminarCategoriaProducto(4);

-- Buscar
Delimiter $$
create procedure sp_BuscarDistribuidores(in disId int)
	begin
		select
			Distribuidores.distribuidorId,
			Distribuidores.nombreDistribuidor,
            Distribuidores.direccionDistribuidor,
            Distribuidores.nitDistribuidor,
            Distribuidores.telefonoDistribuidor,
            Distribuidores.web
				from Distribuidores
					where distribuidorId = disId;
	End $$
delimiter ;
     
-- call sp_BuscarCategoriaProducto(1);     
     
-- Editar
Delimiter $$
Create procedure sp_EditarDistribuidores(in nomDis varchar(30), in dirDis varchar (200), in nitDis varchar(15), in telDis varchar(15), in we varchar(50))
	Begin
		Update Distribuidores
			set
                nombreDistribuidor = nomDis,
                nitDistribuidor = nitDis,
                telefonoDistribuidor = telDis,
                web = we
					where distribuidorId = disId;
	End $$
Delimiter ;



-- Compras

-- Agregar
Delimiter $$
create procedure sp_AgregarCompra(in fecComp date, in totComp decimal(10,2))
	Begin
		insert into Compras(fechaCompra, totalCompra) values
			(fecComp, totComp);
	End$$
Delimiter ;

-- call sp_AgregarClientes('Javier','Aguilar', '2481-1282','Ciudad', 'CF');

-- Listar
Delimiter $$
create procedure sp_ListarCompra()
	begin
		select
			Compras.compraId,
			Compras.fechaCompra,
            Compras.totalCompra
				from Compras;
	End $$
Delimiter ;

-- Eliminar
Delimiter $$
create procedure sp_EliminarCompra(in compId int)
	begin 
		delete from Compras
			where compraId = compId;
	End $$
Delimiter ;

-- call sp_EliminarCategoriaProducto(4);

-- Buscar
Delimiter $$
create procedure sp_BuscarCompras(in compId int)
	begin
		select
			Compras.compraId,
            Compras.fechaCompra,
            Compras.totalCompra
				from Compras
					where compraId = compId;
	End $$
delimiter ;
     
-- call sp_BuscarCategoriaProducto(1);     
     
-- Editar
Delimiter $$
Create procedure sp_EditarCompras(in disId int, in nomDis varchar(30), in dirDis varchar (200), in nitDis varchar(15), in telDis varchar(15), in we varchar(50))
	Begin
		Update Distribuidores
			set
				distribuidorId = disId,
                nombreDistribuidor = nomDis,
                nitDistribuidor = nitDis,
                telefonoDistribuidor = telDis,
                web = we
					where distribuidorId = disId;
	End $$
Delimiter ;