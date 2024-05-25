/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.dto;

import org.diegogarcia.model.Compras;

/**
 *
 * @author diego
 */
public class ComprasDTO {
    
    private static ComprasDTO instance;
    private Compras compra;

    public Compras getCompras() {
        return compra;
    }

    public void setCompras(Compras compra) {
        this.compra = compra;
    }
    
    
    
    private ComprasDTO (){
        
    }
    
    public static ComprasDTO getComprasDTO(){
        if(instance == null){
            instance = new ComprasDTO();
        }
        return instance;
    }
}
