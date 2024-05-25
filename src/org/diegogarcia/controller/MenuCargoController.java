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
import org.diegogarcia.dto.CargoDTO;
import org.diegogarcia.model.Cargos;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuCargoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
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
    
    @FXML
    Button button_AgregarCarg, button_Editar, button_Eliminar, button_Buscar, button_Regresar;
    
    @FXML
    TextField tf_BuscarCargo;
    
    @FXML
    TableView tbl_Cargo;
    
    @FXML
    TableColumn col_cargoNom, col_cargoDesc, col_cargoId;
    
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
    }   
    
    public void cargarDatos(){
        if(op == 3){ 
            tbl_Cargo.getItems().add(buscarCargo());
            op = 0;
        }else {
            tbl_Cargo.setItems(listarCargos());
        }
        col_cargoId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer> ("cargoId"));
        col_cargoNom.setCellValueFactory(new PropertyValueFactory<Cargos, String> ("nombreCargo"));
        col_cargoDesc.setCellValueFactory(new PropertyValueFactory<Cargos, String> ("descripcionCargo"));
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
    
    public Cargos buscarCargo(){
        Cargos cargos = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarCargo(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_BuscarCargo.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombreCargo = resultSet.getString("nombreCargo");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                
                cargos = new Cargos(cargoId, nombreCargo, descripcionCargo);
            }
            
        }catch(SQLException e ){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (Exception e){
               System.out.println(e.getMessage()); 
            }
        }
        return cargos;
    }
    
    public void eliminarCargos(int cargId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCargo(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, cargId);
            statement.execute();     
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
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
        if(event.getSource() == button_Regresar){
            stage.menuPrincipalView();
        } else if(event.getSource()== button_AgregarCarg){
            stage.formCargosView(1);
        } else if(event.getSource()== button_Editar){
            CargoDTO.getCargoDTO().setCargos((Cargos)tbl_Cargo.getSelectionModel().getSelectedItem());
            stage.formCargosView(2);
            cargarDatos();
        }else if(event.getSource()== button_Eliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarCargos(((Cargos)tbl_Cargo.getSelectionModel().getSelectedItem()).getCargoId());
                cargarDatos();
            }
        }else if(event.getSource() == button_Buscar){
            tbl_Cargo.getItems().clear();
            if(tf_BuscarCargo.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }
        
    }
}
