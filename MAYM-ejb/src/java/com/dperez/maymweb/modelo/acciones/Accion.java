/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.acciones;

import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.acciones.actividad.TipoActividad;
import com.dperez.maymweb.modelo.acciones.adjunto.Adjunto;
import com.dperez.maymweb.modelo.acciones.adjunto.TipoAdjunto;
import com.dperez.maymweb.modelo.acciones.comprobaciones.Eficacia;
import com.dperez.maymweb.modelo.acciones.comprobaciones.Implementacion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.producto.Producto;
import com.dperez.maymweb.modelo.usuario.Responsable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author dperez
 */
@Entity
@Table(name = "Acciones")
public abstract class Accion implements Serializable, Comparable<Accion>{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    @Temporal(TemporalType.DATE)
    protected Date fechaDeteccion;
    protected String descripcion  = new String();
    protected String referencias  = new String();
    protected String analisisCausa = new String();
    protected String observacionesDesestimada = new String();
    protected Estado estadoDeAccion;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    protected List<Adjunto> adjuntosDeAccion;
    
    @OneToMany(mappedBy="accionActividad", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Actividad> actividadesDeAccion;
    
    @OneToMany(mappedBy="accionProducto", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Producto> productosInvolucrados;
    
    @ManyToOne
    @JoinColumn(name = "deteccionAccion_id")
    protected Deteccion deteccionAccion;
    
    @ManyToOne
    @JoinColumn(name="areaAccion_id")
    protected Area areaAccion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="codificacionAccion_id")
    protected Codificacion codificacionAccion;
    
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="comprobacionImplementacion_id")
    private Implementacion comprobacionImplementacion;
    
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="comprobacionEficacia_id")
    private Eficacia comprobacionEficacia;
    
    // Constructores
    public Accion(){
        this.estadoDeAccion = Estado.PENDIENTE;
        this.adjuntosDeAccion = new ArrayList<>();
        this.actividadesDeAccion = new ArrayList<>();
        this.productosInvolucrados = new ArrayList<>();
    }
    
    public Accion(Date fechaDeteccion, String descripcion, String referencias) {
        this.estadoDeAccion = Estado.PENDIENTE;
        this.fechaDeteccion = fechaDeteccion;
        this.descripcion = descripcion;
        this.referencias = referencias;
        this.adjuntosDeAccion = new ArrayList<>();
        this.actividadesDeAccion = new ArrayList<>();
        this.productosInvolucrados = new ArrayList<>();
    }
    
    public Accion(Date fechaDeteccion, String descripcion, String referencias, Area area, Deteccion deteccion, Codificacion codificacion) {
        this.estadoDeAccion = Estado.PENDIENTE;
        this.fechaDeteccion = fechaDeteccion;
        this.descripcion = descripcion;
        this.adjuntosDeAccion = new ArrayList<>();
        this.actividadesDeAccion = new ArrayList<>();
        this.productosInvolucrados = new ArrayList<>();
        this.areaAccion = area;
        this.deteccionAccion = deteccion;
        this.codificacionAccion = codificacion;
    }
    
    // Getters
    public int getId() {return this.id;}
    public Date getFechaDeteccion() {return this.fechaDeteccion;}
    public String getDescripcion() {return this.descripcion;}
    public String getReferencias(){return this.referencias;}
    public String getAnalisisCausa() {return this.analisisCausa;}
    public String getObservacionesDesestimada() {return observacionesDesestimada;}
    
    public Estado getEstadoDeAccion() {return estadoDeAccion;}
    
    public List<Adjunto> getAdjuntosDeAccion() {return this.adjuntosDeAccion;}
    
    public List<Actividad> getActividadesDeAccion() {return actividadesDeAccion;}
    
    public Deteccion getDeteccionAccion() {return this.deteccionAccion;}
    
    public Area getAreaAccion() {return this.areaAccion;}
    
    public Codificacion getCodificacionAccion() {return codificacionAccion;}
    
    public Implementacion getComprobacionImplementacion() {return comprobacionImplementacion;}
    
    public Eficacia getComprobacionEficacia() {return comprobacionEficacia;}
    
    public List<Producto> getProductosInvolucrados() {return productosInvolucrados;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setFechaDeteccion(Date fechaDeteccion) {this.fechaDeteccion = fechaDeteccion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setReferencias(String referencias){this.referencias = referencias;}
    public void setAnalisisCausa(String analisisCausa) {this.analisisCausa = analisisCausa;}
    public void setObservacionesDesestimada(String observacionesDesestimada) {this.observacionesDesestimada = observacionesDesestimada;}
    
    public void setEstadoDeAccion(Estado estadoDeAccion) {this.estadoDeAccion = estadoDeAccion;}
    
    public void setComprobacionImplementacion(Implementacion comprobacionImplementacion) {this.comprobacionImplementacion = comprobacionImplementacion;}
    
    public void setComprobacionEficacia(Eficacia comprobacionEficacia) {this.comprobacionEficacia = comprobacionEficacia;}
    
    public void setAdjuntosDeAccion(List<Adjunto> adjuntosDeAccion) {this.adjuntosDeAccion = adjuntosDeAccion;}
    
    public void setActividadesDeAccion(List<Actividad> actividadesDeAccion) {this.actividadesDeAccion = actividadesDeAccion;}
    
    public void setDeteccionAccion(Deteccion deteccionAccion) {this.deteccionAccion = deteccionAccion;}
    
    public void setAreaAccion(Area areaAccion) {this.areaAccion = areaAccion;}
    
    public void setCodificacionAccion(Codificacion codificacionAccion) {this.codificacionAccion = codificacionAccion;}
    
    public void setProductosInvolucrados(List<Producto> productosInvolucrados) {this.productosInvolucrados = productosInvolucrados;}
    
    /*
    Comprobaciones
    */
    
    /*
    * 	Comprobaciones
    */
    
    public Eficacia crearComprobacionEficacia(Date fechaEstimada, Responsable responsable) {
        Eficacia comprobacion = new Eficacia(this, responsable, fechaEstimada);
        this.comprobacionEficacia = comprobacion;
        return comprobacion;
    }
    
    public Implementacion crearComprobacionImplementacion(Date fechaEstimada, Responsable responsable) {
        Implementacion comprobacion = new Implementacion(this, responsable, fechaEstimada);
        this.comprobacionImplementacion = comprobacion;
        return comprobacion;
    }
    
    
    public boolean estaComprobadaImplementacion(){
        return comprobacionImplementacion != null && comprobacionImplementacion.getFechaComprobacion() != null;
    }
    
    public boolean estaComprobadaEficacia(){
        return comprobacionEficacia != null && comprobacionEficacia.getFechaComprobacion() != null;
    }
    
    // Listas
    /*
    * 	Adjuntos
    */
    
    public Adjunto crearAdjunto(String titulo, String ubicacion, TipoAdjunto tipo) {
        Adjunto adjunto = new Adjunto(titulo, ubicacion, tipo, this);
        this.adjuntosDeAccion.add(adjunto);
        return adjunto;
    }
    
    public void addAdjunto(Adjunto ArchivoAdjunto){
        this.adjuntosDeAccion.add(ArchivoAdjunto);
    }
    
    public Adjunto findAdjunto(int id){
        return this.adjuntosDeAccion.stream()
                .filter((Adjunto a)->a.getIda() == id)
                .findFirst()
                .orElse(null);
    }
    
    public Adjunto findAdjuntoReciente() {
        return this.adjuntosDeAccion.stream()
                .max(Comparator.comparing(Adjunto::getIda)).orElse(null);
    }
    
    public Adjunto removeAdjunto(int id){
        Adjunto adjunto = findAdjunto(id);
        adjunto.setAccionAdjunto(null);
        this.adjuntosDeAccion.remove(adjunto);
        return adjunto;
    }
    
    // Actividades
    public Actividad crearActividad(Date fechaEstimadaImplementacion, String descripcion, Responsable responsableImplementacion,
            TipoActividad tipoActividad) {
        Actividad actividad = new Actividad(fechaEstimadaImplementacion, descripcion, responsableImplementacion, tipoActividad, this);
        this.actividadesDeAccion.add(actividad);
        return actividad;
    }
    
    public void removeActividad(Actividad actividad) {
        this.actividadesDeAccion.forEach((Actividad item)->{
            if(actividad.getId()== item.getId()){
                this.actividadesDeAccion.remove(item);
            }
        });
    }
    
    public void setFechaImplementacionActividad(int idActividad, Date fechaImplementacion){
        actividadesDeAccion.stream()
                .filter(a -> a.getId() == idActividad)
                .findFirst()
                .get().setFechaImplementacion(fechaImplementacion);
    }
    
    public Actividad findActividad(int id) {
        return this.actividadesDeAccion.stream()
                .filter((Actividad a)->a.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean existeActividad(int id) {
        return this.actividadesDeAccion.stream()
                .anyMatch((Actividad a)-> a.getId()==id);
    }
    
    @Override
    public int compareTo(Accion OtraAccion) {
        return this.getFechaDeteccion().compareTo(OtraAccion.getFechaDeteccion());
    }
    /***
     * Chequea las Medidas y cambia el estado de la accion de acuerdo a su implementacion.
     */
    public void cambiarEstado(){
        if(this.estadoDeAccion != Estado.DESESTIMADA){
            if(this.actividadesDeAccion.isEmpty()){
                this.estadoDeAccion = Estado.PENDIENTE;
            }else{
                if(this.estaComprobadaEficacia()){
                    this.estadoDeAccion = Estado.CERRADA;
                }else{
                    if(this.estaComprobadaImplementacion()){
                        this.estadoDeAccion = Estado.PROCESO_VER;
                    }else{
                        if(estaImplementadaAlgunaActividad()){
                            this.estadoDeAccion = Estado.PROCESO_IMP;
                        }
                    }
                }
            }
        }
    }
    
    public boolean estaImplementadaAlgunaActividad() {
        return this.actividadesDeAccion.stream()
                .anyMatch(a->a.estaImplementada());
    }
    
    public boolean estanImplementadasTodasActividades() {
        return this.actividadesDeAccion.stream()
                .allMatch((Actividad actividad)->actividad.estaImplementada());
    }
    
    
    /*
    * 	Productos
    */
    
    public Producto crearProducto(String nombre, String datos) {
        Producto producto = new Producto(nombre, datos, this);
        this.productosInvolucrados.add(producto);
        return producto;
    }
    
    public void addProducto(Producto producto) {
        this.productosInvolucrados.add(producto);
    }
    
    public Producto findProducto(int id) {
        return this.productosInvolucrados.stream()
                .filter((Producto p)-> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public Producto findProductoReciente() {
        return this.productosInvolucrados.stream()
                .max(Comparator.comparing(Producto::getId)).orElse(null);
    }
    
    public Producto removeProducto(int id){
        Producto producto = findProducto(id);
        producto.setAccionProducto(null);
        this.productosInvolucrados.remove(producto);
        return producto;
    }
}
