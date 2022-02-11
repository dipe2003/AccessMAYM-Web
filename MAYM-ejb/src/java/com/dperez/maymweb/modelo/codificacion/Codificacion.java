/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.codificacion;

import com.dperez.maymweb.modelo.acciones.Accion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Codificacion")
public class Codificacion implements Serializable, Comparable<Codificacion>{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre = new String();
    private String descripcion = new String();
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Accion> accionesCodificacion;
    
    // Constructores
    public Codificacion(){
        this.accionesCodificacion = new ArrayList<>();
    }
    public Codificacion(String nombreCodificacion, String descripcionCodificacion){
        this.nombre = nombreCodificacion;
        this.descripcion = descripcionCodificacion;
        this.accionesCodificacion = new ArrayList<>();
    }
    
    // Getters
    public int getId() {return this.id;}
    public String getNombre() {return this.nombre;}
    public String getDescripcion(){return this.descripcion;}    
    public List<Accion> getAccionesCodificacion() {return this.accionesCodificacion;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setDescripcion(String descripcion){this.descripcion = descripcion;}    
    public void setAccionesCodificacion(List<Accion> accionesCodificacion) {this.accionesCodificacion = accionesCodificacion;}
    
    // Listas
    /*
    Accion
    */
    public void addAccion(Accion accin){
        this.accionesCodificacion.add(accin);    
    }
    
    public void removeAccion(Accion accion){
        this.accionesCodificacion.forEach((Accion item)->{
            if(Objects.equals(accion.getId(), item.getId())){
                this.accionesCodificacion.remove(item);
            }
        });
    }
        
    @Override
    public int compareTo(Codificacion otraCodificacion) {
       return this.nombre.compareToIgnoreCase(otraCodificacion.nombre);
    }
    
}
