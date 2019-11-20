/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.deteccion;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.fortaleza.Fortaleza;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Detecciones")
public class Deteccion implements Serializable, Comparable<Deteccion> {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String Nombre = new String();
    
    private EnumTipoDeteccion Tipo;
    
    @OneToMany(mappedBy = "GeneradaPor")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Accion> AccionesDetectadas;
    
    @OneToMany(mappedBy= "GeneradaPor")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Fortaleza> FortalezasDetectadas;
    
    // Constructores
    public Deteccion(){
        this.AccionesDetectadas = new ArrayList<>();
        this.FortalezasDetectadas = new ArrayList<>();
    }
    public Deteccion(String NombreDeteccion, EnumTipoDeteccion tipoDeteccion){
        this.Nombre = NombreDeteccion;
        this.Tipo = tipoDeteccion;
        this.AccionesDetectadas = new ArrayList<>();
        this.FortalezasDetectadas = new ArrayList<>();
    }
    
    // Getters
    public int getId() {return this.Id;}
    public String getNombre() {return this.Nombre;}
    
    public EnumTipoDeteccion getTipo() {return this.Tipo;}
    
    public List<Accion> getAccionesDetectadas() {return AccionesDetectadas;}

    public List<Fortaleza> getFortalezasDetectadas() {return FortalezasDetectadas;}    
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    
    public void setTipo(EnumTipoDeteccion Tipo) {this.Tipo = Tipo;}

    public void setAccionesDetectadas(List<Accion> AccionesDetectadas) {
        this.AccionesDetectadas = AccionesDetectadas;
        for(Accion acc:this.AccionesDetectadas){
            acc.setGeneradaPor(this);
        }
    }

    public void setFortalezasDetectadas(List<Fortaleza> FortalezasDetectadas) {
        this.FortalezasDetectadas = FortalezasDetectadas;
        for(Fortaleza fort: this.FortalezasDetectadas){
            fort.setGeneradaPor(this);
        }
    }    
    
    // Listas
    public void addAccionDetectada(Accion AccionDetectada){
        this.AccionesDetectadas.add(AccionDetectada);
        if(AccionDetectada.getGeneradaPor() == null ||!AccionDetectada.getGeneradaPor().equals(this)) 
            AccionDetectada.setGeneradaPor(this);
    }
    
    public void removeAccionDetectada(Accion AccionDetectada){
        this.AccionesDetectadas.remove(AccionDetectada);
        if(AccionDetectada.getGeneradaPor()!=null && AccionDetectada.getGeneradaPor().equals(this))
            AccionDetectada.setGeneradaPor(null);
    }
    
    public void removeAccionDetectada(int IdAccionDetectada){
        Iterator<Accion> it = this.AccionesDetectadas.iterator() ;
        while(it.hasNext()){
            Accion a = it.next();
            if(a.getId()==IdAccionDetectada){
                it.remove();
                if(a.getGeneradaPor()!=null && a.getGeneradaPor().equals(this))
                    a.setGeneradaPor(null);
            }
        }
    }
    
    public void addFortalezaDetectada(Fortaleza FortalezaDetectada){
        this.FortalezasDetectadas.add(FortalezaDetectada);
        if(FortalezaDetectada.getGeneradaPor() == null || !FortalezaDetectada.getGeneradaPor().equals(this))
            FortalezaDetectada.setGeneradaPor(this);
    }
    
    public void removeFortalezaDetectada(Fortaleza FortalezaDetectada){
        this.FortalezasDetectadas.remove(FortalezaDetectada);
        if(FortalezaDetectada.getGeneradaPor()!=null && FortalezaDetectada.getGeneradaPor().equals(this))
            FortalezaDetectada.setGeneradaPor(null);
    }
    
    public void removeFortalezaDetectada(int IdFortalezaDetectada){
        Iterator<Fortaleza> it = this.FortalezasDetectadas.iterator() ;
        while(it.hasNext()){
            Fortaleza f = it.next();
            if(f.getId()==IdFortalezaDetectada){
                it.remove();
                if(f.getGeneradaPor()!=null && f.getGeneradaPor().equals(this))
                    f.setGeneradaPor(null);
            }
        }
    }

    @Override
    public int compareTo(Deteccion OtraDeteccion) {
        return this.Nombre.compareToIgnoreCase(OtraDeteccion.Nombre);
    }
}
