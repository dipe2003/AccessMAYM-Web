/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.acciones;

import com.dperez.maymweb.acciones.Accion;
import static com.dperez.maymweb.acciones.TipoAccion.PREVENTIVA;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author dperez
 */
@Entity
public class Mejora extends Accion implements Serializable {

    // Constructores
    public Mejora(Date FechaDeteccion, String Descripcion){
        super(FechaDeteccion, Descripcion, PREVENTIVA);
    }
    public Mejora(){}

    
}
