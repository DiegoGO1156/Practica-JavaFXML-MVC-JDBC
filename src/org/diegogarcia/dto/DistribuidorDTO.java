/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.dto;

import org.diegogarcia.model.Distribuidores;

/**
 *
 * @author diego
 */
public class DistribuidorDTO {
    private static DistribuidorDTO instance;
    private Distribuidores distribuidor;
    
    private DistribuidorDTO(){
    }

    public static DistribuidorDTO getDistribuidorDTO(){
        if(instance == null){
            instance = new DistribuidorDTO();
        }
        return instance;
    }

    public Distribuidores getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Distribuidores distribuidor) {
        this.distribuidor = distribuidor;
    }
}
