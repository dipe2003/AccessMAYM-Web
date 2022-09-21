/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.general;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Diego
 */
@Named
@ViewScoped
public class ModalVerActividades implements Serializable{
    
    private TipoAccion tipoDeAccion;
    
    
    private Accion accionSeleccionada;
    
    public ModalVerActividades(){
        
    }
    
    
    //<editor-fold desc="Getters">
    public TipoAccion getTipoDeAccion() {return tipoDeAccion;}
    public Accion getAccionSeleccionada(){return this.accionSeleccionada;}
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setTipoDeAccion(TipoAccion tipoDeAccion) {this.tipoDeAccion = tipoDeAccion;}
    public void setAccionSeleccionada(Accion accion){this.accionSeleccionada = accion;}
    //</editor-fold>
        
 }
