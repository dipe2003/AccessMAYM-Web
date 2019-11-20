/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.deteccion;


/**
 *
 * @author dperez
 */
/**
 *
 * @author dperez
 */
public enum EnumTipoDeteccion {
    INTERNA ("Deteccion Interna"),
    EXTERNA ("Deteccion Externa");
    
    private final String Descripcion;
    
    EnumTipoDeteccion(String descripcion){
        this.Descripcion = descripcion;
    }
    public String getDescripcion(){
        return this.Descripcion;
    }
}
