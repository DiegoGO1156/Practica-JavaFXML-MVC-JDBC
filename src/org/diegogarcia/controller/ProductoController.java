package org.diegogarcia.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.model.CategoriaProducto;
import org.diegogarcia.model.Distribuidores;
import org.diegogarcia.model.Producto;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class ProductoController implements Initializable {
    /**
     * Initializes the controller class.
     */
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    private List<File> files = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmb_Categoría.setItems(listarCategoriaProductos());
        cmb_DIstribuidor.setItems(listarDistribuidor());
    }    
    
    private Main stage;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
    @FXML
    Button button_RegresarMenu, button_VaciarProd, button_AgProd, button_editProd, button_eliminarProd, button_buscarProd;
    
    @FXML
    TextField tf_ProdId, tf_NomProd, tf_CantStock, tf_ventUni, tf_ventMay, tf_preComp;
    
    @FXML
    ComboBox cmb_Categoría, cmb_DIstribuidor;
    
    @FXML
    TextArea ta_DescProd;
    
    @FXML
    ImageView img_CargarImg;
    
    @FXML
    public void handleOnDrag(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
    
    @FXML
    public void handleOnDrop(DragEvent event){
        try{
            files = event.getDragboard().getFiles();
            FileInputStream file = new FileInputStream(files.get(0));
            Image image = new Image(file);
            img_CargarImg.setImage(image);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void agregarProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarProducto(?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            
            statement.setString(1, tf_NomProd.getText());
            statement.setString(2, ta_DescProd.getText());
            statement.setInt(3, Integer.parseInt(tf_CantStock.getText()));
            statement.setDouble(4, Double.parseDouble(tf_ventUni.getText()));
            statement.setDouble(5, Double.parseDouble(tf_ventMay.getText()));
            statement.setDouble(6, Double.parseDouble(tf_preComp.getText()));
            InputStream img = new FileInputStream(files.get(0));
            statement.setBinaryStream(7, img);
            statement.setInt(8, ((CategoriaProducto)(cmb_Categoría.getSelectionModel()).getSelectedItem()).getCategoriaProductosId());
            statement.setInt(9, ((Distribuidores)(cmb_DIstribuidor.getSelectionModel()).getSelectedItem()).getDistribuidorId());
            statement.execute();
            
        }catch(Exception e){
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
    
    public ObservableList<Distribuidores> listarDistribuidor(){
        ArrayList<Distribuidores> distribuidor = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDistribuidores();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                distribuidor.add(new Distribuidores(distribuidorId, nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web));
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
        return FXCollections.observableList(distribuidor);
    }    
    
    public Producto buscarProducto(){
        Producto producto = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_ProdId.getText()));
            
            resultSet = statement.executeQuery();
            
                if(resultSet.next()){
                   int productoId = resultSet.getInt("productoId"); 
                   String nombreProducto = resultSet.getString("nombreProducto"); 
                   String descripcionProducto = resultSet.getString("descripcionProducto"); 
                   Integer cantidadStock = resultSet.getInt("cantidadStock"); 
                   Double precioVentaUnitario = resultSet.getDouble("precioVentaUnitario"); 
                   Double precioVentaMayor = resultSet.getDouble("precioVentaMayor"); 
                   Double precioCompra = resultSet.getDouble("precioCompra"); 
                   Blob imagenProducto = resultSet.getBlob("imagenProducto"); 
                   int distribuidorId = resultSet.getInt("distribuidorId"); 
                   int categoriaId = resultSet.getInt("categoriaProductosId");
                   
                   producto = new Producto(productoId, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaId);
                }
            
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
                if(resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return producto;
    }
    
    public void eliminarProducto(int prodId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql =  "call sp_eliminarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, prodId);
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
    
    public int obtenerIndexDistribuidor() {
        int index = -1;
        int distribuidorId = Integer.parseInt(tf_ProdId.getText()); 
        
        for (int i = 0; i < cmb_DIstribuidor.getItems().size(); i++) {
            Distribuidores distribuidor = (Distribuidores) cmb_DIstribuidor.getItems().get(i);
            if (distribuidor.getDistribuidorId() == distribuidorId) {
                index = i;
                break;
            }
        }

        return index;
    }
       
    public int obtenerIndexCategoria() {
        int index = -1;
        int categoriaId = Integer.parseInt(tf_ProdId.getText()); 

        for (int i = 0; i < cmb_Categoría.getItems().size(); i++) {
            CategoriaProducto categoria = (CategoriaProducto) cmb_Categoría.getItems().get(i);
            if (categoria.getCategoriaProductosId() == categoriaId) {
                index = i;
                break;
            }
        }

        return index;
    }
    
    public void vaciarProd(){
        tf_NomProd.clear();
        cmb_DIstribuidor.getSelectionModel().select(null);
        cmb_Categoría.getSelectionModel().select(null);
        tf_ProdId.clear();
        tf_CantStock.clear();
        tf_ventUni.clear();
        tf_ventMay.clear();
        tf_preComp.clear();
        ta_DescProd.clear();
        img_CargarImg.setImage(null);
    }
    
    
    public void handleButtonAction(ActionEvent event){  
        try{
            if(event.getSource() == button_RegresarMenu){
            stage.menuPrincipalView();
            } else if(event.getSource() == button_VaciarProd){
                vaciarProd();
            }else if(event.getSource() == button_AgProd){
                
                if(tf_NomProd.getText().equals("") && tf_CantStock.getText().equals("") && tf_ventUni.getText().equals("")&& tf_ventMay.getText().equals("") && tf_preComp.getText().equals("") && ta_DescProd.getText().equals("")){
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                }else if(!tf_ProdId.getText().equals("")){
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(200);
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    agregarProducto();
                }
            }else if(event.getSource() == button_eliminarProd){
                    eliminarProducto(Integer.parseInt(tf_ProdId.getText())); 
            }else if(event.getSource() == button_buscarProd){
                Producto producto = buscarProducto();
                    if(producto != null){
                        tf_NomProd.setText(producto.getNombreProducto());
                        InputStream file = producto.getImagenProducto().getBinaryStream();
                        Image image = new Image(file);
                        img_CargarImg.setImage(image);
                        cmb_DIstribuidor.getSelectionModel().select(obtenerIndexDistribuidor());
                        cmb_Categoría.getSelectionModel().select(obtenerIndexCategoria());
                        tf_CantStock.setText(Integer.toString(producto.getCantidadStock()));
                        tf_ventUni.setText(Double.toString(producto.getPrecioVentaUnitario()));
                        tf_ventMay.setText(Double.toString(producto.getPrecioVentaMayor()));
                        tf_preComp.setText(Double.toString(producto.getPrecioCompra()));
                        ta_DescProd.setText(producto.getDescripcionProducto());
                }else if(producto == null){
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(100); 
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    
}
