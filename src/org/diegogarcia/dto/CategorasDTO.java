/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.dto;

import org.diegogarcia.model.CategoriaProducto;

/**
 *
 * @author diego
 */
public class CategorasDTO {
    private static CategorasDTO instance;
    private CategoriaProducto CategoriaProducto;

    public CategoriaProducto getCategoriaProducto() {
        return CategoriaProducto;
    }

    public void setCategoriaProducto(CategoriaProducto CategoriaProducto) {
        this.CategoriaProducto = CategoriaProducto;
    }
    
    
    
    private CategorasDTO (){
        
    }
    
    public static CategorasDTO getCategorasDTO(){
        if(instance == null){
            instance = new CategorasDTO();
        }
        return instance;
    }
}
