 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.system;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.diegogarcia.controller.DetalleCompraController;
import org.diegogarcia.controller.MenuAgregarClienteController;
import org.diegogarcia.controller.FormCargoController;
import org.diegogarcia.controller.FormDetalleCompraController;
import org.diegogarcia.controller.MenuCargoController;
import org.diegogarcia.controller.MenuPrincipalController;
import org.diegogarcia.controller.MenuClientesController;
import org.diegogarcia.controller.MenuTicketController;
import org.diegogarcia.controller.ProductoController;


/**
 *
 * @author diego
 */
public class Main extends Application {
    
    private Stage stage;
    private Scene scene;
    private final String VIEWURL = "/org/diegogarcia/view/";
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Super Kinal App");
        menuPrincipalView();
        //menuClientesView();
        stage.show();
    }
    
    public Initializable switchScene(String fxmlName, int width, int heigth)throws Exception{
        Initializable resultado = null;
        FXMLLoader loader  = new FXMLLoader();
        
        InputStream file = Main.class.getResourceAsStream(VIEWURL + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(VIEWURL+ fxmlName));
        
        scene = new Scene((AnchorPane)loader.load(file), width, heigth);
        stage.setScene(scene);
        stage.sizeToScene();
        
        resultado = (Initializable)loader.getController();
        
        return resultado;
    }
    
    public void menuPrincipalView(){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)switchScene("MenuPrincipalView.fxml", 950, 700);
            menuPrincipalView.setStage(this);
        } catch(Exception e){
           e.printStackTrace();
        }
    }
    
    
    public void menuClientesView(){
        try{
            MenuClientesController menuClientesView = (MenuClientesController) switchScene("MenuClientesView.fxml", 1200, 750);
            menuClientesView.setStage(this);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    public void formClienteView(int op){
        try{
            MenuAgregarClienteController formClienteView = (MenuAgregarClienteController) switchScene("formCliente.fxml", 386, 649);
            formClienteView.setOp(op);
            formClienteView.setStage(this);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void menuTicketView(){
        try{
            MenuTicketController menuTicketView = (MenuTicketController) switchScene("MenuTicketView.fxml", 1200, 750);
            menuTicketView.setStage(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    
    public void productoView(){
        try{
            ProductoController productoView = (ProductoController) switchScene("ProductoView.fxml", 1600, 850);
            productoView.setStage(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    
    public void formCargosView(int op){
        try{
            FormCargoController formCargosView = (FormCargoController) switchScene("FormCargoView.fxml", 500, 400);
            formCargosView.setOp(op);
            formCargosView.setStage(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    
    public void menuCargosView(){
        try{
            MenuCargoController menuCargosView = (MenuCargoController) switchScene("MenuCargoView.fxml", 500, 600);
            menuCargosView.setStage(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    
    public void menuDetalleCompras(){
        try{
            DetalleCompraController menuDetalleCompras = (DetalleCompraController) switchScene("DetalleCompraView.fxml", 675, 731);
            menuDetalleCompras.setStage(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    
    public void formDetalleCompra(int op){
        try{
            FormDetalleCompraController formDetalleCompra = (FormDetalleCompraController) switchScene("FormDetalleCompraView.fxml", 675, 844);
            formDetalleCompra.setOp(op);
            formDetalleCompra.setStage(this);
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
