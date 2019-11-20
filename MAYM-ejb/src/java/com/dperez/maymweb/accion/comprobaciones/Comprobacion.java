/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.comprobaciones;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Comprobacion implements Serializable{
    @Id
    private int Id;
    @Temporal(TemporalType.DATE)
    private Date FechaEstimada;
    @Temporal(TemporalType.DATE)
    private Date FechaComprobacion;
    private ResultadoComprobacion Resultado;
    private String Observaciones = new String();
    private TipoComprobacion TipoComprobacion;
    
    @OneToOne
    private Accion AccionComprobacion;
    
    @ManyToOne
    private Usuario Responsable;
    
    //  Constructores
    public Comprobacion(){
        this.Resultado = ResultadoComprobacion.NO_COMPROBADA;
    }
    public Comprobacion(int Id, Date FechaEstimada, Usuario ResponsableComprobacion, TipoComprobacion tipoComprobacion){
        this.Resultado = ResultadoComprobacion.NO_COMPROBADA;
        this.Responsable = ResponsableComprobacion;
        this.FechaEstimada = FechaEstimada;
        this.Id = Id;
        this.TipoComprobacion = tipoComprobacion;
    }
    
    //  Getters
    
    public int getId() {return Id;}
    public Date getFechaComprobacion() {return FechaComprobacion;}
    public ResultadoComprobacion getResultado() {return Resultado;}
    public String getObservaciones() {return Observaciones;}
    public Usuario getResponsable() {return Responsable;}
    public Date getFechaEstimada() {return FechaEstimada;}
    public TipoComprobacion getTipoComprobacion() {return TipoComprobacion;}    
    
    public Accion getAccionComprobacion() {return AccionComprobacion;}
    
    //  Setters
    
    public void setId(int Id) {this.Id = Id;}
    public void setFechaComprobacion(Date FechaComprobacion) {this.FechaComprobacion = FechaComprobacion;}
    public void setResultado(ResultadoComprobacion Resultado) {this.Resultado = Resultado;}
    public void setObservaciones(String Observaciones) {this.Observaciones = Observaciones;}
    public void setResponsable(Usuario Responsable) {this.Responsable = Responsable;}
    public void setFechaEstimada(Date FechaEstimada) {this.FechaEstimada = FechaEstimada;}
    public void setTipoComprobacion(TipoComprobacion TipoComprobacion) {this.TipoComprobacion = TipoComprobacion;}
    
    public void setAccionComprobacion(Accion AccionComprobacion) {
        this.AccionComprobacion = AccionComprobacion;        
    }
       
}
