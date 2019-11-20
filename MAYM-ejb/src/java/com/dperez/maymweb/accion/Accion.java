/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion;

import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.accion.actividad.TipoActividad;
import com.dperez.maymweb.accion.adjunto.Adjunto;
import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
import com.dperez.maymweb.accion.comprobaciones.ResultadoComprobacion;
import com.dperez.maymweb.accion.comprobaciones.TipoComprobacion;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.codificacion.Codificacion;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.estado.EnumEstado;
import com.dperez.maymweb.usuario.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author dperez
 */
@Entity
@Table(name = "Acciones")
public abstract class Accion implements Serializable, Comparable<Accion>{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    protected int Id;
    @Temporal(TemporalType.DATE)
    protected Date FechaDeteccion;
    protected String Descripcion  = new String();
    protected String AnalisisCausa = new String();
    protected EnumEstado EstadoAccion;
    protected TipoAccion TipoAccion;
    
    @OneToMany(mappedBy = "AccionAdjunto",orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    protected List<Adjunto> Adjuntos;
    
    @OneToMany(cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Actividad> Actividades;
    
    @ManyToOne
    protected Deteccion GeneradaPor;
    
    @ManyToOne
    protected Area AreaSectorAccion;
    
    @ManyToOne
    protected Codificacion CodificacionAccion;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    protected Comprobacion ComprobacionEficacia;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    protected Comprobacion ComprobacionImplementacion;
    
    // Constructores
    public Accion(){
        this.EstadoAccion = EnumEstado.PENDIENTE;
        this.Adjuntos = new ArrayList<>();
        this.Actividades = new ArrayList<>();
    }
    
    public Accion(Date FechaDeteccion, String Descripcion, TipoAccion tipoAccion) {
        this.EstadoAccion = EnumEstado.PENDIENTE;
        this.FechaDeteccion = FechaDeteccion;
        this.Descripcion = Descripcion;
        this.Adjuntos = new ArrayList<>();
        this.Actividades = new ArrayList<>();
        this.TipoAccion = tipoAccion;
    }
    
    // Getters
    public int getId() {return this.Id;}
    public Date getFechaDeteccion() {return this.FechaDeteccion;}
    public String getDescripcion() {return this.Descripcion;}
    public String getAnalisisCausa() {return this.AnalisisCausa;}
    public EnumEstado getEstadoAccion() {return EstadoAccion;}
    public TipoAccion getTipoAccion() {return TipoAccion;}
    
    public List<Adjunto> getAdjuntos() {return this.Adjuntos;}
    
    public List<Actividad> getActividades() {return Actividades;}
    
    public Deteccion getGeneradaPor() {return this.GeneradaPor;}
    
    public Area getAreaSectorAccion() {return this.AreaSectorAccion;}
    
    public Codificacion getCodificacionAccion() {return CodificacionAccion;}
    
    public Comprobacion getComprobacionEficacia() {return ComprobacionEficacia;}
    public Comprobacion getComprobacionImplementacion() {return ComprobacionImplementacion;}
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setFechaDeteccion(Date FechaDeteccion) {this.FechaDeteccion = FechaDeteccion;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setAnalisisCausa(String AnalisisCausa) {this.AnalisisCausa = AnalisisCausa;}
    public void setEstadoAccion(EnumEstado EstadoAccion) {this.EstadoAccion = EstadoAccion;}
    public void setTipoAccion(TipoAccion TipoAccion) {this.TipoAccion = TipoAccion;}
    
    public void setComprobacionEficacia(Comprobacion ComprobacionEficacia) {this.ComprobacionEficacia = ComprobacionEficacia;}
    public void setComprobacionImplementacion(Comprobacion ComprobacionImplementacion) {this.ComprobacionImplementacion = ComprobacionImplementacion;}
    
    public void setAdjuntos(List<Adjunto> Adjuntos) {
        this.Adjuntos = Adjuntos;
        for(Adjunto adj: this.Adjuntos){
            adj.setAccionAdjunto(this);
        }
    }
    
    public void setActividades(List<Actividad> Actividades) {
        this.Actividades = Actividades;
        for (Actividad actividad : this.Actividades) {
            actividad.setAccionActividad(this);
        }
    }
    
    public void setGeneradaPor(Deteccion GeneradaPor) {
        if(GeneradaPor==null && this.GeneradaPor !=null){
            this.GeneradaPor.getAccionesDetectadas().remove(this);
            this.GeneradaPor = null;
        }else{
            if(GeneradaPor != null){
                this.GeneradaPor = GeneradaPor;
                if(!GeneradaPor.getAccionesDetectadas().contains(this))
                    GeneradaPor.addAccionDetectada(this);
            }
        }
    }
    
    public void setAreaSectorAccion(Area AreaSectorAccion) {
        if(AreaSectorAccion==null && this.AreaSectorAccion !=null){
            this.AreaSectorAccion.getAccionesEnAreaSector().remove(this);
            this.AreaSectorAccion = null;
        }else{
            if(AreaSectorAccion != null){
                this.AreaSectorAccion = AreaSectorAccion;
                if(!AreaSectorAccion.getAccionesEnAreaSector().contains(this))
                    AreaSectorAccion.addAccionEnAreaSector(this);
            }
        }
    }
    
    public void setCodificacionAccion(Codificacion CodificacionAccion) {
        if(CodificacionAccion == null && this.CodificacionAccion !=null){
            this.CodificacionAccion.getAccionesConCodificacion().remove(this);
            this.CodificacionAccion = null;
        }else{
            if(CodificacionAccion != null){
                this.CodificacionAccion = CodificacionAccion;
                if(!CodificacionAccion.getAccionesConCodificacion().contains(this))
                    CodificacionAccion.addAccionCodificacion(this);
            }
        }
    }
    
    public Comprobacion setComprobacionEficacia(Date FechaEstimada, Usuario ResponsableComprobacion) {
        Comprobacion comprobacion = new Comprobacion(this.Id, FechaEstimada, ResponsableComprobacion, TipoComprobacion.EFICACIA);
        comprobacion.setAccionComprobacion(this);
        ComprobacionEficacia = comprobacion;
        return comprobacion;
    }
    
    public Comprobacion setComprobacionImplementacion(Date FechaEstimada, Usuario ResponsableComprobacion) {
        Comprobacion comprobacion = new Comprobacion(this.Id, FechaEstimada, ResponsableComprobacion, TipoComprobacion.IMPLEMENTACION);
        comprobacion.setAccionComprobacion(this);
        ComprobacionImplementacion = comprobacion;
        return comprobacion;
    }
    
    // Listas
    public void AddAdjunto(Adjunto ArchivoAdjunto){
        this.Adjuntos.add(ArchivoAdjunto);
        if(ArchivoAdjunto.getAccionAdjunto() == null || !ArchivoAdjunto.getAccionAdjunto().equals(this))
            ArchivoAdjunto.setAccionAdjunto(this);
    }
    
    public void RemoveAdjunto(Adjunto ArchivoAdjunto){
        this.Adjuntos.remove(ArchivoAdjunto);
        if(ArchivoAdjunto.getAccionAdjunto()!=null && ArchivoAdjunto.getAccionAdjunto().equals(this))
            ArchivoAdjunto.setAccionAdjunto(null);
    }
    
    public void RemoveAdjunto(int IdArchivoAdjunto){
        Iterator<Adjunto> it = this.Adjuntos.iterator() ;
        while(it.hasNext()){
            Adjunto a = it.next();
            if(a.getId()==IdArchivoAdjunto){
                it.remove();
                if(a.getAccionAdjunto()!=null && a.getAccionAdjunto().equals(this))
                    a.setAccionAdjunto(null);
            }
        }
    }
    
    // Actividades
    public Actividad AddActividad(Date fechaEstimadaImplementacion, String descripcion, Usuario responsableImplementacion, TipoActividad tipoActividad) {
        Actividad actividad = new Actividad(fechaEstimadaImplementacion, descripcion, responsableImplementacion, tipoActividad);
        this.Actividades.add(actividad);
        actividad.setAccionActividad(this);
        responsableImplementacion.AddActividad(actividad);
        return actividad;
    }
    
    public void RemoveActividad(Actividad actividad) {
        this.Actividades.remove(actividad);
        if (actividad.getAccionActividad() != null && actividad.getAccionActividad().equals(this)) {
            actividad.setAccionActividad(null);
        }
    }
    
    public void SetFechaImplementacionActividad(int idActividad, Date fechaImplementacion){
        Actividades.stream()
                .filter(a -> a.getIdActividad() == idActividad)
                .findFirst()
                .get().setFechaEstimadaImplementacion(fechaImplementacion);
    }
    
    /**
     * Comprueba que todas las actividades de la lista esten implementadas.
     * @return
     */
    public boolean EstanImplementadasActividades(){
        return this.Actividades.stream()
                .allMatch(actividad->actividad.EstaImplementada());
    }
    
    /**
     * Comprueba si existe alguna actividad implementada en la lsita.
     * @param actividades lista de actividades a comprobar
     * @return
     */
    protected boolean ExisteAlgunaActividadImplementada(List<Actividad> actividades){
        return actividades.stream()
                .anyMatch(actividad->actividad.EstaImplementada());
    }
    
    public boolean SeComproboImplementacion(){
        return ComprobacionImplementacion != null && ComprobacionImplementacion.getFechaComprobacion() != null;
    }
    
    public boolean SeComproboEficacia(){
        return ComprobacionEficacia != null && ComprobacionEficacia.getFechaComprobacion() != null;
    }
    
    @Override
    public int compareTo(Accion OtraAccion) {
        return this.getFechaDeteccion().compareTo(OtraAccion.getFechaDeteccion());
    }
    /***
     * Chequea las Medidas y cambia el estado de la accion de acuerdo a su implementacion.
     */
    public  void CambiarEstado(){
        if(this.getEstadoAccion()!= EnumEstado.DESESTIMADA && this.getEstadoAccion()!= EnumEstado.CERRADA){
            // si no hay actividades el estado es pendiente.
            if(Actividades.isEmpty()){
                this.setEstadoAccion(EnumEstado.PENDIENTE);
            }else{
                boolean actividadesImp = true;
                int numActividadesImp = 0;
                // chequear implementacion de todas las actividades
                for(Actividad actividad: this.Actividades){
                    if(actividad.getFechaImplementacion()==null) {
                        actividadesImp = false;
                    }else{
                        numActividadesImp ++;
                    }
                }
                if(actividadesImp == true && this.getComprobacionEficacia().getResultado()!= ResultadoComprobacion.NO_COMPROBADA){
                    this.setEstadoAccion(EnumEstado.CERRADA);
                }else{
                    if(actividadesImp == true && this.getComprobacionEficacia().getResultado()== ResultadoComprobacion.NO_COMPROBADA){
                        this.setEstadoAccion(EnumEstado.PROCESO_VER);
                    }else{
                        if(actividadesImp != true && numActividadesImp >= 0){
                            this.setEstadoAccion(EnumEstado.PROCESO_IMP);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Devuelve la actividad especificada.
     * @param IdActividad
     * @return Null si no se encuentra.
     */
    public Actividad GetActividad(int IdActividad) {
        Actividad actividad = Actividades.stream()
                .filter(medida->medida.getIdActividad()== IdActividad)
                .findFirst()
                .orElse(null);
        return actividad;
    }
    
}
