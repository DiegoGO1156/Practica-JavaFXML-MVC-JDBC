/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.report;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.diegogarcia.dao.Conexion;
import win.zqxu.jrviewer.JRViewerFX;

/**
 *
 * @author informatica
 */
public class GenerarReport {
    private static GenerarReport instance;
    
    private static Connection conexion = null;
    
    private GenerarReport(){
        
    }
    
    public static GenerarReport getInstance(){
        if(instance == null){
            instance = new GenerarReport();
        }
        return instance;
    }
    
    public void generarFactura(int factId){
        try{
            
            conexion = Conexion.getInstance().obtenerConexion();
            
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("facId", factId);
            
            Stage reportStage = new Stage();
            
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReport.class.getResourceAsStream("/org/diegogarcia/report/Factura.jasper"), parametros, conexion);
            
            JRViewerFX reportView = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            root.getChildren().add(reportView);
            
            reportView.setPrefSize(1000,800);
            
            Scene scene = new Scene (root);
            
            reportStage.setScene(scene);
            reportStage.setTitle("Factura");
            reportStage.show();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarRegClientes(){
        try{
             conexion = Conexion.getInstance().obtenerConexion();
            
            Map<String, Object> parametros = new HashMap<>();
            
            Stage reportStage = new Stage();
            
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReport.class.getResourceAsStream("/org/diegogarcia/report/Clientes.jasper"), parametros, conexion);
            
            JRViewerFX reportView = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            root.getChildren().add(reportView);
            
            reportView.setPrefSize(1000,800);
            
            Scene scene = new Scene (root);
            
            reportStage.setScene(scene);
            reportStage.setTitle("Clientes");
            reportStage.show();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarProductos(){
        try{
             conexion = Conexion.getInstance().obtenerConexion();
            
            Map<String, Object> parametros = new HashMap<>();
            
            Stage reportStage = new Stage();
            
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReport.class.getResourceAsStream("/org/diegogarcia/report/Productos.jasper"), parametros, conexion);
            
            JRViewerFX reportView = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            root.getChildren().add(reportView);
            
            reportView.setPrefSize(1000,800);
            
            Scene scene = new Scene (root);
            
            reportStage.setScene(scene);
            reportStage.setTitle("Clientes");
            reportStage.show();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
