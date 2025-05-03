/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones.adjunto;

/**
 *
 * @author dperez
 */
public enum TipoAdjunto {
    IMAGEN ("Imagen"),
    DOCUMENTO ("Documento"),
    VIDEO ("Video/mp4");
    
    private final String Descripcion;  
     
    TipoAdjunto(String descripcion){
        this.Descripcion = descripcion;
    }
    public String getDescripcion(){
        return this.Descripcion;
    }
}
