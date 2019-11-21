/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.dperez.maymweb.herramientas;

import com.dperez.maymweb.acciones.Accion;
import java.io.Serializable;

/**
 *
 * @author dperez
 * 21/11/2019
 */
public class EventoAccion extends Evento implements Serializable {
    private TipoEvento Tipo;
    private Accion Accion;

    public EventoAccion(TipoEvento Tipo, Accion Accion) {
        this.Tipo = Tipo;
        this.Accion = Accion;
    }    
        
    public TipoEvento getTipo() {return Tipo;}
    public void setTipo(TipoEvento Tipo) {this.Tipo = Tipo;}
    
    public Accion getAccion() {return Accion;}
    public void setAccion(Accion IdAccion) {this.Accion = IdAccion;}
}
