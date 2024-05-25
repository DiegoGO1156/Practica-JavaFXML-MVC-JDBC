use superDB;


-- ================== Asignar Encargado ================== --

Delimiter $$
Create function FN_AsignarEncargado(encId int) returns boolean
deterministic
Begin
	declare i int default 2;
    declare a int;
    select count(*) into a from Empleados;
		while i <= a do
			update Empleados set encargadoId = encId where empleadoId = i;
			set i = i +1;
		end while;
        return true;
End$$
Delimiter ;

-- ================== Total Factura ================== --

Delimiter $$
Create function FN_totalFactura (factId int) returns decimal(10,2) deterministic
Begin
	declare i int default 1;
	declare a decimal(10,2);
	declare total decimal(10,2) default 0.00;
    declare totalIVA decimal (10,2) default 0.00;
    declare totalFa decimal (10,2) default 0.00;
    
    totalLoop : loop
    
    if factId = (select DetalleFactura.facturaId from DetalleFactura where detalleFacturaId = i) then
		set a = ( select Productos.precioVentaUnitario from Productos where productoId = (select productoId from DetalleFactura where detalleFacturaId = i));
		set total = a + total;
        set totalIVA = total * 0.12;
        set totalFa = total + totalIVA ;
    end if;
    
    if i = (select count(*) from DetalleFactura) then
			leave totalLoop;
		end if;
        
		set i = i + 1;
        
    end loop totalLoop;
    
     call sp_asignarTotalFactura(factId,totalFa);
     
	return totalIVA; 
End $$
Delimiter ;

-- ================== Descuento Masivo ================== --

delimiter $$
create function FN_descuentoMasivo(descuento double) returns boolean
deterministic
begin
	declare i int default 1;
    declare precionu double;
    declare dia int;
    declare a int;
    select count(*) into a from Productos;
    set dia = dayofmonth(CURDATE());
    if dia = 22 then
		while i <= a do  
			select precio * (1 - descuento) into precionu from (select * from Productos) as temp where  productoId = i;
			update Productos set precio = precionu where productoId = i;
			set i = i + 1;
		end while;
    end if;
  return true;
end $$
delimiter ;

-- ================== Cambio Status ================== --

delimiter $$
create function FN_cambioStatus(estatus VARCHAR(30)) returns boolean
deterministic
begin
	declare i int default 1;
    declare nuevoEstatus varchar(30) DEFAULT 'En proceso';
    declare dia int;
    declare a int;
	select count(*) into a from ticketSporte;
		while i < a do
			update ticketSporte set estatus = nuevoEstatus where ticketSoporteId = i;
			set i = i + 1;
		end while;
  return true;
end $$
delimiter ;

-- ================== Descuento Stock ================== --

delimiter $$
create function FN_desStok(proId int) returns int deterministic
begin
	declare i int default 1;
	declare a decimal(10,2);
	declare Stok int default 0;
    declare total int default (select count(*) from detalleFactura);
    
    totalLoop : loop
    
    if 	proId = (select DF.productoId from detalleFactura DF where detallefacturaId = i) then
		set a = ( select P.cantidadStock from Productos P where productoId = (select productoId from detalleFactura where detallefacturaId = i));
		set Stok =  a - 1;
    end if;
    
    if i = total then
			leave totalLoop;
		end if;
        
		set i = i + 1;
        
    end loop totalLoop;
    
     call sp_asignarDesStok(proId,Stok);
     
	return Stok; 
end $$
delimiter ;

-- ================== Total Stock ================== --

Delimiter $$
Create trigger tg_totalStok
After insert on detalleFactura
For each row
	Begin
		Declare totalStok int;
     
		Set totalStok = FN_desStok(new.productoId);
	End$$
Delimiter ;