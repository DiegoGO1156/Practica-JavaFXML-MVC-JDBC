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
import org.diegogarcia.dto.PromocionesDTO;
import org.diegogarcia.model.Promociones;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class FormPromocionesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    TextField tf_ProdId, tf_PromoId, tf_FechInicio, tf_Precio, tf_FechFinal;
    
    @FXML
    TextArea ta_Descripcion;
    
    @FXML
    Button button_Acept, button_Cancel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(PromocionesDTO.getPromocionesDTO().getPromociones() != null){
            cargarDatos(PromocionesDTO.getPromocionesDTO().getPromociones());
        }
    }    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    private Main stage;
    private int op;

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
    
    public void cargarDatos(Promociones promocion){
        tf_PromoId.setText(Integer.toString(promocion.getPromocionId()));
        tf_ProdId.setText(Integer.toString(promocion.getProductoId()));
        tf_FechInicio.setText(promocion.getFechaInicio());
        tf_FechFinal.setText(promocion.getFechaFinal());
        tf_Precio.setText(Double.toString(promocion.getPrecioPromocion()));
        ta_Descripcion.setText(promocion.getDescripcionPromocion());
    }
    
    public void agregarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarPromocion(?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(tf_Precio.getText()));
            statement.setString(2, ta_Descripcion.getText());
            statement.setString(3, tf_FechInicio.getText());
            statement.setString(4, tf_FechFinal.getText());
            statement.setInt(5, Integer.parseInt(tf_ProdId.getText()));
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
    
    public void editarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarPromocion(?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_PromoId.getText()));
            statement.setDouble(2, Double.parseDouble(tf_Precio.getText()));
            statement.setString(3, ta_Descripcion.getText());
            statement.setString(4, tf_FechInicio.getText());
            statement.setString(5, tf_FechFinal.getText());
            statement.setInt(6, Integer.parseInt(tf_ProdId.getText()));
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
       if(event.getSource() == button_Acept){
            if(op == 1){
                
                if(!tf_Precio.getText().equals("") && !tf_FechInicio.getText().equals("") && !tf_FechFinal.getText().equals("") && !ta_Descripcion.getText().equals("")){
                    agregarPromocion();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuPromociones();
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tf_Precio.getText().equals("")){
                        tf_Precio.requestFocus();
                    } else if(tf_FechInicio.getText().equals("")) {
                        tf_FechInicio.requestFocus();
                    } else if(tf_FechFinal.getText().equals("")){
                        tf_FechFinal.requestFocus();
                    }else if(ta_Descripcion.getText().equals("")){
                        ta_Descripcion.requestFocus();
                    }
                }
            }else if(op == 2){
                
               if(!tf_Precio.getText().equals("") && !tf_FechInicio.getText().equals("") && !tf_FechFinal.getText().equals("") && !ta_Descripcion.getText().equals("")){  
                   
                   if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                       editarPromocion();
                       SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                       PromocionesDTO.getPromocionesDTO().setPromociones(null);
                       stage.menuPromociones();
                   }else{
                       stage.menuPromociones();
                   }
                }else{
                   SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    if(tf_Precio.getText().equals("")){
                        tf_Precio.requestFocus();
                    } else if(tf_FechInicio.getText().equals("")) {
                        tf_FechInicio.requestFocus();
                    } else if(tf_FechFinal.getText().equals("")){
                        tf_FechFinal.requestFocus();
                    }else if(ta_Descripcion.getText().equals("")){
                        ta_Descripcion.requestFocus();
                    }
               }   
            }
        } else if(event.getSource()== button_Cancel){
            stage.menuPromociones();
            PromocionesDTO.getPromocionesDTO().setPromociones(null);
        }        
    }
    
}
