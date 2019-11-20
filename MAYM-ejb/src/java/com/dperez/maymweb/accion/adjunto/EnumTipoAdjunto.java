/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.adjunto;

/**
 *
 * @author dperez
 */
public enum EnumTipoAdjunto {
    IMAGEN ("Imagen"),
    DOCUMENTO ("Documento");
    
    private final String Descripcion;  
     
    EnumTipoAdjunto(String descripcion){
        this.Descripcion = descripcion;
    }
    public String getDescripcion(){
        return this.Descripcion;
    }
}
