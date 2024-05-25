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
import org.diegogarcia.dto.PromocionesDTO;
import org.diegogarcia.model.Promociones;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class MenuPromocionesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private Main stage;
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    @FXML
    Button button_Lupa, button_Agre, button_Elimin, button_Edit, button_Regresar; 
    
    @FXML
    TextField tf_Buscar;
    
    @FXML
    TableView tb_Promociones;
    
    @FXML
    TableColumn col_PromId, col_PrecioPromocion, col_Descripcion, col_FechInicial, col_FechFinal, col_IdProducto; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        if(op == 3){ 
            tb_Promociones.getItems().add(buscarPromocion());
            op = 0;
        }else {
            tb_Promociones.setItems(listarPromocion());
        }
        col_PromId.setCellValueFactory(new PropertyValueFactory<Promociones, Integer> ("promocionId"));
        col_PrecioPromocion.setCellValueFactory(new PropertyValueFactory<Promociones, Double> ("precioPromocion"));
        col_Descripcion.setCellValueFactory(new PropertyValueFactory<Promociones, String> ("descripcionPromocion"));
        col_FechInicial.setCellValueFactory(new PropertyValueFactory<Promociones, String> ("fechaInicio"));
        col_FechFinal.setCellValueFactory(new PropertyValueFactory<Promociones, String> ("fechaFinal"));
        col_IdProducto.setCellValueFactory(new PropertyValueFactory<Promociones, Integer> ("productoId"));
    }
    
    public ObservableList<Promociones> listarPromocion(){
        ArrayList<Promociones> promocion = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarPromocion();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechaFinal = resultSet.getString("fechaFinal");
                int productoId = resultSet.getInt("productoId");
                
                promocion.add(new Promociones(precioPromocion, descripcionPromocion, fechaInicio, fechaFinal, productoId));
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
        
        return FXCollections.observableList(promocion);
    } 
    
    public Promociones buscarPromocion(){
        Promociones promocion = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarPromocion(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, 1);
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechaFinal = resultSet.getString("fechaFinal");
                int productoId = resultSet.getInt("productoId");
                
                promocion = new Promociones(precioPromocion, descripcionPromocion, fechaInicio,fechaFinal, productoId);
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
           }catch(Exception e){
               e.printStackTrace();
           }
        }
        
        return promocion;
    } 
    
    public void eliminarPromocion(int promId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarPromocion(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, promId);
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
        if(event.getSource() == button_Regresar){
            stage.menuPrincipalView();
        }else if(event.getSource()== button_Agre){
            stage.formPromociones(1);
        } else if(event.getSource()== button_Edit){
            PromocionesDTO.getPromocionesDTO().setPromociones((Promociones)tb_Promociones.getSelectionModel().getSelectedItem());
            stage.formPromociones(2);
            cargarDatos();
        }else if(event.getSource()== button_Elimin){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarPromocion(((Promociones)tb_Promociones.getSelectionModel().getSelectedItem()).getPromocionId());
                cargarDatos();
            }
        }else if(event.getSource() == button_Lupa){
            tb_Promociones.getItems().clear();
            if(tf_Buscar.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }
    }
    
    
}
