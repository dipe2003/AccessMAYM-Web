/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maymweb.modelo.acciones;

import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author dperez
 */
@Entity
public class Correctiva extends Accion implements Serializable {
    // Constructores
    public Correctiva() {
    }

    public Correctiva(Date fechaDeteccion, String descripcion, String referencias, Area area, Deteccion deteccion) {
        super(fechaDeteccion, descripcion, referencias, area, deteccion);        
    }

    
}
