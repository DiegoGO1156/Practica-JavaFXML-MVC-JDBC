/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author diego
 */
public class SuperKinalAlert {
    
    private static SuperKinalAlert instance;
    
    private SuperKinalAlert(){
        
    }
    
    public static SuperKinalAlert getInstance(){
        if(instance == null){
            instance = new SuperKinalAlert();
        }
        return instance;
    }
    
    public void mostrarAlertaInfo(int code){
        if(code == 600){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmacion de Registro");
            alert.setHeaderText("Confirmacion de Registro");
            alert.setContentText("Registro Realizado con Exito!");
            alert.showAndWait();
        }else if(code == 500){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmacion de Edicion");
            alert.setHeaderText("Confirmacion de Edicion");
            alert.setContentText("Edicion Realizada con Exito!");
            alert.showAndWait();
        }else if(code == 400){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Campos Pendientes");
           alert.setHeaderText("Campos Pendientes");
           alert.setContentText("Aún quedan campos necesiario se encuentran vacios!");
           alert.showAndWait(); 
        }else if(code == 200){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Campos llenos");
           alert.setHeaderText("Campos llenos");
           alert.setContentText("Utilice el boton vaciar para realizar el registro");
           alert.showAndWait();
        }
    }
    
    public Optional <ButtonType> mostrarAlertaConfirmacion(int code){
        Optional <ButtonType> action = null;
        if(code == 700){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminacion de Registro");
            alert.setHeaderText("Eliminacion de Registro");
            alert.setContentText("¿Desea confirmar la eliminación?");
            action = alert.showAndWait();
        }else if(code == 800){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edicion Registro");
            alert.setHeaderText("Edicion Registro");
            alert.setContentText("¿Desea confirmar la Edicion?");
            action = alert.showAndWait();
        }
        return action;
    }
    
}
