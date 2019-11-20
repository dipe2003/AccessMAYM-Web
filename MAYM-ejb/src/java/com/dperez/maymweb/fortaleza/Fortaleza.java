/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.fortaleza;

import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.deteccion.Deteccion;
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
@Table(name="Fortalezas")
public class Fortaleza implements Serializable, Comparable<Fortaleza> {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    @Temporal(TemporalType.DATE)
    private Date FechaDeteccion;
    private String Descripcion = new String();
    
    @ManyToOne
    private Deteccion GeneradaPor;
    
    @ManyToOne
    private Area AreaSectorFortaleza;
    
    // Constructores
    public Fortaleza(){}
    
    public Fortaleza(Date FechaDeteccion, String Descripcion){
        this.FechaDeteccion = FechaDeteccion;
        this.Descripcion = Descripcion;
    }
    
    // Getters
    public int getId() {return Id;}
    public Date getFechaDeteccion() {return this.FechaDeteccion;}
    public String getDescripcion() {return this.Descripcion;}
    public Deteccion getGeneradaPor() {return this.GeneradaPor;}
    public Area getAreaSectorFortaleza() {return this.AreaSectorFortaleza;}
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setFechaDeteccion(Date FechaDeteccion) {this.FechaDeteccion = FechaDeteccion;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setGeneradaPor(Deteccion GeneradaPor) {
        if(GeneradaPor == null && this.GeneradaPor != null){
            this.GeneradaPor.getFortalezasDetectadas().remove(this);
            this.GeneradaPor = null;
        }else{
            if(GeneradaPor != null){
                this.GeneradaPor = GeneradaPor;
                if(!GeneradaPor.getFortalezasDetectadas().contains(this))
                    GeneradaPor.addFortalezaDetectada(this);
            }
        }
    }
    
    public void setAreaSectorFortaleza(Area AreaSectorFortaleza) {
        if(AreaSectorFortaleza == null && this.AreaSectorFortaleza != null){
            this.AreaSectorFortaleza.getFortalezasEnAreaSector().remove(this);
            this.AreaSectorFortaleza = null;
        }else{
            if(AreaSectorFortaleza != null){
                this.AreaSectorFortaleza = AreaSectorFortaleza;
                if(!AreaSectorFortaleza.getFortalezasEnAreaSector().contains(this))
                    AreaSectorFortaleza.addFortalezaEnAreaSector(this);
            }
        }
    }
    
    @Override
    public int compareTo(Fortaleza OtraFortaleza) {
        return OtraFortaleza.FechaDeteccion.compareTo(this.FechaDeteccion);
    }
    
}
