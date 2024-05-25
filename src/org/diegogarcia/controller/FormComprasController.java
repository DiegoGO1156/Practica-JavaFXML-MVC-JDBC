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
import org.diegogarcia.dto.ComprasDTO;
import org.diegogarcia.dto.DetalleComDTO;
import org.diegogarcia.model.Compras;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FormComprasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(ComprasDTO.getComprasDTO().getCompras() != null){
            cargarDatos(ComprasDTO.getComprasDTO().getCompras());
        }
    }   
    
    public void cargarDatos(Compras compra){
        tf_CompFech.setText((compra.getFechaCompra()));
        tf_TotalComp.setText(Double.toString(compra.getTotalCompra()));
        
    }
    
    private Main stage;
    private int op;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;

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
    TextField tf_CompFech, tf_TotalComp, tf_compId;
    @FXML
    Button button_Acept, button_Cancel;
    
    public void agregarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCompra(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tf_CompFech.getText());
            statement.setDouble(2, Double.parseDouble(tf_TotalComp.getText()));
            statement.execute();
            

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
                e.printStackTrace();
            }
        }
    }
    
    public void editarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCompra(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tf_compId.getText());
            statement.setString(2, tf_CompFech.getText());
            statement.setDouble(3, Double.parseDouble(tf_TotalComp.getText()));
            statement.execute();
            

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
                e.printStackTrace();
            }
        }
    }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Cancel){
            stage.menuDetalleCompras();
        }else if(event.getSource() == button_Acept){
            if(op == 1){
                
                if(!tf_CompFech.getText().equals("") && !tf_TotalComp.getText().equals("")){
                    agregarCompra();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuDetalleCompras();
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                        if(tf_CompFech.getText().equals("")) {
                            tf_CompFech.requestFocus();
                        }else if(tf_TotalComp.getText().equals("")) {
                            tf_TotalComp.requestFocus();
                        }
                    }
                }else if(op == 2){
                if(!tf_CompFech.getText().equals("") && !tf_TotalComp.getText().equals("")){  
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                        editarCompra();
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                        DetalleComDTO.getDetalleComDTO().setCompraDetalle(null);
                        stage.menuDetalleCompras();
                    }else{
                        stage.menuDetalleCompras();
                    }
                 }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                     if(tf_CompFech.getText().equals("")) {
                            tf_CompFech.requestFocus();
                        }else if(tf_TotalComp.getText().equals("")) {
                            tf_TotalComp.requestFocus();
                        }
                }
            }
        }
    }  
}
