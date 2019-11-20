/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.actividad;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    private int IdActividad;
    @Temporal(TemporalType.DATE)
    private Date FechaEstimadaImplementacion;
    @Temporal(TemporalType.DATE)
    private Date FechaImplementacion;
    private String Descripcion  = new String();
    private TipoActividad TipoActividad;
    
    @OneToOne
    private Accion AccionActividad;
    
    @ManyToOne
    private Usuario ResponsableImplementacion;
    
    // Constructores
    public Actividad(){}
    public Actividad(Date FechaEstimadaDeImplementacion, String Descripcion, Usuario ResponsableImplementacion, TipoActividad TipoActividad){
        this.FechaEstimadaImplementacion = FechaEstimadaDeImplementacion;
        this.Descripcion = Descripcion;
        this.TipoActividad = TipoActividad;
        this.ResponsableImplementacion = ResponsableImplementacion;
    }
    
    // Getters
    public int getIdActividad() {return this.IdActividad;}
    public Date getFechaEstimadaImplementacion() {return this.FechaEstimadaImplementacion;}
    public Date getFechaImplementacion() {return this.FechaImplementacion;}
    public String getDescripcion() {return this.Descripcion;}
    public TipoActividad getTipoActividad() {return TipoActividad;}
    
    public Accion getAccionActividad(){return this.AccionActividad;}
    
    public Usuario getResponsableImplementacion() {return this.ResponsableImplementacion;}
    
    // Setters
    public void setIdActividad(int IdActividad) {this.IdActividad = IdActividad;}
    public void setFechaEstimadaImplementacion(Date FechaEstimadaImplementacion) {this.FechaEstimadaImplementacion = FechaEstimadaImplementacion;}
    public void setFechaImplementacion(Date FechaImplementacion) {this.FechaImplementacion = FechaImplementacion;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setTipoActividad(TipoActividad TipoActividad) {this.TipoActividad = TipoActividad;}
    
    public void setAccionActividad(Accion AccionActividad){
        this.AccionActividad = AccionActividad;
    }
    
    public void setResponsableImplementacion(Usuario ResponsableImplementacion) {
        if(ResponsableImplementacion == null && this.ResponsableImplementacion != null){
            this.ResponsableImplementacion.getActividades().remove(this);
            this.ResponsableImplementacion = null;
        }else{
            if(ResponsableImplementacion != null){
                this.ResponsableImplementacion = ResponsableImplementacion;
                if(!ResponsableImplementacion.getActividades().contains(this))
                    ResponsableImplementacion.AddActividad(this);
            }
        }
    }

    /**
     * Comprueba si la fecha de implentación es null.
     * @return 
     */
    public boolean EstaImplementada(){
        return FechaImplementacion != null;
    }

    @Override
    public int compareTo(Actividad OtraActividad) {
        return this.FechaEstimadaImplementacion.compareTo(OtraActividad.FechaEstimadaImplementacion);
    }
}
