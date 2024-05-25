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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.model.Empleados;
import org.diegogarcia.system.Main;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class EmpleadosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    } 
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    private static int op;
    private Main stage;
    
    @FXML
    Button button_Agregar, button_Editar, button_Eliminar, button_Regresa, button_Lupa;
    
    @FXML
    TextField tf_IDEmpleado;
    
    @FXML
    TableView tbl_Empleados;
    
    @FXML
    TableColumn col_ID, col_Nombre, col_Apellido, col_Sueldo, col_HoraEntrada, col_HoraSalida, col_Cargo, col_Encargado;
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    public void cargarDatos(){
        if(op == 3){
            tbl_Empleados.getItems().add(buscarEmpleado());
            op = 0;
            
        }else{
            tbl_Empleados.setItems(listarEmpleados()); 

            col_ID.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("empleadoId"));
            col_Nombre.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
            col_Apellido.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
            col_Sueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
            col_HoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleados, Time>("horaEntrada"));
            col_HoraSalida.setCellValueFactory(new PropertyValueFactory<Empleados, Time>("horaSalida"));
            col_Cargo.setCellValueFactory(new PropertyValueFactory<Empleados, String>("cargo"));
            col_Encargado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("Encargado"));
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
    
    public void eliminarEmpleado(int empId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarEmpleados(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,empId);
            statement.execute();
            
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
    }
    
    public Empleados buscarEmpleado(){
        Empleados empleado = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarEmpleados(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(tf_IDEmpleado.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                double sueldo = resultSet.getDouble("sueldo");
                Time horaentrada = resultSet.getTime("horaentrada");
                Time horaSalida = resultSet.getTime("horaSalida");
                String cargoId = resultSet.getString("cargo");
                String encargadoId = resultSet.getString("Encargado");
            
                empleado = new Empleados(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaentrada, horaSalida,encargadoId,cargoId);

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
        return empleado;
    }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Agregar){
            stage.formEmpleadosView(1);
        }else if(event.getSource() == button_Editar){
            stage.formEmpleadosView(2);
        }else if(event.getSource() == button_Eliminar){
                eliminarEmpleado(((Empleados)tbl_Empleados.getSelectionModel().getSelectedItem()).getEmpleadoId());
                cargarDatos();
        }else if(event.getSource() == button_Lupa){
            tbl_Empleados.getItems().clear();
            if(tf_IDEmpleado.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }else if(event.getSource() == button_Regresa){
            stage.menuPrincipalView();
        }
    }
}
