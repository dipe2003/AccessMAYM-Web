/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones.adjunto;

import com.dperez.maymweb.modelo.acciones.Accion;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Adjuntos")
public class Adjunto implements Serializable{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Ida;
    private String titulo = new String();
    private String ubicacion = new String();
    private TipoAdjunto tipoDeAdjunto;
    
    @ManyToOne
    @JoinColumn(name ="accionAdjunto_id")
    private Accion accionAdjunto;
    
    // Constructores
    public Adjunto(){}
    public Adjunto(String TituloAdjunto, String UbicacionArchivo, TipoAdjunto TipoAdjunto, Accion accion){
        this.titulo = TituloAdjunto;
        this.ubicacion = UbicacionArchivo;
        this.tipoDeAdjunto = TipoAdjunto;
        this.accionAdjunto = accion;
    }
    
    // Getters
    public int getIda() {return this.Ida;}
    public String getTitulo() {return this.titulo;}
    public String getUbicacion() {return this.ubicacion;}
    public Accion getAccionAdjunto() {return accionAdjunto;}
    public TipoAdjunto getTipoDeAdjunto(){return this.tipoDeAdjunto;}
    
    // Setters
    public void setIda(int Ida) {this.Ida = Ida;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public void setUbicacion(String ubicacion) {this.ubicacion = ubicacion;}
    public void setAccionAdjunto(Accion accionAdjunto) {this.accionAdjunto = accionAdjunto;}
    public void setTipoDeAdjunto(TipoAdjunto tipoDeAdjunto){this.tipoDeAdjunto = tipoDeAdjunto;}
}
