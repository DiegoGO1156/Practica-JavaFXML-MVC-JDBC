/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.dto;

import org.diegogarcia.model.CompraDetalle;

/**
 *
 * @author diego
 */
public class DetalleComDTO {
    
    private static DetalleComDTO instance;
    private CompraDetalle compraDet;

    public CompraDetalle getCompraDetalle() {
        return compraDet;
    }

    public void setCompraDetalle(CompraDetalle compraDet) {
        this.compraDet = compraDet;
    }
    
    
    
    private DetalleComDTO (){
        
    }
    
    public static DetalleComDTO getDetalleComDTO(){
        if(instance == null){
            instance = new DetalleComDTO();
        }
        return instance;
    }

}
