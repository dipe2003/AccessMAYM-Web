/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.general;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.comprobaciones.Comprobacion;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import static com.dperez.maymweb.modelo.acciones.actividad.TipoActividad.CORRECTIVA;
import static com.dperez.maymweb.modelo.acciones.actividad.TipoActividad.PREVENTIVA;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.producto.Producto;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class VerAccion implements Serializable {
 
    private FacadeLectura fLectura;
    
    private TipoAccion tipoDeAccion;
    
    private Date FechaDeteccion;
    private String strFechaDeteccion;
    
    private Deteccion GeneradaPor;
    private Area AreaSector;
    private String Descripcion;
    private String AnalisisCausa;
    
    private List<Producto> ListaProductos;

    private List<Actividad> actividades;
    
    private Date FechaImplementacion;
    
    private String ObservacionesComprobacion;
    private Date FechaComprobacion;
    
    private Comprobacion ComprobacionImplementacion;
    private Comprobacion ComprobacionEficacia;
    
    private Accion AccionSeleccionada;
    
    private Estado Estado;
    
    //<editor-fold desc="Getters">
    public Date getFechaDeteccion() {return FechaDeteccion;}
    public String getStrFechaDeteccion(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaDeteccion == null) {
            return this.strFechaDeteccion;
        }else{
            return fDate.format(FechaDeteccion);
        }
    }
    public Deteccion getGeneradaPor() {return GeneradaPor;}
    public Area getAreaSector() {return AreaSector;}
    public String getDescripcion() {return Descripcion;}
    public String getAnalisisCausa() {return AnalisisCausa;}
    
    public List<Producto> getListaProductos() {return ListaProductos;}
    
    public List<Actividad> getActividades(){return this.actividades;}
    public Date getFechaImplementacion(){return this.FechaImplementacion;}
    public String getObservacionesComprobacion() {return ObservacionesComprobacion;}
    public Date getFechaComprobacion() {return FechaComprobacion;}
    public Accion getAccionSeleccionada() {return AccionSeleccionada;}
    public Estado getEstado() {return Estado;}
    public Comprobacion getComprobacionImplementacion() {return ComprobacionImplementacion;}
    public Comprobacion getComprobacionEficacia() {return ComprobacionEficacia;}
    public TipoAccion getTipoDeAccion(){return this.tipoDeAccion;}
    
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setFechaDeteccion(Date FechaDeteccion) {this.FechaDeteccion = FechaDeteccion;}
    public void setStrFechaDeteccion(String strFechaDeteccion) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaDeteccion));
        }catch(ParseException ex){}
        this.strFechaDeteccion = strFechaDeteccion;
        this.FechaDeteccion = cal.getTime();
    }
    public void setGeneradaPor(Deteccion GeneradaPor) {this.GeneradaPor = GeneradaPor;}
    public void setAreaSector(Area AreaSector) {this.AreaSector = AreaSector;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setAnalisisCausa(String AnalisisCausa) {this.AnalisisCausa = AnalisisCausa;}
    
    public void setListaProductos(List<Producto> ListaProductos) {this.ListaProductos = ListaProductos;}   

    public void setActividades(List<Actividad> actividades){this.actividades = actividades;}
    public void setFechaImplementacion(Date FechaImplementacion){this.FechaImplementacion = FechaImplementacion;}
    public void setObservacionesComprobacion(String ObservacionesComprobacion) {this.ObservacionesComprobacion = ObservacionesComprobacion;}
    public void setFechaComprobacion(Date FechaComprobacion) {this.FechaComprobacion = FechaComprobacion;}
    public void setAccionSeleccionada(Accion AccionSeleccionada) {this.AccionSeleccionada = AccionSeleccionada;}
    public void setEstado(Estado Estado) {this.Estado = Estado;}
    public void setComprobacionImplementacion(Comprobacion ComprobacionImplementacion) {this.ComprobacionImplementacion = ComprobacionImplementacion;}
    public void setComprobacionEficacia(Comprobacion ComprobacionEficacia) {this.ComprobacionEficacia = ComprobacionEficacia;}
    public void setTipoDeAccion(TipoAccion tipo){this.tipoDeAccion = tipo;}
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    //  Inicializcion
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion y el resto de las propiedades.
        
        int IdAccion = 0;
        try{
            IdAccion = Integer.parseInt(request.getParameter("id"));
        }catch(Exception nEx){}
        if(IdAccion != 0){
            AccionSeleccionada = fLectura.GetAccion(IdAccion);
            tipoDeAccion = TipoAccion.valueOf(AccionSeleccionada.getClass().getSimpleName().toUpperCase());           
            FechaDeteccion = AccionSeleccionada.getFechaDeteccion();
            GeneradaPor = AccionSeleccionada.getDeteccionAccion();
            AreaSector = AccionSeleccionada.getAreaAccion();
            Descripcion = AccionSeleccionada.getDescripcion();
            AnalisisCausa = AccionSeleccionada.getAnalisisCausa();
            Estado = AccionSeleccionada.getEstadoDeAccion();
            ComprobacionImplementacion = AccionSeleccionada.getComprobacionImplementacion();
            ComprobacionEficacia = AccionSeleccionada.getComprobacionEficacia();            
            
            actividades = AccionSeleccionada.getActividadesDeAccion().stream()                    
                    .collect(Collectors.toList());
            if(!(AccionSeleccionada.getProductosInvolucrados().isEmpty())){
                ListaProductos = AccionSeleccionada.getProductosInvolucrados();
            }

        }
    }
    //</editor-fold>
}
