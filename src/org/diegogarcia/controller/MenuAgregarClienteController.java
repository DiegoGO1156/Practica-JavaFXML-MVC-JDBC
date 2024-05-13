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
import org.diegogarcia.dto.ClienteDTO;
import org.diegogarcia.model.Cliente;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class MenuAgregarClienteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL Location, ResourceBundle resources) {
        if(ClienteDTO.getClienteDTO().getCliente() != null){
        cargarDatos(ClienteDTO.getClienteDTO().getCliente());
        }
    }    
    
     public void cargarDatos(Cliente cliente){
        tfID.setText(Integer.toString(cliente.getClienteId()));
        tfNombre.setText(cliente.getNombre());
        tfApellido.setText(cliente.getApellido());  
        tfTelefono.setText(cliente.getTelefono());        
        tfDireccion.setText(cliente.getDireccion());      
        tfNit.setText(cliente.getNit());
    }
    
    private Main stage;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    private int op;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
    
    
    @FXML
    Button button_Aceptar, button_Cancelar;
    
    @FXML
    TextField tfNombre, tfApellido, tfTelefono, tfDireccion, tfNit,tfID;
    
    public void agregarCliente(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_AgregarClientes(?,?,?,?,?);";
            statement = conexion.prepareStatement(sql);
            
            statement.setString(1, tfNombre.getText());
            statement.setString(2, tfApellido.getText());
            statement.setString(3, tfTelefono.getText());
            statement.setString(4, tfDireccion.getText());
            statement.setString(5, tfNit.getText());
            statement.execute();
            
        }catch (SQLException e){
            System.out.println(e.getMessage());              
        }finally{
            try{
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
    }
    
    public void editarCliente(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarClientes(?,?,?,?,?,?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfID.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, tfApellido.getText());
            statement.setString(4, tfTelefono.getText());
            statement.setString(5, tfDireccion.getText());
            statement.setString(6, tfNit.getText());      
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
    

    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Aceptar){
            if(op == 1){
                
                if(!tfNombre.getText().equals("") && !tfApellido.getText().equals("") && !tfDireccion.getText().equals("")){
                    agregarCliente();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuClientesView();
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tfNombre.getText().equals("")){
                        tfNombre.requestFocus();
                    } else if(tfApellido.getText().equals("")) {
                        tfApellido.requestFocus();
                    } else if(tfDireccion.getText().equals("")){
                        tfDireccion.requestFocus();
                    }
                }
            }else if(op == 2){
                
               if(!tfNombre.getText().equals("") && !tfApellido.getText().equals("") && !tfDireccion.getText().equals("")){  
                   
                   if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                       editarCliente();
                       SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                       ClienteDTO.getClienteDTO().setCliente(null);
                       stage.menuClientesView();
                   }else{
                       stage.menuClientesView();
                   }
                }else{
                   SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tfNombre.getText().equals("")){
                        tfNombre.requestFocus();
                    } else if(tfApellido.getText().equals("")) {
                        tfApellido.requestFocus();
                    } else if(tfDireccion.getText().equals("")){
                        tfDireccion.requestFocus();
                    }
               }
                
            }
        } else if(event.getSource()== button_Cancelar){
            stage.menuClientesView();
            ClienteDTO.getClienteDTO().setCliente(null);
        }        
    }

    public void setOp(int op) {
        this.op = op;
    }  
    
}
