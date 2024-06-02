/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.model;

/**
 *
 * @author diego
 */
public class NivelAcceso {
    
    private int nivelesAccesoId;
    private String nivelAcceso;
    
    public NivelAcceso(){
        
    }
    
    public NivelAcceso(int nivelesAccesoId, String nivelAcceso){
        this.nivelesAccesoId = nivelesAccesoId;
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelesAccesoId() {
        return nivelesAccesoId;
    }

    public void setNivelesAccesoId(int nivelesAccesoId) {
        this.nivelesAccesoId = nivelesAccesoId;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
    
    @Override
    public String toString(){
        return "Nivel de Acceso: " + nivelAcceso;
    }
    
}
