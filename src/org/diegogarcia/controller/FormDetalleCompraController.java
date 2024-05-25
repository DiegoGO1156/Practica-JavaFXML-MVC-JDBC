/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.controller;

import java.net.URL;
import java.sql.Blob;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.DetalleComDTO;
import org.diegogarcia.model.CompraDetalle;
import org.diegogarcia.model.Compras;
import org.diegogarcia.model.Producto;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FormDetalleCompraController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        if(DetalleComDTO.getDetalleComDTO().getCompraDetalle() != null){
            cargarDatos(DetalleComDTO.getDetalleComDTO().getCompraDetalle());
        }
        cmb_Prod.setItems(listarProductos());
        cmb_CompId.setItems(listarCompra());
    }       
    
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
    Button button_acept, button_cancel;
    
    @FXML
    TextField tf_CantComp,tf_DetICompd; 
    
    @FXML
    ComboBox cmb_Prod, cmb_CompId;
    
     public void setOp(int op) {
        this.op = op;
    }
    
    public void cargarDatos(CompraDetalle compraDetalle){
        tf_CantComp.setText(Integer.toString(compraDetalle.getCantidadCompra()));
        tf_DetICompd.setText(Integer.toString(compraDetalle.getDetalleCompraId()));
        
    }
    
    public int obtenerIndexProducto() {
        int index = -1;
        int productoId = Integer.parseInt(tf_DetICompd.getText()); 
        
        for (int i = 0; i < cmb_Prod.getItems().size(); i++) {
            Producto productos = (Producto) cmb_Prod.getItems().get(i);
            if (productos.getProductoId() == productoId) {
                index = i;
                break;
            }
        }

        return index;
    }
    
    
    public void agregarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDetalleCompra(?, ?, ?);";
            statement = conexion.prepareStatement(sql);
            
            statement.setInt(1, Integer.parseInt(tf_CantComp.getText()));
            statement.setInt(2, ((Producto)(cmb_Prod.getSelectionModel().getSelectedItem())).getProductoId());
            statement.setInt(3, ((Compras)(cmb_CompId.getSelectionModel().getSelectedItem())).getCompraId());
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
            }catch(Exception e){
                e.printStackTrace();
            }
        }
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
    
    public ObservableList<Producto> listarProductos(){
        ArrayList<Producto> producto = new ArrayList<>();
            try{
                conexion = Conexion.getInstance().obtenerConexion();
                String sql = "call sp_listarProducto()";
                statement = conexion.prepareStatement(sql);
                resultSet = statement.executeQuery();
                
                while(resultSet.next()){
                    int productoId = resultSet.getInt("productoId");
                    String nombreProducto = resultSet.getString("nombreProducto");
                    String descripcionProducto = resultSet.getString("descripcionProducto");
                    int cantidadStock = resultSet.getInt("cantidadStock");
                    Double precioVentaUnitario = resultSet.getDouble("precioVentaUnitario");
                    Double precioVentaMayor = resultSet.getDouble("precioVentaMayor");
                    Double precioCompra = resultSet.getDouble("precioCompra");
                    Blob imagenProducto = resultSet.getBlob("imagenProducto");
                    String distribuidorId = resultSet.getString("Distribuidores");
                    String categoriaId = resultSet.getString("Categorias");
                    
                    producto.add(new Producto(productoId, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaId));
                }
                
            }catch(Exception e){
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
        return FXCollections.observableList(producto);
    }
    
    
    public void editarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargo(?, ?, ? ,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_DetICompd.getText()));
            statement.setInt(2, Integer.parseInt(tf_CantComp.getText()));
            statement.setInt(3, ((Producto)(cmb_Prod.getSelectionModel().getSelectedItem())).getProductoId());
            statement.setInt(4, ((Compras)(cmb_CompId.getSelectionModel().getSelectedItem())).getCompraId());
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
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }    

    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_cancel){
            stage.menuDetalleCompras();
        }else if(event.getSource() == button_acept){
            if(op == 1){
                
                if(!tf_CantComp.getText().equals("")){
                    agregarCargo();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuDetalleCompras();
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                        if(tf_CantComp.getText().equals("")) {
                            tf_CantComp.requestFocus();
                        }
                    }
                }else if(op == 2){
                if(!tf_CantComp.getText().equals("")){  
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                        editarCargo();
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                        DetalleComDTO.getDetalleComDTO().setCompraDetalle(null);
                        stage.menuDetalleCompras();
                    }else{
                        stage.menuDetalleCompras();
                    }
                 }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                     if(tf_CantComp.getText().equals("")) {
                        tf_CantComp.requestFocus();
                     }
                }
            }
        }
    }
    
}
