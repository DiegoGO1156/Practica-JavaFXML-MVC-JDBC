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
import java.text.SimpleDateFormat;
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
import org.diegogarcia.model.Cargos;
import org.diegogarcia.model.Empleados;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FormEmpleadosController implements Initializable {

    /**
     * Initializes the controller class.
     */
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
    
    public void setOp(int op) {
        this.op = op;
    }
    
   @FXML
    Button Button_Cancel,Button_Acept;
   
   @FXML
   TextField tf_EmpId,tf_Nombre,tf_Apellido,tf_Sueldo,tf_HoraEntrada,tf_HoraSalida;
   
   @FXML
   ComboBox cmb_CargId,cmbEncargados;
   
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(EmpleadosDto.getEmpleadosDto().getEmpleados() != null){
            cargarDatos(EmpleadosDto.getEmpleadosDto().getEmpleados());
        }
        cmb_CargId.setItems(listarCargos());
        
    }

    public void cargarDatos(Empleados empleado){
        tf_EmpId.setText(Integer.toString(empleado.getEmpleadoId()));
        tf_Nombre.setText(empleado.getNombreEmpleado());
        tf_Apellido.setText(empleado.getApellidoEmpleado());
        tf_Sueldo.setText(Double.toString(empleado.getSueldo()));
        tf_HoraEntrada.setText(empleado.getHoraEntrada().toString());
        tf_HoraSalida.setText(empleado.getHoraSalida().toString());
    }
    
    public void agregarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarEmpleados(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tf_Nombre.getText());
            statement.setString(2, tf_Apellido.getText());
            statement.setString(3, tf_Sueldo.getText());
            statement.setString(4, tf_HoraEntrada.getText());
            statement.setString(5, tf_HoraSalida.getText());
            statement.setInt(6,((Cargos)(cmb_CargId.getSelectionModel().getSelectedItem())).getCargoId());
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
    
    public void editarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarEmpleados(?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_EmpId.getText()));
            statement.setString(2, tf_Nombre.getText());
            statement.setString(3, tf_Apellido.getText());
            statement.setString(4, tf_Sueldo.getText());
            statement.setString(5, tf_HoraEntrada.getText());
            statement.setString(6, tf_HoraSalida.getText());
            statement.setInt(7,((Cargos)cmb_CargId.getSelectionModel().getSelectedItem()).getCargoId());
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
    
    public ObservableList<Cargos> listarCargos(){
        ArrayList<Cargos> cargos = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCargo();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombreCargo = resultSet.getString("nombreCargo");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                
                cargos.add(new Cargos(cargoId, nombreCargo, descripcionCargo));
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
        
        return FXCollections.observableList(cargos);
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
                String encargadoId = resultSet.getString("nombreEncargado");
            
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
    
   
    public void handleButtonAction(ActionEvent event) {
    
        if(event.getSource() == Button_Cancel){
            EmpleadosDto.getEmpleadosDto().setEmpleados(null);
            stage.menuEmpleadosView();
        }else if(event.getSource() == Button_Acept){
            if(op == 1){
                if(!tf_Nombre.getText().equals("") && !tf_Apellido.getText().equals("") && !tf_Sueldo.getText().equals("") && !tf_HoraEntrada.getText().equals("") && !tf_HoraSalida.getText().equals("")){
                    agregarEmpleado();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuEmpleadosView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tf_Nombre.getText().equals("")){
                        tf_Nombre.requestFocus();
                    }else if(tf_Apellido.getText().equals("")){
                        tf_Apellido.requestFocus();
                    }else if(tf_Sueldo.getText().equals("")){
                        tf_Sueldo.requestFocus();
                    }else if(tf_HoraEntrada.getText().equals("")){
                        tf_HoraEntrada.requestFocus();
                    }else if(tf_HoraSalida.getText().equals("")){
                        tf_HoraSalida.requestFocus();
                    }
                }
                
               
            }else if(op == 2){
                if(!tf_Nombre.getText().equals("") && !tf_Apellido.getText().equals("") && !tf_Sueldo.getText().equals("") && !tf_HoraEntrada.getText().equals("") && !tf_HoraSalida.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                        editarEmpleado();
                        EmpleadosDto.getEmpleadosDto().setEmpleados(null);
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                        stage.menuEmpleadosView();
                    }else{
                        stage.menuEmpleadosView();
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tf_Nombre.getText().equals("")){
                        tf_Nombre.requestFocus();
                    }else if(tf_Apellido.getText().equals("")){
                        tf_Apellido.requestFocus();
                    }else if(tf_Sueldo.getText().equals("")){
                        tf_Sueldo.requestFocus();
                    }else if(tf_HoraEntrada.getText().equals("")){
                        tf_HoraEntrada.requestFocus();
                    }else if(tf_HoraSalida.getText().equals("")){
                        tf_HoraSalida.requestFocus();
                    }
                }
                
            }
        }
    }
   
   
}
