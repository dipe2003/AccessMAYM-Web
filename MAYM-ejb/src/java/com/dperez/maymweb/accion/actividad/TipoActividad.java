/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.actividad;

/**
 *
 * @author dperez
 */
public enum TipoActividad {
    CORRECTIVA ("Correctiva"),
    PREVENTIVA ("Preventiva"),
    MEJORA ("Mejora");
    
    private final String Descripcion;  
     
    TipoActividad(String descripcion){
        this.Descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
}
