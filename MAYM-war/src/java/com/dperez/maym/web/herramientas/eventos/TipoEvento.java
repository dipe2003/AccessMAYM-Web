/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.herramientas.eventos;

/**
 *
 * @author Diego
 */
public enum TipoEvento{
    IMPLEMENTACION_ACCION ("Implementacion de Accion"),
    VERIFICACION_EFICACIA ("Verificacion de Eficacia");
    
    private final String Descripcion;
    
    TipoEvento(String Descripcion){
        this.Descripcion = Descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
}
