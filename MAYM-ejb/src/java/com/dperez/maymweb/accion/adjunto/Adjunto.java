/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.adjunto;

import com.dperez.maymweb.accion.Accion;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private int Id;
    private String Titulo = new String();
    private String Ubicacion = new String();
    private EnumTipoAdjunto TipoAdjunto;
    
    @ManyToOne
    private Accion AccionAdjunto;
    
    // Constructores
    public Adjunto(){}
    public Adjunto(String TituloAdjunto, String UbicacionArchivo, EnumTipoAdjunto TipoAdjunto){
        this.Titulo = TituloAdjunto;
        this.Ubicacion = UbicacionArchivo;
        this.TipoAdjunto = TipoAdjunto;
    }
    
    // Getters
    public int getId() {return this.Id;}
    public String getTitulo() {return this.Titulo;}
    public String getUbicacion() {return this.Ubicacion;}
    public Accion getAccionAdjunto() {return AccionAdjunto;}
    public EnumTipoAdjunto getTipoAdjunto(){return this.TipoAdjunto;}
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setTitulo(String Titulo) {this.Titulo = Titulo;}
    public void setUbicacion(String Ubicacion) {this.Ubicacion = Ubicacion;}
    public void setAccionAdjunto(Accion AccionAdjunto) {
        if(AccionAdjunto == null && this.AccionAdjunto != null){
            this.AccionAdjunto.getAdjuntos().remove(this);
            this.AccionAdjunto = null;
        }else{
            if(AccionAdjunto != null){
                this.AccionAdjunto = AccionAdjunto;
                if(!AccionAdjunto.getAdjuntos().contains(this))
                    this.AccionAdjunto.AddAdjunto(this);
            }
        }
    }
    public void setTipoAdjunto(EnumTipoAdjunto TipoAdjunto){this.TipoAdjunto = TipoAdjunto;}
}
