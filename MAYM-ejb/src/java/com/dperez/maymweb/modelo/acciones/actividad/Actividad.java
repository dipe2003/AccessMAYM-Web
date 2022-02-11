/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones.actividad;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.usuario.Responsable;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Actividades")
public class Actividad implements Serializable, Comparable<Actividad> {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date fechaEstimadaImplementacion;
    @Temporal(TemporalType.DATE)
    private Date fechaImplementacion;
    private String descripcion  = new String();
    private TipoActividad tipoDeActividad;
    
    @ManyToOne
    private Accion accionActividad;
    
    @ManyToOne
    private Responsable responsableImplementacion;
    
    // Constructores
    public Actividad(){}
    public Actividad(Date fechaEstimadaDeImplementacion, String descripcion, Responsable responsableImplementacion, TipoActividad tipoActividad,
            Accion accion){
        this.fechaEstimadaImplementacion = fechaEstimadaDeImplementacion;
        this.descripcion = descripcion;
        this.tipoDeActividad = tipoActividad;
        this.responsableImplementacion = responsableImplementacion;
        this.accionActividad = accion;
    }
    
    // Getters
    public int getId() {return this.id;}
    public Date getFechaEstimadaImplementacion() {return this.fechaEstimadaImplementacion;}
    public Date getFechaImplementacion() {return this.fechaImplementacion;}
    public String getDescripcion() {return this.descripcion;}
    public TipoActividad getTipoDeActividad() {return tipoDeActividad;}
    
    public Accion getAccionActividad(){return this.accionActividad;}
    public Responsable getResponsableImplementacion() {return this.responsableImplementacion;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setFechaEstimadaImplementacion(Date fechaEstimadaImplementacion) {this.fechaEstimadaImplementacion = fechaEstimadaImplementacion;}
    public void setFechaImplementacion(Date fechaImplementacion) {this.fechaImplementacion = fechaImplementacion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setTipoDeActividad(TipoActividad tipoDeActividad) {this.tipoDeActividad = tipoDeActividad;}
    
    public void setAccionActividad(Accion accionActividad){this.accionActividad = accionActividad;}
    
    public void setResponsableImplementacion(Responsable responsableImplementacion) {
        this.responsableImplementacion = responsableImplementacion;
    }
    
    /**
     * Comprueba si la fecha de implentación es null.
     * @return
     */
    public boolean estaImplementada (){
        return fechaImplementacion != null;
    }
    
    @Override
    public int compareTo(Actividad OtraActividad) {
        return this.fechaEstimadaImplementacion.compareTo(OtraActividad.fechaEstimadaImplementacion);
    }
}
