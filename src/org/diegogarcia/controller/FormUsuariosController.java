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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.model.Empleados;
import org.diegogarcia.model.NivelAcceso;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.PasswordUtils;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FormUsuariosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmb_Empleado.setItems(listarEmpleados());
        cmb_NivelAcceso.setItems(listarNiveles());
    }    
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    private Main stage;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
    
    @FXML
    TextField tf_Usuario, tf_PassWord;
    
    @FXML
    ComboBox cmb_Empleado, cmb_NivelAcceso;
    
    @FXML
    Button button_Registrar, button_cancel, button_AgEmpledo;
    
    
    public void crearUsuario(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_CrearUsuario(?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            
            statement.setString(1, tf_Usuario.getText());
            statement.setString(2, PasswordUtils.getInstance().encryptedPassword(tf_PassWord.getText()));
            statement.setInt(3, ((NivelAcceso)cmb_NivelAcceso.getSelectionModel().getSelectedItem()).getNivelesAccesoId());
            statement.setInt(4, ((Empleados)cmb_Empleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
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
    
    public ObservableList<Empleados> listarEmpleados(){
        ArrayList<Empleados> empleados = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = " call sp_listarEmpleados()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                double sueldo = resultSet.getDouble("sueldo");
                Time horaentrada = resultSet.getTime("horaentrada");
                Time horaSalida = resultSet.getTime("horaSalida");
                String cargoId = resultSet.getString("cargo");
                String encargadoId = resultSet.getString("Encargado");
            
                empleados.add(new Empleados(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaentrada, horaSalida,cargoId,encargadoId));
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
        
        
        return FXCollections.observableList(empleados);
    }
    
    public ObservableList <NivelAcceso> listarNiveles(){
        ArrayList <NivelAcceso> nivelAcceso = new ArrayList<>();

        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarNivelAcceso()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int nivelesAccesoId = resultSet.getInt("nivelesAccesoId");
                String nivel = resultSet.getString("nivelAcceso");
            
                nivelAcceso.add(new NivelAcceso(nivelesAccesoId, nivel));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return FXCollections.observableList(nivelAcceso);
    }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Registrar){
            crearUsuario();
            stage.loginView();
        }else if(event.getSource() == button_cancel){
            stage.loginView();
        }else if(event.getSource() == button_AgEmpledo){
            stage.formEmpleadosView(1);
        }
    }
    
}
