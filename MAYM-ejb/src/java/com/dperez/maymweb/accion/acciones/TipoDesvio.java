/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.acciones;

/**
 *
 * @author dperez
 */
public enum TipoDesvio {
    NC_CRITICA ("No Conformidad Critica"),
    NC_MAYOR ("No Conformidad Mayor"),
    NC_MENOR_OBS ("No Conformidad Menor/Observacion");
    
    private final String Descripcion;  
     
    TipoDesvio(String descripcion){
        this.Descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
    
}
