/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.dperez.maymweb.herramientas;

import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import java.io.Serializable;

/**
 *
 * @author dperez
 * 21/11/2019
 */
public class EventoActividad extends Evento implements Serializable{
    private Actividad Actividad;

    public EventoActividad(Actividad Actividad) {
        this.Actividad = Actividad;
    }    
    
    public Actividad getActividad() {return Actividad;}
    public void setActividad(Actividad IdActividad) {this.Actividad = IdActividad;}
}
