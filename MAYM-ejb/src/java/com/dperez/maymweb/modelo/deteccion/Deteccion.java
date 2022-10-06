/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.deteccion;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Detecciones")
public class Deteccion implements Serializable, Comparable<Deteccion> {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre = new String();
    
    private TipoDeteccion tipoDeDeteccion;
    
    @OneToMany(mappedBy="deteccionAccion", cascade = CascadeType.ALL)
    private List<Accion> accionesDeteccion;
    
    @OneToMany(mappedBy="deteccionFortaleza", cascade = CascadeType.ALL)
    private List<Fortaleza> fortalezasDeteccion;
    
    // Constructores
    public Deteccion(){
        this.accionesDeteccion = new ArrayList<>();
        this.fortalezasDeteccion = new ArrayList<>();
    }
    public Deteccion(String nombreDeteccion, TipoDeteccion tipoDeteccion){
        this.nombre = nombreDeteccion;
        this.tipoDeDeteccion = tipoDeteccion;
        this.accionesDeteccion = new ArrayList<>();
        this.fortalezasDeteccion = new ArrayList<>();
    }
    
    // Getters
    public int getId() {return this.id;}
    public String getNombre() {return this.nombre;}
    
    public TipoDeteccion getTipoDeDeteccion() {return this.tipoDeDeteccion;}
    
    public List<Accion> getAccionesDeteccion() {return accionesDeteccion;}

    public List<Fortaleza> getFortalezasDeteccion() {return fortalezasDeteccion;}    
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    
    public void setTipoDeDeteccion(TipoDeteccion tipoDeDeteccion) {this.tipoDeDeteccion = tipoDeDeteccion;}

    public void setAccionesDeteccion(List<Accion> accionesDeteccion) {
        this.accionesDeteccion = accionesDeteccion;
    }

    public void setFortalezasDeteccion(List<Fortaleza> fortalezasDeteccion) {
        this.fortalezasDeteccion = fortalezasDeteccion;
    }    
    
    // Listas
    public void addAccion(Accion accion){
        this.accionesDeteccion.add(accion);
    }
    
    public void removeAccion(Accion accion){
        this.accionesDeteccion.forEach((Accion item)->{
            if(Objects.equals(accion.getId(), item.getId())){
                this.accionesDeteccion.remove(item);
            }
        });
    }
    
    public void addFortaleza(Fortaleza fortaleza){
        this.fortalezasDeteccion.add(fortaleza);
    }
    
    public void removeFortaleza(Fortaleza fortaleza){
        this.fortalezasDeteccion.forEach((Fortaleza item)->{
            if(Objects.equals(fortaleza.getId(), item.getId())){
                fortalezasDeteccion.remove(item);
            }
        });
    }
    
    @Override
    public int compareTo(Deteccion otraDeteccion) {
        return this.nombre.compareToIgnoreCase(otraDeteccion.nombre);
    }
}
