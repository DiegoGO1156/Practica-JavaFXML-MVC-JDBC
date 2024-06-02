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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.model.Usuario;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.PasswordUtils;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    private static Main stage;
    private int op = 0;

    public static Main getStage() {
        return stage;
    }

    public static void setStage(Main stage) {
        LoginController.stage = stage;
    }
    
    
    
    @FXML
    Button button_StartSesion, button_Registrar; 
    
    @FXML
    TextField tf_Usuario;
    
    @FXML
    PasswordField tf_Password;
    
    public Usuario buscarUsuario(){
        Usuario usuario = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_BuscarUsuario(?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1,tf_Usuario.getText());
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int usuarioId = resultSet.getInt("usuarioId");
                String usuarios = resultSet.getString("usuario");
                String password = resultSet.getString("contrasenia");
                int nivelesAccesoId = resultSet.getInt("nivelesAccesoId");
                int empleadoId = resultSet.getInt("empleadoId");
                
                usuario = new Usuario(usuarioId, usuarios, password, nivelesAccesoId, empleadoId);
                
            }
            
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
                    if(resultSet != null){
                    resultSet.close();
                    }
                 }catch(Exception e){{
                    e.printStackTrace();
                }
            }
        }
        return usuario;
    }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_StartSesion){
            if(op == 0){
                Usuario usuario = buscarUsuario();    
                if(usuario != null){
                    if(PasswordUtils.getInstance().checkPassword(tf_Password.getText(), usuario.getPassword())){
                        SuperKinalAlert.getInstance().alertBienvenida(usuario.getUsuario());
                        if(usuario.getNivelesAccesoId() == 1){ // Admin
                           button_Registrar.setDisable(false);
                           button_StartSesion.setText("Ir al Menú");
                           op = 1;
                        }else if(usuario.getNivelesAccesoId() == 2){ // Empledo
                            stage.menuPrincipalView();
                        }
                    }else{
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(80);
                    }
                }else{
                  SuperKinalAlert.getInstance().mostrarAlertaInfo(90);
                }
            }else{
                stage.menuPrincipalView();
            }
        }else if(event.getSource() == button_Registrar){
            stage.formUsuariosView();
        }
    }
    
    
}
