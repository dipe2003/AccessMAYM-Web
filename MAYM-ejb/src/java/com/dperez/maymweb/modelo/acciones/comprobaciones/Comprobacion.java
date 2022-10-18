/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones.comprobaciones;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.usuario.Responsable;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Diego
 */
@Entity
@Table(name="Comprobaciones")
public abstract class Comprobacion implements Serializable{
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date fechaEstimada;
    @Temporal(TemporalType.DATE)
    private Date fechaComprobacion;
    private ResultadoComprobacion resultadoComprobacion;
    private String observaciones = new String();
    private TipoComprobacion tipoDeComprobacion;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Accion accionComprobacion;
    
    @ManyToOne
    @JoinColumn(name ="responsableComprobacion_id")
    private Responsable responsableComprobacion;
    
    //  Constructores
    public Comprobacion(){
        this.resultadoComprobacion = ResultadoComprobacion.NO_COMPROBADA;
    }
    
    public Comprobacion(Accion accion) {
        this.accionComprobacion = accion;
        this.resultadoComprobacion = ResultadoComprobacion.NO_COMPROBADA;
    }
    
    public Comprobacion(Accion accion, Responsable responsable, Date fechaEstimada) {
        this.responsableComprobacion =responsable;
        this.accionComprobacion = accion;
        this.fechaEstimada = fechaEstimada;
        this.resultadoComprobacion = ResultadoComprobacion.NO_COMPROBADA;
    }
    
    //  Getters
    
    public int getId() {return id;}
    public Date getFechaComprobacion() {return fechaComprobacion;}
    public ResultadoComprobacion getResultadoComprobacion() {return resultadoComprobacion;}
    public String getObservaciones() {return observaciones;}
    public Responsable getResponsableComprobacion() {return responsableComprobacion;}
    public Date getFechaEstimada() {return fechaEstimada;}
    public TipoComprobacion getTipoDeComprobacion() {return tipoDeComprobacion;}    
    
    public Accion getAccionComprobacion() {return accionComprobacion;}
    
    //  Setters
    
    public void setId(int id) {this.id = id;}
    public void setFechaComprobacion(Date fechaComprobacion) {this.fechaComprobacion = fechaComprobacion;}
    public void setResultadoComprobacion(ResultadoComprobacion resultadoComprobacion) {this.resultadoComprobacion = resultadoComprobacion;}
    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}
    public void setResponsableComprobacion(Responsable responsableComprobacion) {this.responsableComprobacion = responsableComprobacion;}
    public void setFechaEstimada(Date fechaEstimada) {this.fechaEstimada = fechaEstimada;}
    public void setTipoDeComprobacion(TipoComprobacion tipoDeComprobacion) {this.tipoDeComprobacion = tipoDeComprobacion;}
    
    public void setAccionComprobacion(Accion accionComprobacion) {this.accionComprobacion = accionComprobacion;}
    
}
