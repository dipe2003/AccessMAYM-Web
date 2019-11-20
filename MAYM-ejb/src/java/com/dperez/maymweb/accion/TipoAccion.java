/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion;

/**
 *
 * @author dperez
 */
public enum TipoAccion {
    CORRECTIVA ("Accion Correctiva"),
    PREVENTIVA ("Accion Preventiva"),
    MEJORA ("Oportunidad de Mejora");
    
    private final String Descripcion;  
     
    TipoAccion(String descripcion){
        this.Descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
}
