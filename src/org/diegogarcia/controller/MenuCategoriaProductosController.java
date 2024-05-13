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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.CategorasDTO;
import org.diegogarcia.model.CategoriaProducto;
import org.diegogarcia.system.Main;

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
    Button btnAgregarCatePro, btnEditarCatePro, btnEliminarCatePro, btnBuscarCatePro, btnRegresarCtPr;
    @FXML
    TextField tfCateProId;
    @FXML
    TableView tblCateProductos; 
    @FXML
    TableColumn colcatgProductosId, colnomCatePro, coldescripcionCatePro;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    
    
    public void cargarDatos(){
        if(op == 3){ 
            tblCateProductos.getItems().add(BuscarCategoriaProductos());
            op = 0;
        }else {
            tblCateProductos.setItems(listarCategoriaProductos());
        }
        colcatgProductosId.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer> ("categoriaProductosId"));
        colnomCatePro.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String> ("nombreCategoria"));
        coldescripcionCatePro.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String> ("descripcionCategoria"));
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
            statement.setInt(1, Integer.parseInt(tfCateProId.getText()));
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
        if(event.getSource() == btnRegresarCtPr){
            stage.menuPrincipalView();
        } else if(event.getSource()== btnAgregarCatePro){
            //stage.formCategoriaProductosView(1);
        } else if(event.getSource()== btnEditarCatePro){
            CategorasDTO.getCategorasDTO().setCategoriaProducto((CategoriaProducto)tblCateProductos.getSelectionModel().getSelectedItem());
            //stage.formCategoriaProductosView(2);
            cargarDatos();
        }else if(event.getSource()== btnEliminarCatePro){
                eliminarCargos(((CategoriaProducto)tblCateProductos.getSelectionModel().getSelectedItem()).getCategoriaProductosId());
                cargarDatos();         
        }else if(event.getSource() == btnBuscarCatePro){
            tblCateProductos.getItems().clear();
            if(tfCateProId.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }
        
    }  
    
}
