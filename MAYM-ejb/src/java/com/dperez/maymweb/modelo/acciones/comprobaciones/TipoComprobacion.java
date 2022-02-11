/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones.comprobaciones;

/**
 *
 * @author dperez
 */
public enum TipoComprobacion {
    EFICACIA ("Eficacica"),
    IMPLEMENTACION ("Implementacion");
    
    private final String Descripcion;  
     
    TipoComprobacion(String descripcion){
        this.Descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
}
