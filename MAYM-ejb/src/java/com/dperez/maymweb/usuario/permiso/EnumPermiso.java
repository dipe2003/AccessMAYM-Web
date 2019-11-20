/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.usuario.permiso;


/**
 *
 * @author Diego
 */
public enum EnumPermiso {
    LECTURA ("Lectura"),
    DATOS ("Datos"),
    VERIFICADOR ("Verificador"),
    ADMINISTRADOR ("Administrador");
    
    private final String Descripcion;  
     
    EnumPermiso(String descripcion){
        this.Descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return this.Descripcion;
    }
}
