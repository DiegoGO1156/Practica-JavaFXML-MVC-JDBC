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
import org.diegogarcia.dto.ClienteDTO;
import org.diegogarcia.model.Cliente;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;
/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuClientesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Main stage;
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    //Cliente cliente = new Cliente();
    
    @FXML
    Button button_regresar, button_eliminar, button_editar,button_agregar, button_Eliminar, button_lupa;
    
    @FXML
    TableView tblClientes;
    
    @FXML
    TableColumn colClienteId, colNombre, colApellido, colTelefono, colDireccion, colNit;
    
    @FXML
    TextField tfBarra;

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }   
    
    public void cargarDatos(){
        if(op == 3){ 
            tblClientes.getItems().add(buscarCliente());
            op = 0;
        }else {
            tblClientes.setItems(listarClientes());
        }
        colClienteId.setCellValueFactory(new PropertyValueFactory<Cliente, Integer> ("clienteId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String> ("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Cliente, String> ("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String> ("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String> ("direccion"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String> ("nit"));
    }
    
    public ObservableList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarClientes();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                String nit = resultSet.getString("nit");
                
                clientes.add(new Cliente(clienteId, nombre, apellido, telefono, direccion, nit));
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
        
        return FXCollections.observableList(clientes);
    } 
    
    public Cliente buscarCliente(){
        Cliente cliente = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_BuscarCliente(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBarra.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                String nit = resultSet.getString("nit");
                
                cliente = new Cliente(clienteId, nombre, apellido, telefono, direccion, nit);
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
        return cliente;
    }
    
    public void eliminarClientes(int clidId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_EliminarCliente(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, clidId);
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
    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_regresar){
            stage.menuPrincipalView();
        } else if(event.getSource()== button_agregar){
            stage.formClienteView(1);
        } else if(event.getSource()== button_editar){
            ClienteDTO.getClienteDTO().setCliente((Cliente)tblClientes.getSelectionModel().getSelectedItem());
            stage.formClienteView(2);
            cargarDatos();
        }else if(event.getSource()== button_eliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(700).get() == ButtonType.OK){
                eliminarClientes(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getClienteId());
                cargarDatos();
            }
        }else if(event.getSource() == button_lupa){
            tblClientes.getItems().clear();
            if(tfBarra.getText().equals("")){
                cargarDatos();
            }else {
                op = 3;
                cargarDatos();
            }
        }
        
    }
}
