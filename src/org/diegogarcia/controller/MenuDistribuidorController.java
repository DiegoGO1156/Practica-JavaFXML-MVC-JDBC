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
import org.diegogarcia.dto.DistribuidorDTO;
import org.diegogarcia.model.Cargos;
import org.diegogarcia.model.Distribuidores;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class MenuDistribuidorController implements Initializable {

    /**
     * Initializes the controller class.
     */
   private Main stage;
    private int op;
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    @FXML
    Button button_Reg, button_Ag, button_Edit, button_Elimn, button_Busc;
    @FXML
    TableView tbl_Distri;
    @FXML
    TableColumn col_Id, col_Nom, col_Direccion, col_Nit, col_Telef, col_Web;
    @FXML
    TextField tf_Buscar;
    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tbl_Distri.getItems().add(buscarDistribuidor());
            op = 0;
        }else{
            tbl_Distri.setItems(listarDistribuidor());
        }
        col_Id.setCellValueFactory(new PropertyValueFactory<Distribuidores, Integer>("distribuidorId"));
        col_Nom.setCellValueFactory(new PropertyValueFactory<Distribuidores, String>("nombreDistribuidor"));
        col_Direccion.setCellValueFactory(new PropertyValueFactory<Distribuidores, String>("direccionDistribuidor"));
        col_Nit.setCellValueFactory(new PropertyValueFactory<Distribuidores, String>("nitDistribuidor"));
        col_Telef.setCellValueFactory(new PropertyValueFactory<Distribuidores, String>("telefonoDistribuidor"));
        col_Web.setCellValueFactory(new PropertyValueFactory<Distribuidores, String>("web"));
    }
    
    public ObservableList<Distribuidores> listarDistribuidor(){
        ArrayList<Distribuidores> distribuidor = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDistribuidores();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                distribuidor.add(new Distribuidores(distribuidorId, nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return FXCollections.observableList(distribuidor);
    }    

 
    public Main getStage() {
        return stage;
    }
 
    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    public Distribuidores buscarDistribuidor(){
        Distribuidores distribuidor = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDistribuidores(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_Buscar.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                
                distribuidor = new Distribuidores(distribuidorId, nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web);
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
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
        
        return distribuidor;
    }
    
    public void eliminarDistribuidores(int distriId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarDistribuidores(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, distriId);
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
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Reg){
            stage.menuPrincipalView();
        }else if(event.getSource() == button_Ag){
             stage.formDistribuidorView(1);
        }else if(event.getSource() == button_Edit){
             DistribuidorDTO.getDistribuidorDTO().setDistribuidor((Distribuidores)tbl_Distri.getSelectionModel().getSelectedItem());
             stage.formDistribuidorView(2);
        }else if(event.getSource() == button_Elimn){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarDistribuidores(((Distribuidores)tbl_Distri.getSelectionModel().getSelectedItem()).getDistribuidorId());
                cargarDatos();
            }
            
        }else if(event.getSource() == button_Busc){ 
            tbl_Distri.getItems().clear();
            if(tf_Buscar.getText().equals("")){
                cargarDatos();
            }else{
                op = 3;
                cargarDatos();
            }
        }
           
    }
}
