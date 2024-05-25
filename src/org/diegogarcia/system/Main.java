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
import org.diegogarcia.controller.EmpleadosController;
import org.diegogarcia.controller.MenuAgregarClienteController;
import org.diegogarcia.controller.FormCargoController;
import org.diegogarcia.controller.FormCategoriaController;
import org.diegogarcia.controller.FormComprasController;
import org.diegogarcia.controller.FormDetalleCompraController;
import org.diegogarcia.controller.FormEmpleadosController;
import org.diegogarcia.controller.FormPromocionesController;
import org.diegogarcia.controller.FromDistribuidoresController;
import org.diegogarcia.controller.MenuCargoController;
import org.diegogarcia.controller.MenuCategoriaProductosController;
import org.diegogarcia.controller.MenuPrincipalController;
import org.diegogarcia.controller.MenuClientesController;
import org.diegogarcia.controller.MenuDistribuidorController;
import org.diegogarcia.controller.MenuPromocionesController;
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
            DetalleCompraController menuDetalleCompras = (DetalleCompraController) switchScene("DetalleCompraView.fxml", 1012, 900);
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
    
    public void menuPromociones(){
        try{
            MenuPromocionesController menuPromociones = (MenuPromocionesController)switchScene("MenuPromocionesView.fxml", 900,600);
            menuPromociones.setStage(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void formPromociones(int op){
        try{
            FormPromocionesController formPromociones = (FormPromocionesController) switchScene("FormPromocionesView.fxml", 1000, 700);
            formPromociones.setOp(op);
            formPromociones.setStage(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuDistribuidorView(){
        try{
            MenuDistribuidorController menuDistribuidorView = (MenuDistribuidorController) switchScene("MenuDistribuidorView.fxml", 1200, 600);
            menuDistribuidorView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void formDistribuidorView(int op){
        try{
            FromDistribuidoresController formDistribuidorView = (FromDistribuidoresController)switchScene("FromDistribuidores.fxml", 500, 800);
            formDistribuidorView.setOp(op);
            formDistribuidorView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void menuCategoriasView(){
        try{
            MenuCategoriaProductosController menuCategoriasView = (MenuCategoriaProductosController) switchScene("MenuCategoriaProductosView.fxml", 750, 900);
            menuCategoriasView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void formCategoriasView(int op){
        try{
            FormCategoriaController FormCategoriaController = (FormCategoriaController)switchScene("FormCategoriaView.fxml", 875, 700);
            FormCategoriaController.setOp(op);
            FormCategoriaController.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void menuEmpleadosView(){
        try{
            EmpleadosController menuEmpleadosView = (EmpleadosController) switchScene("EmpleadosView.fxml", 1050, 656);
            menuEmpleadosView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void formEmpleadosView(int op){
        try{
            FormEmpleadosController formEmpleadosView = (FormEmpleadosController)switchScene("FormEmpleadosView.fxml", 956, 844);
            formEmpleadosView.setOp(op);
            formEmpleadosView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void formCompra(int op){
        try{
            FormComprasController formEmpleadosView = (FormComprasController)switchScene("FormCompras.fxml", 600, 750);
            formEmpleadosView.setOp(op);
            formEmpleadosView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
