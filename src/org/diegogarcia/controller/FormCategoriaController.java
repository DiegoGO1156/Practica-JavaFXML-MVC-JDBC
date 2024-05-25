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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.CategorasDTO;
import org.diegogarcia.model.CategoriaProducto;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FormCategoriaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       if(CategorasDTO.getCategorasDTO().getCategoriaProducto() != null){
          cargarDatos(CategorasDTO.getCategorasDTO().getCategoriaProducto()); 
       }
    }
    
    private static Connection conexion;
    private static ResultSet resulSet;
    private static PreparedStatement statement;

    
    @FXML
    Button button_Aceptar, button_Cancelar;
    
    @FXML
    TextField tf_CategoriaID, tf_NombreCategoria;
    
    @FXML
    TextArea ta_Descripcion;
            
            
    private Main stage;
    
    private static int op;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

    public int getop() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
    public void agregarCategoria(){
        try{
            
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCategoriaProducto(?,?)";
            statement = conexion.prepareStatement(sql);
            
            statement.setString(1, tf_NombreCategoria.getText());
            statement.setString(2, ta_Descripcion.getText());
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
    
    public void editarCategoria(){
        try{
            
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCategoriaProductos(?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            
            statement.setInt(1, Integer.parseInt(tf_CategoriaID.getText()));
            statement.setString(2, tf_NombreCategoria.getText());
            statement.setString(3, ta_Descripcion.getText());
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
    
    public void cargarDatos(CategoriaProducto categoria){
        tf_CategoriaID.setText(Integer.toString(categoria.getCategoriaProductosId()));
        tf_NombreCategoria.setText(categoria.getNombreCategoria());
        ta_Descripcion.setText(categoria.getDescripcionCategoria());
    }
    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Aceptar){
            if(op == 1){
                if(!tf_NombreCategoria.getText().equals("") && !ta_Descripcion.getText().equals("")){
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    agregarCategoria();
                    stage.menuCategoriasView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tf_NombreCategoria.getText().equals("")){
                        tf_NombreCategoria.requestFocus();
                    }else if(ta_Descripcion.getText().equals("")){
                        ta_Descripcion.requestFocus();
                    }
                }
            }else if(op == 2){
                if(!tf_NombreCategoria.getText().equals("") && !ta_Descripcion.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                        editarCategoria();
                        CategorasDTO.getCategorasDTO().setCategoriaProducto(null);
                        stage.menuCategoriasView();
                    }
                }else{
                    stage.menuCategoriasView();
                }
            }
            
        }else if(event.getSource() == button_Cancelar){
            stage.menuCategoriasView();
        }
    }
    
    
}
