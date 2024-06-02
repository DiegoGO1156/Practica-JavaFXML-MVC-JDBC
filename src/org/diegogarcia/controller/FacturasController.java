/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.model.DetalleFactura;
import org.diegogarcia.model.Factura;
import org.diegogarcia.system.Main;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FacturasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }   
    
    private static Connection conexion;
    private static ResultSet resultSet;
    private static PreparedStatement statement;
    
    private Main stage;
    private int op;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
    @FXML
    TableColumn col_IDFactura, col_Fecha, col_Hora, col_Cliente, col_Empleado, col_Total;
    
    @FXML
    TableView tbl_Factura;
    
    public void cargarDatos(){
        if(op == 3){ 
           // tbl_Factura.getItems().add(buscarCompraDetalle());
            op = 0;
        }else {
            tbl_Factura.setItems(listarFactura());
        }
        col_IDFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer> ("facturaId"));
        col_Fecha.setCellValueFactory(new PropertyValueFactory<Factura, String> ("fecha"));
        col_Hora.setCellValueFactory(new PropertyValueFactory<Factura, Time> ("hora"));
        col_Cliente.setCellValueFactory(new PropertyValueFactory<Factura, String> ("Cliente"));
        col_Empleado.setCellValueFactory(new PropertyValueFactory<Factura, String> ("Empleado"));
        col_Total.setCellValueFactory(new PropertyValueFactory<Factura, Double> ("total"));
    }
    
    
    public ObservableList <Factura> listarFactura(){
        ArrayList <Factura> factura = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarFactura()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int facturaId = resultSet.getInt("facturaId");
                String fecha = resultSet.getString("fecha");
                Time hora = resultSet.getTime("hora");
                String cliente = resultSet.getString("Cliente");
                String empleado = resultSet.getString("Empleado");
                Double total = resultSet.getDouble("total");
                
                factura.add(new Factura(facturaId, fecha, hora, cliente, empleado, total));
            }
            
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return FXCollections.observableList(factura);
    }
    
    public ObservableList <DetalleFactura> listarDetalleFactura(){
        ArrayList <DetalleFactura> detalle = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarDetFacturas()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int detalleFacturaId = resultSet.getInt("detalleFacturaId");
                String factura = resultSet.getString("Factura");
                String producto = resultSet.getString("Producto");
                
                detalle.add(new DetalleFactura(detalleFacturaId, factura, producto));
            }
            
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return FXCollections.observableList(detalle);
    }
    
}
