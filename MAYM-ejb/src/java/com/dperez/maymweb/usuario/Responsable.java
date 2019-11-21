/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.dperez.maymweb.usuario;

import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**
 *
 * @author dperez
 * 20/11/2019
 */
@Entity
public class Responsable implements Serializable, Comparable<Responsable>{
    
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String NombreResponsable;
    
    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Usuario UsuarioResponsable;
    
    @OneToMany(mappedBy = "Responsable")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comprobacion> Comprobaciones;
    
    @OneToMany(mappedBy = "ResponsableImplementacion")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Actividad> Actividades;
    
    public Responsable() {
        this.Comprobaciones = new ArrayList<>();
        this.Actividades = new ArrayList<>();
    }
    
    /*
    Getters
    */
    
    public int getId() {return Id;}
    public String getNombreResponsable() {return NombreResponsable;}
    
    public Usuario getUsuarioResponsable() {return UsuarioResponsable;}
    
    public List<Comprobacion> getComprobaciones() {return Comprobaciones;}
    
    public List<Actividad> getActividades() {return Actividades;}
    
    /*
    Setters
    */
    
    public void setId(int Id) {this.Id = Id;}
    public void setNombreResponsable(String NombreResponsable) {this.NombreResponsable = NombreResponsable;}
    
    public void setUsuarioResponsable(Usuario UsuarioResponsable) {this.UsuarioResponsable = UsuarioResponsable;}    
    
    public void setComprobaciones(List<Comprobacion> Comprobaciones) {this.Comprobaciones = Comprobaciones;}
    
    public void setActividades(List<Actividad> Actividades) {
        this.Actividades = Actividades;
        for(Actividad med: this.Actividades){
            med.setResponsableImplementacion(this);
        }
    }
    
    /*
    Listas
    */    
    
    public void AddComprobacion(Comprobacion comprobacion){
        if(comprobacion != null){
            this.Comprobaciones.add(comprobacion);
            if(comprobacion.getResponsable() != null && !comprobacion.getResponsable().equals(this))
                comprobacion.setResponsable(this);
        }
    }
    
    public void RemoveComprobacion(Comprobacion comprobacion){
        if(comprobacion != null){
            this.Comprobaciones.remove(comprobacion);
            if(comprobacion.getResponsable() != null && comprobacion.getResponsable().equals(this))
                comprobacion.setResponsable(null);
        }
    }
    
    public void RemoveComprobacion(int IdComprobacion){
        Iterator<Comprobacion> it = this.Comprobaciones.iterator();
        while(it.hasNext()){
            Comprobacion c = it.next();
            if(c.getId() == IdComprobacion){
                it.remove();
                if(c.getResponsable() != null && c.getResponsable().equals(this))
                    c.setResponsable(null);
            }
        }
    }
    
    public void AddActividad(Actividad actividad){
        if(actividad != null){
            this.Actividades.add(actividad);
            if(actividad.getResponsableImplementacion() != null && !actividad.getResponsableImplementacion().equals(this))
                actividad.setResponsableImplementacion(this);
        }
    }
    
    public void RemoveActividad(Actividad actividad){
        if(actividad != null){
            this.Actividades.remove(actividad);
            if(actividad.getResponsableImplementacion() != null &&
                    actividad.getResponsableImplementacion().equals(this))
                actividad.setResponsableImplementacion(null);
        }
    }
    
    public void RemoveActividad(int actividad){
        Iterator<Actividad> it = this.Actividades.iterator() ;
        while(it.hasNext()){
            Actividad m = it.next();
            if(m.getIdActividad()== actividad){
                it.remove();
                if(m.getResponsableImplementacion()!=null && m.getResponsableImplementacion().equals(this))
                    m.setResponsableImplementacion(null);
            }
        }
    }
    
    /*
        Otros
    */
    /**
     * Devuelve la actividad indicada por su id.
     * @param IdActividad
     * @return Actividad, null si no se encuentra.
     */
    public Actividad GetActividad(int IdActividad){
        return Actividades.stream()
                .filter(actividad->actividad.getIdActividad() == IdActividad)
                .findFirst()
                .orElse(null);
    }
    
     @Override
    public int compareTo(Responsable otro) {
        return this.NombreResponsable.compareTo(otro.NombreResponsable);
    }
}
