/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.fortaleza;

import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    private int id;
    @Temporal(TemporalType.DATE)
    private Date fechaDeteccion;
    private String descripcion = new String();
    
    @ManyToOne
    @JoinColumn(name = "deteccionFortaleza_id")
    private Deteccion deteccionFortaleza;
    
    @ManyToOne
    @JoinColumn(name="areaFortaleza_id")
    private Area areaFortaleza;
    
    // Constructores
    public Fortaleza(){}
    
    public Fortaleza(Date fechaDeteccion, String descripcion, Area area, Deteccion deteccion){
        this.fechaDeteccion = fechaDeteccion;
        this.descripcion = descripcion;
        this.areaFortaleza = area;
        this.deteccionFortaleza = deteccion;
    }
    
    // Getters
    public int getId() {return id;}
    public Date getFechaDeteccion() {return this.fechaDeteccion;}
    public String getStrFechaDeteccion(){
        try{
            SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yy");
            return fDate.format(fechaDeteccion);
        }catch(NullPointerException ex){
            ex.getMessage();
        }
        return "";
    }
    public String getDescripcion() {return this.descripcion;}
    public Deteccion getDeteccionFortaleza() {return this.deteccionFortaleza;}
    public Area getAreaFortaleza() {return this.areaFortaleza;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setFechaDeteccion(Date fechaDeteccion) {this.fechaDeteccion = fechaDeteccion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    
    public void setDeteccionFortaleza(Deteccion deteccionFortaleza) {this.deteccionFortaleza = deteccionFortaleza;}    
    public void setAreaFortaleza(Area areaFortaleza) {this.areaFortaleza = areaFortaleza;}
    
    @Override
    public int compareTo(Fortaleza otraFortaleza) {
        return otraFortaleza.fechaDeteccion.compareTo(this.fechaDeteccion);
    }
    
}
