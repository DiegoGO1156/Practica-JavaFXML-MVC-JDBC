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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.DistribuidorDTO;
import org.diegogarcia.model.Distribuidores;
import org.diegogarcia.system.Main;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FromDistribuidoresController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Main stage;
    private int op;   
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    
    @FXML
    TextField tf_IDistri, tf_Nombre, tf_Direccion, tf_Nit, tf_Telefono, tf_Web;
    @FXML
    Button button_Acpet, button_cancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(DistribuidorDTO.getDistribuidorDTO().getDistribuidor() != null){
            cargarDatos(DistribuidorDTO.getDistribuidorDTO().getDistribuidor());
        }
    }    
    
    public void cargarDatos(Distribuidores distribuidor){
        tf_IDistri.setText(Integer.toString(distribuidor.getDistribuidorId()));
        tf_Nombre.setText(distribuidor.getNombreDistribuidor());
        tf_Direccion.setText(distribuidor.getDireccionDistribuidor());  
        tf_Nit.setText(distribuidor.getNitDistribuidor());        
        tf_Telefono.setText(distribuidor.getTelefonoDistribuidor());      
        tf_Web.setText(distribuidor.getWeb());
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    public void agregarDistribuidores(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDistribuidores(?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tf_Nombre.getText());
            statement.setString(2, tf_Direccion.getText());
            statement.setString(3, tf_Nit.getText());
            statement.setString(4, tf_Telefono.getText());
            statement.setString(5, tf_Web.getText());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{            
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void editarDistribuidores(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarDistribuidores(?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_IDistri.getText()));
            statement.setString(2, tf_Nombre.getText());
            statement.setString(3, tf_Direccion.getText());
            statement.setString(4, tf_Nit.getText());
            statement.setString(5, tf_Telefono.getText());
            statement.setString(6, tf_Web.getText());      
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
            System.out.println(e.getMessage());                
            }
        }
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_cancelar){
            DistribuidorDTO.getDistribuidorDTO().setDistribuidor(null);
            stage.menuDistribuidorView();
        }else if(event.getSource() == button_Acpet){
            if(op == 1){
            agregarDistribuidores(); 
            stage.menuDistribuidorView();
            }else if(op == 2){
                editarDistribuidores();
                DistribuidorDTO.getDistribuidorDTO().setDistribuidor(null);
                stage.menuDistribuidorView();
            }          
        }
    }

    public void setOp(int op) {
        this.op = op;
    }
}
