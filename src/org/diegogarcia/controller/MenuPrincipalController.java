/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.diegogarcia.system.Main;

/**
 *
 * @author diego
 */
public class MenuPrincipalController implements Initializable{
    
    private Main stage;
    
    @Override
    public void initialize (URL Location, ResourceBundle resources){
    
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    @FXML
    MenuItem Button_clientes;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == Button_clientes){
            stage.menuClientesView();
        }
    }
    
    
}
