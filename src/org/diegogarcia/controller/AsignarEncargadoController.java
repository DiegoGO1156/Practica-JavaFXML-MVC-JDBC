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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.EmpleadosDto;
import org.diegogarcia.model.Empleados;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class AsignarEncargadoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmb_Encargado.setItems(listarEmpleados());
        if(EmpleadosDto.getEmpleadosDto().getEmpleados() != null){
            cargarDatos(EmpleadosDto.getEmpleadosDto().getEmpleados());
        }
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
    
    public void cargarDatos(Empleados empleado){
        tf_Empleado.setText(Integer.toString(empleado.getEmpleadoId()));
    }
    
    @FXML
    TextField tf_Empleado;
    
    @FXML
    ComboBox cmb_Encargado;
    
    @FXML
    Button button_cancelar, button_Aceptar;
    
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
    
    public void asignarEncargado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_AsignarEncargado(?, ?)";
            statement = conexion.prepareStatement(sql);
            
            statement.setInt(1, Integer.parseInt(tf_Empleado.getText()));
            statement.setInt(2, ((Empleados)(cmb_Encargado.getSelectionModel().getSelectedItem())).getEmpleadoId());
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
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Aceptar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(900).get() == ButtonType.OK){
                asignarEncargado();
                stage.menuEmpleadosView();
            }else{
                 stage.menuEmpleadosView();
            }
        }else if(event.getSource() == button_cancelar){
            stage.menuEmpleadosView();
        }
    }
    
}
