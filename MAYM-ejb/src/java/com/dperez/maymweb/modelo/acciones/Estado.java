/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones;


/**
 *
 * @author dperez
 */
public enum Estado {
    PENDIENTE ("Pendiente"),
    PROCESO_IMP ("En Proceso (Implementacion)"),
    PROCESO_VER ("En Proceso (Verificacion)"),
    CERRADA ("Cerrada"),
    DESESTIMADA("Desestimada");
    
    private final String Descripcion;  
     
    Estado(String descripcion){
        this.Descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
}
