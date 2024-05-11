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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.DetalleComDTO;
import org.diegogarcia.model.CompraDetalle;
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
    TextField tf_CantComp, tf_ProdId, tf_CompId,tf_DetICompd; 
    
    
    
    
    
    
     public void setOp(int op) {
        this.op = op;
    }
    
    public void cargarDatos(CompraDetalle compraDetalle){
        tf_CantComp.setText(Integer.toString(compraDetalle.getCantidadCompra()));
        tf_ProdId.setText(Integer.toString(compraDetalle.getProductoId()));
        tf_CompId.setText(Integer.toString(compraDetalle.getCompraId()));
        tf_DetICompd.setText(Integer.toString(compraDetalle.getDetalleCompraId()));
        
    }
    
    
    public void agregarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDetalleCompra(?, ?, ?);";
            statement = conexion.prepareStatement(sql);
            
            statement.setInt(1, Integer.parseInt(tf_CantComp.getText()));
            statement.setInt(2, Integer.parseInt(tf_ProdId.getText()));
            statement.setInt(3, Integer.parseInt(tf_CompId.getText()));
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
    
    public void editarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargo(?, ?, ? ,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_DetICompd.getText()));
            statement.setInt(2, Integer.parseInt(tf_CantComp.getText()));
            statement.setInt(3, Integer.parseInt(tf_ProdId.getText()));
            statement.setInt(4, Integer.parseInt(tf_CompId.getText()));
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
    

    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_cancel){
            stage.menuDetalleCompras();
        }else if(event.getSource() == button_acept){
            if(op == 1){
                
                if(!tf_CantComp.getText().equals("")){
                    agregarCargo();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuCargosView();
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
                        stage.menuCargosView();
                    }else{
                        stage.menuCargosView();
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
