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
import org.diegogarcia.dto.CategorasDTO;
import org.diegogarcia.model.CategoriaProducto;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class MenuCategoriaProductosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Main stage;
    private int op;
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    @FXML
    Button button_Agr, button_Editar, btnEliminarCatePro, button_Buscar, button_Regresar;
    @FXML
    TextField tf_Buscar;
    @FXML
    TableView tbl_Categorias; 
    @FXML
    TableColumn col_ID, col_categoria, col_Descripcion;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    
    
    public void cargarDatos(){
        if(op == 3){ 
            tbl_Categorias.getItems().add(BuscarCategoriaProductos());
            op = 0;
        }else {
            tbl_Categorias.setItems(listarCategoriaProductos());
        }
        col_ID.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer> ("categoriaProductosId"));
        col_categoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String> ("nombreCategoria"));
        col_Descripcion.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String> ("descripcionCategoria"));
    }
    
    public ObservableList<CategoriaProducto> listarCategoriaProductos(){
        ArrayList<CategoriaProducto> CategoriaProducto = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCategoriaProductos();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int categoriaProductosId = resultSet.getInt("categoriaProductosId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                
                CategoriaProducto.add(new CategoriaProducto(categoriaProductosId, nombreCategoria, descripcionCategoria));
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
        
        return FXCollections.observableList(CategoriaProducto);
    } 
    
    public void eliminarCargos(int catProId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCategoriaProductos(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, catProId);
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
    
    public CategoriaProducto BuscarCategoriaProductos(){
        CategoriaProducto categoriaProducto = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarCategoriaProductos(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_Buscar.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int categoriaProductosId = resultSet.getInt("categoriaProductosId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                
                categoriaProducto = new CategoriaProducto(categoriaProductosId, nombreCategoria, descripcionCategoria);
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
        return categoriaProducto;
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_Regresar){
            stage.menuPrincipalView();
        } else if(event.getSource()== button_Agr){
            stage.formCategoriasView(1);
        } else if(event.getSource()== button_Editar){
            CategorasDTO.getCategorasDTO().setCategoriaProducto((CategoriaProducto)tbl_Categorias.getSelectionModel().getSelectedItem());
            stage.formCategoriasView(2);
            cargarDatos();
        }else if(event.getSource()== btnEliminarCatePro){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarCargos(((CategoriaProducto)tbl_Categorias.getSelectionModel().getSelectedItem()).getCategoriaProductosId());
                cargarDatos();
            }         
        }else if(event.getSource() == button_Buscar){
            tbl_Categorias.getItems().clear();
            if(tf_Buscar.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }
        
    }  
    
}
