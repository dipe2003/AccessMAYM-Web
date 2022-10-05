/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.dperez.maymweb.modelo.usuario;

import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.acciones.comprobaciones.Comprobacion;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author dperez
 * 20/11/2019
 */
@Entity
public class Responsable implements Serializable{
    
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;
    
    @OneToOne
    private Responsabilidad responsabilidadResponsable;
    
    @OneToOne
    private Usuario usuarioResponsable;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comprobacion> comprobacionesResponsable;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Actividad> actividadesResponsable;
    
    public Responsable() {
        this.comprobacionesResponsable = new ArrayList<>();
        this.actividadesResponsable = new ArrayList<>();
    }
    
    public Responsable(Responsabilidad responsabilidad, Usuario usuario){
        this.responsabilidadResponsable = responsabilidad;
        this.usuarioResponsable = usuario;
        this.comprobacionesResponsable = new ArrayList<>();
        this.actividadesResponsable = new ArrayList<>();
    }
    
    /*
    Getters
    */
    
    public int getId() {return id;}
    public Date getFechaBaja(){return this.fechaBaja;}
    
    public Responsabilidad getResponsabilidadResponsable(){return this.responsabilidadResponsable;}
    
    public Usuario getUsuarioResponsable() {return usuarioResponsable;}
    public List<Comprobacion> getComprobacionesResponsable() {return comprobacionesResponsable;}
    public List<Actividad> getActividadesResponsable() {return actividadesResponsable;}
    
    /*
    Setters
    */
    
    public void setId(int id) {this.id = id;}
    public void setFechaBaja(Date fecha){this.fechaBaja = fecha;}
    
    public void setResponsabilidadResponsable(Responsabilidad responsabilidad){this.responsabilidadResponsable = responsabilidad;}
    
    public void setUsuarioResponsable(Usuario usuarioResponsable) {this.usuarioResponsable = usuarioResponsable;}
    public void setComprobacionesResponsable(List<Comprobacion> comprobacionesResponsable) {this.comprobacionesResponsable = comprobacionesResponsable;}
    public void setActividadesResponsable(List<Actividad> actividadesResponsable) {
        this.actividadesResponsable = actividadesResponsable;
    }
    
    /*
    Listas
    */
    
    public void addComprobacion(Comprobacion comprobacion){
        this.comprobacionesResponsable.add(comprobacion);
    }
    
    public void removeComprobacion(Comprobacion comprobacion){
        this.comprobacionesResponsable.forEach((item)->{
            if(Objects.equals(comprobacion.getId(), item.getId())){
                this.comprobacionesResponsable.remove(item);
            }
        });
    }
    
    
    public void addActividad(Actividad actividad){
        this.actividadesResponsable.add(actividad);
    }
    
    public void removeActividad(Actividad actividad){
        this.actividadesResponsable.forEach((item)->{
            if(Objects.equals(actividad.getId(), item.getId())){
                actividadesResponsable.remove(item);
            }
        });
    }
    
    /*
    Otros
    */
    /**
     * Devuelve la actividad indicada por su id.
     * @param idActividad
     * @return Actividad, null si no se encuentra.
     */
    public Actividad getActividad(int idActividad){
        return actividadesResponsable.stream()
                .filter(actividad->actividad.getId() == idActividad)
                .findFirst()
                .orElse(null);
    }
    
    public boolean isVigente(){
        return this.fechaBaja == null;
    }
    
}
