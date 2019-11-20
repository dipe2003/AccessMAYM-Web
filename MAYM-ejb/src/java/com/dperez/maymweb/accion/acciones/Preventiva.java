/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.acciones;

import com.dperez.maymweb.accion.Accion;
import static com.dperez.maymweb.accion.TipoAccion.PREVENTIVA;
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
    public Preventiva(Date FechaDeteccion, String Descripcion){
        super(FechaDeteccion, Descripcion, PREVENTIVA);
    }
    public Preventiva(){}

    
}
