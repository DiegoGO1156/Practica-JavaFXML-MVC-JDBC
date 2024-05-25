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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.ComprasDTO;
import org.diegogarcia.dto.DetalleComDTO;
import org.diegogarcia.model.CompraDetalle;
import org.diegogarcia.model.Compras;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class DetalleCompraController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    
    private Main stage;
    private int op;
    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    @FXML
    Button button_Ag, button_Edit, button_Elimn, button_Busc, button_reg, button_Ag1, button_Edit1, button_Elimn1, button_Busc1;
    
    @FXML
    TextField tf_Buscar, tf_Buscar1;
    
    @FXML
    TableView tbl_DetalleComp, tbl_Compras;
    
    @FXML
    TableColumn  col_DetalleCompId,col_CantComp, col_Prod, col_CompId, col_IdCompra, col_FechCompra, col_TotalCompra;
    
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
        cargarDatos2();
    }   
    
    public void cargarDatos(){
        if(op == 3){ 
            tbl_DetalleComp.getItems().add(buscarCompraDetalle());
            op = 0;
        }else {
            tbl_DetalleComp.setItems(listarCompraDetalle());
        }
        col_DetalleCompId.setCellValueFactory(new PropertyValueFactory<CompraDetalle, Integer> ("detalleCompraId"));
        col_CantComp.setCellValueFactory(new PropertyValueFactory<CompraDetalle, Integer> ("cantidadCompra"));
        col_Prod.setCellValueFactory(new PropertyValueFactory<CompraDetalle, String> ("Producto"));
        col_CompId.setCellValueFactory(new PropertyValueFactory<CompraDetalle, String> ("Compra"));
    }
    
    public void cargarDatos2(){
        if(op == 3){ 
            tbl_Compras.getItems().add(buscarCompraDetalle());
            op = 0;
        }else {
            tbl_Compras.setItems(listarCompra());
        }
        col_IdCompra.setCellValueFactory(new PropertyValueFactory<Compras, Integer> ("compraId"));
        col_FechCompra.setCellValueFactory(new PropertyValueFactory<Compras, String> ("fechaCompra"));
        col_TotalCompra.setCellValueFactory(new PropertyValueFactory<Compras, Double> ("totalCompra"));
    }
    
    public ObservableList<CompraDetalle> listarCompraDetalle(){
        ArrayList<CompraDetalle> CompraDetalle = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDetalleCompra();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int detalleCompraId = resultSet.getInt("detalleCompraId");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("Producto");
                String compraId = resultSet.getString("Compra");
                
                CompraDetalle.add(new CompraDetalle(detalleCompraId, cantidadCompra, producto,  compraId));
            }
            
        }catch (SQLException e){
            System.out.println(e.getMessage());
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
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        return FXCollections.observableList(CompraDetalle);
    } 
    
    public ObservableList<Compras> listarCompra(){
        ArrayList <Compras> compra = new ArrayList<>();
            try{
                conexion = Conexion.getInstance().obtenerConexion();
                String sql = "call sp_listarCompra()";
                statement = conexion.prepareStatement(sql);
                resultSet = statement.executeQuery();
                
                while(resultSet.next()){
                    int compraId = resultSet.getInt("compraId");
                    String fechaCompra = resultSet.getString("fechaCompra");
                    Double totalCompra = resultSet.getDouble("totalCompra");
                    
                    compra.add(new Compras(compraId, fechaCompra, totalCompra));
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
                }catch(Exception e){
                    System.out.println(e.getMessage());
            }
        }
        return FXCollections.observableList(compra);
    }
    
    
    public CompraDetalle buscarCompraDetalle(){
        CompraDetalle CompraDetalle = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDetalleCompra(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_Buscar.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int detalleCompraId = resultSet.getInt("detalleCompraId");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("Producto");
                String compraId = resultSet.getString("Compra");
                
                
                CompraDetalle = new CompraDetalle(detalleCompraId, cantidadCompra, producto,  compraId);
            }
            
        }catch(SQLException e ){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (Exception e){
               System.out.println(e.getMessage()); 
            }
        }
        return CompraDetalle;
    }
    
    public void eliminarCompraDetalle(int cargId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarDetalleCompra(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, cargId);
            statement.execute();     
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void eliminarCompra(int compId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql =  "call sp_eliminarCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, compId);
            statement.execute();
            
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
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }   
    
    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_reg){
            stage.menuPrincipalView();
        } else if(event.getSource()== button_Ag){
            stage.formDetalleCompra(1);
        } else if(event.getSource()== button_Edit){
            DetalleComDTO.getDetalleComDTO().setCompraDetalle((CompraDetalle)tbl_DetalleComp.getSelectionModel().getSelectedItem());
            stage.formDetalleCompra(2);
            cargarDatos();
        }else if(event.getSource()== button_Elimn){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarCompraDetalle(((CompraDetalle)tbl_DetalleComp.getSelectionModel().getSelectedItem()).getDetalleCompraId());
                cargarDatos();
            }
        }else if(event.getSource() == button_Busc){
            tbl_DetalleComp.getItems().clear();
            if(tf_Buscar.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }else if(event.getSource()== button_Ag1){
            stage.formCompra(1);
        }else if(event.getSource()== button_Edit1){
            ComprasDTO.getComprasDTO().setCompras(((Compras)tbl_Compras.getSelectionModel().getSelectedItem()));
            stage.formCompra(2);
            cargarDatos();
        }else if(event.getSource()== button_Elimn1){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarCompraDetalle(((Compras)tbl_Compras.getSelectionModel().getSelectedItem()).getCompraId());
                cargarDatos();
            }
        }else if(event.getSource() == button_Busc1){
            tbl_Compras.getItems().clear();
            if(tf_Buscar1.getText().equals("")){
                //cargarDatos();
            }else {
                op = 3;
                //cargarDatos();
            }
        }
        
    }
}
