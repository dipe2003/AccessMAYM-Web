/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.codificacion;

import com.dperez.maymweb.acciones.Accion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Codificacion")
public class Codificacion implements Serializable, Comparable<Codificacion>{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String Nombre = new String();
    private String Descripcion = new String();
    
    @OneToMany(mappedBy = "CodificacionAccion")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Accion> AccionesConCodificacion;
    
    // Constructores
    public Codificacion(){
        this.AccionesConCodificacion = new ArrayList<>();
    }
    public Codificacion(String NombreCodificacion, String DescripcionCodificacion){
        this.Nombre = NombreCodificacion;
        this.Descripcion = DescripcionCodificacion;
        this.AccionesConCodificacion = new ArrayList<>();
    }
    
    // Getters
    public int getId() {return this.Id;}
    public String getNombre() {return this.Nombre;}
    public String getDescripcion(){return this.Descripcion;}    
    public List<Accion> getAccionesConCodificacion() {return this.AccionesConCodificacion;}
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    public void setDescripcion(String Descripcion){this.Descripcion = Descripcion;}    
    public void setAccionesConCodificacion(List<Accion> AccionesConCodificacion) {
        this.AccionesConCodificacion = AccionesConCodificacion;
        for(Accion acc:this.AccionesConCodificacion){
            acc.setCodificacionAccion(this);
        }
    }
    
    // Listas
    /*
    Accion
    */
    public void addAccionCodificacion(Accion AccionCodificacion){
        this.AccionesConCodificacion.add(AccionCodificacion);
        if(AccionCodificacion.getCodificacionAccion() == null || !AccionCodificacion.getCodificacionAccion().equals(this))
            AccionCodificacion.setCodificacionAccion(this);        
    }
    
    public void removeAccionCodificacion(Accion AccionCodificacion){
        this.AccionesConCodificacion.remove(AccionCodificacion);
        if(AccionCodificacion.getCodificacionAccion()!=null && AccionCodificacion.getCodificacionAccion().equals(this))
            AccionCodificacion.setCodificacionAccion(null);
    }
    
    public void removeAccionCodificacion(int IdAccionCodificacion){
        Iterator<Accion> it = this.AccionesConCodificacion.iterator() ;
        while(it.hasNext()){
            Accion a = it.next();
            if(a.getId()==IdAccionCodificacion){
                it.remove();
                if(a.getCodificacionAccion()!=null && a.getCodificacionAccion().equals(this))
                    a.setCodificacionAccion(null);
            }
        }        
    }
    
    @Override
    public int compareTo(Codificacion OtraCodificacion) {
       return this.Nombre.compareToIgnoreCase(OtraCodificacion.Nombre);
    }
    
}
