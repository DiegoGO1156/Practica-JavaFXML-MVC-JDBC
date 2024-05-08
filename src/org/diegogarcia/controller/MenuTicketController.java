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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.model.Cliente;
import org.diegogarcia.model.TicketSoporte;
import org.diegogarcia.system.Main;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class MenuTicketController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    
    @FXML
    Button BTN_regresar, BTN_Guardado, BTN_Vaciado;
    
    @FXML
    TextField tf_TicketId;
    
    @FXML 
    TextArea ta_Desc;
    
    @FXML 
    ComboBox cmb_Cliente, cmb_Factura, cmb_Estatus;
    
    @FXML 
    TableView tb_Tick;
    
    @FXML 
    TableColumn col_TicketId, col_Descri, col_Cliente, col_FacturaId;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cargarCmbEstatus();
        cmb_Cliente.setItems(listarClientes());
        cargarCmbFactura();
    }  
    private Main stage;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    public ObservableList <TicketSoporte> listarTickets(){
        ArrayList <TicketSoporte> tickets = new ArrayList<>();
        
        try{
          conexion = Conexion.getInstance().obtenerConexion();
          String sql = "call listarTicketSoporte();";
          statement = conexion.prepareStatement(sql);
          resultSet = statement.executeQuery();
          
          while(resultSet.next()){
              int ticketId = resultSet.getInt("");
              String descripcion = resultSet.getString("descripcionTicket");
              String estatus = resultSet.getString("estatus");
              String cliente = resultSet.getString("cliente");
              int facturaId = resultSet.getInt("facturaId");
              
              tickets.add(new TicketSoporte(ticketId, descripcion, estatus, cliente, facturaId));
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
    
       return FXCollections.observableList(tickets);
    }
    
    public ObservableList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarTicketSoporte();";
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
                
            }
        }
        
        return FXCollections.observableList(clientes);
    } 
    
   public void cargarDatos(){
       tb_Tick.setItems(listarTickets());
       col_TicketId.setCellFactory(new PropertyValueFactory<TicketSoporte, Integer>("ticketSoporteId")); 
       col_Descri.setCellFactory(new PropertyValueFactory<TicketSoporte, String>("descripcion")); 
       col_Descri.setCellFactory(new PropertyValueFactory<TicketSoporte, String>("estatus")); 
       col_Descri.setCellFactory(new PropertyValueFactory<TicketSoporte, String>("cliente")); 
       col_Descri.setCellFactory(new PropertyValueFactory<TicketSoporte, Integer>("facturaId")); 
   }
   
   public void cargarCmbEstatus(){
       cmb_Factura.getItems().add(1);
   }

   public void cargarCmbFactura(){
       cmb_Estatus.getItems().add("En proceso");
       cmb_Estatus.getItems().add("Finalizado");
   }
   
   @FXML
   public void cargaForm(){
       TicketSoporte ts = (TicketSoporte)tb_Tick.getSelectionModel().getSelectedItem();
       if(ts != null){
           tf_TicketId.setText(Integer.toString(ts.getTicketSoporteId()));
           ta_Desc.setText(ts.getDescripcion());
           cmb_Estatus.getSelectionModel().select(0);
           cmb_Cliente.getSelectionModel().select(obtenerIndexCliente());
           cmb_Factura.getSelectionModel().select(0);
       }
   }
   
   public int obtenerIndexCliente(){
       int index = 0;
       String clienteTbl = (((TicketSoporte)tb_Tick.getSelectionModel().getSelectedItems()).getCliente());
       
       for(int i = 0; i <= cmb_Cliente.getItems().size(); i++){
        String cliemteCmb = cmb_Cliente.getItems().get(i).toString();
        
        
            if(clienteTbl.equals(cliemteCmb)){
                index = i;
                break;
            }
        
       }
       
       return index;
   }
   
   public void agregarTickets(){
       try{
           conexion = Conexion.getInstance().obtenerConexion();
           String sql = "call sp_agregarTicketSoporte(?,?,?)";
           
           statement = conexion.prepareStatement(sql);
           
           statement.setString(1, ta_Desc.getText());
           statement.setInt(2, ((Cliente)cmb_Cliente.getSelectionModel().getSelectedItem()).getClienteId());
           statement.setInt(3, Integer.parseInt(cmb_Factura.getSelectionModel().getSelectedItem().toString()));
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
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
   }
   
   public void editarTickets(){
       try{
           conexion = Conexion.getInstance().obtenerConexion();
           String sql = "call sp_editarTicketSoporte(?,?,?,?,?)";
           statement = conexion.prepareStatement(sql);
           
           
       }catch (SQLException e){
           System.out.println(e.getMessage());
       }finally{
           
       }
   }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == BTN_regresar){
            stage.menuPrincipalView();
        } else if(event.getSource() == BTN_Guardado){
            agregarTickets();
            cargarDatos();
        }else if(event.getSource() == BTN_Vaciado){
            
        }
    }
    
}
