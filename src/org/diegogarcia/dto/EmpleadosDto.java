/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.dto;

import org.diegogarcia.model.Empleados;

/**
 *
 * @author diego
 */
public class EmpleadosDto {
    private static EmpleadosDto instance;
    private Empleados empleado;

    public Empleados getEmpleados() {
        return empleado;
    }

    public void setEmpleados(Empleados cargos) {
        this.empleado = cargos;
    }
    
    
    
    private EmpleadosDto (){
        
    }
    
    public static EmpleadosDto getEmpleadosDto(){
        if(instance == null){
            instance = new EmpleadosDto();
        }
        return instance;
    }
}
