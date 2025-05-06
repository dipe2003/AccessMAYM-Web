/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones;

import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author dperez
 */
@Entity
public class Preventiva extends Accion implements Serializable {

    // Constructores
    public Preventiva(Date fechaDeteccion, String descripcion, String referencias, Area area, Deteccion deteccion, Codificacion codificacion){
        super(fechaDeteccion, descripcion, referencias, area, deteccion, codificacion);
    }
    public Preventiva(){}

    
}
