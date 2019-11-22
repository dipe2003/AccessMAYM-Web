/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.correctivas;

import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
import com.dperez.maymweb.acciones.Correctiva;
import com.dperez.maymweb.accion.actividad.Actividad;
import static com.dperez.maymweb.accion.actividad.TipoActividad.CORRECTIVA;
import static com.dperez.maymweb.accion.actividad.TipoActividad.PREVENTIVA;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.acciones.EnumEstado;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeVerificador;
import com.dperez.maymweb.producto.Producto;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class VerCorrectiva implements Serializable {
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    @Inject
    private FacadeVerificador fVerif;
    
    private Date FechaDeteccion;
    private String strFechaDeteccion;
    
    private Deteccion GeneradaPor;
    private Area AreaSector;
    private String Descripcion;
    private String AnalisisCausa;
    private String Cliente;
    
    private List<Producto> ListaProductos;

    
    private Map<Integer, Actividad> MedidasCorrectivas;
    private Map<Integer, Actividad> MedidasPreventivas;
    
    private Date FechaImplementacion;
    
    private String ObservacionesComprobacion;
    private Date FechaComprobacion;
    
    private Comprobacion ComprobacionImplementacion;
    private Comprobacion ComprobacionEficacia;
    
    private Accion AccionSeleccionada;
    
    private Empresa EmpresaAccion;
    
    private EnumEstado Estado;
    
    //  Getters
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
    public String getCliente() {return Cliente;}
    
    public List<Producto> getListaProductos() {return ListaProductos;}
    
    public Map<Integer, Actividad> getMedidasCorrectivas() {return MedidasCorrectivas;}
    public Map<Integer, Actividad> getMedidasPreventivas() {return MedidasPreventivas;}
    public Date getFechaImplementacion(){return this.FechaImplementacion;}
    public String getObservacionesComprobacion() {return ObservacionesComprobacion;}
    public Date getFechaComprobacion() {return FechaComprobacion;}
    public Accion getAccionSeleccionada() {return AccionSeleccionada;}
    public Empresa getEmpresaAccion(){return this.EmpresaAccion;}
    public EnumEstado getEstado() {return Estado;}
    public Comprobacion getComprobacionImplementacion() {return ComprobacionImplementacion;}
    public Comprobacion getComprobacionEficacia() {return ComprobacionEficacia;}
    
    //  Setters
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
    public void setCliente(String Cliente) {this.Cliente = Cliente;}
    
    public void setListaProductos(List<Producto> ListaProductos) {this.ListaProductos = ListaProductos;}   
    
    public void setMedidasCorrectivas(Map<Integer, Actividad> MedidasCorrectivas) {this.MedidasCorrectivas = MedidasCorrectivas;}
    public void setMedidasPreventivas(Map<Integer, Actividad> MedidasPreventivas) {this.MedidasPreventivas = MedidasPreventivas;}
    public void setFechaImplementacion(Date FechaImplementacion){this.FechaImplementacion = FechaImplementacion;}
    public void setObservacionesComprobacion(String ObservacionesComprobacion) {this.ObservacionesComprobacion = ObservacionesComprobacion;}
    public void setFechaComprobacion(Date FechaComprobacion) {this.FechaComprobacion = FechaComprobacion;}
    public void setAccionSeleccionada(Accion AccionSeleccionada) {this.AccionSeleccionada = AccionSeleccionada;}
    public void setEmpresaAccion(Empresa EmpresaAccion){this.EmpresaAccion = EmpresaAccion;}
    public void setEstado(EnumEstado Estado) {this.Estado = Estado;}
    public void setComprobacionImplementacion(Comprobacion ComprobacionImplementacion) {this.ComprobacionImplementacion = ComprobacionImplementacion;}
    public void setComprobacionEficacia(Comprobacion ComprobacionEficacia) {this.ComprobacionEficacia = ComprobacionEficacia;}
    
    //  Metodos
    //  Inicializcion
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion y el resto de las propiedades.
        int IdAccion = 0;
        try{
            IdAccion = Integer.parseInt(request.getParameter("id"));
        }catch(Exception nEx){}
        if(IdAccion != 0){
            AccionSeleccionada = (Correctiva) fLectura.GetAccion(IdAccion);
            FechaDeteccion = AccionSeleccionada.getFechaDeteccion();
            GeneradaPor = AccionSeleccionada.getDeteccion();
            AreaSector = AccionSeleccionada.getAreaSectorAccion();
            Descripcion = AccionSeleccionada.getDescripcion();
            AnalisisCausa = AccionSeleccionada.getAnalisisCausa();
            Estado = AccionSeleccionada.getEstadoAccion();
            ComprobacionImplementacion = AccionSeleccionada.getComprobacionImplementacion();
            ComprobacionEficacia = AccionSeleccionada.getComprobacionEficacia();
            Cliente = ((Correctiva)AccionSeleccionada).getCliente();
            List<Actividad> actividades = ((Correctiva)AccionSeleccionada).getActividades().stream()
                    .filter(a->a.getTipoActividad() == CORRECTIVA)
                    .collect(Collectors.toList());
            
            MedidasCorrectivas = new HashMap<>();
            MedidasCorrectivas = actividades.stream()
                    .collect(Collectors.toMap(Actividad::getIdActividad, actividad->actividad));
            actividades.clear();
            
            actividades = ((Correctiva)AccionSeleccionada).getActividades().stream()
                    .filter(a->a.getTipoActividad() == PREVENTIVA)
                    .collect(Collectors.toList());
            MedidasPreventivas = new HashMap<>();
            MedidasPreventivas = actividades.stream()
                    .collect(Collectors.toMap(Actividad::getIdActividad, actividad->actividad));
            
            ListaProductos = new ArrayList<>();
            if(!((Correctiva)AccionSeleccionada).getProductosAfectados().isEmpty()){
                ListaProductos = ((Correctiva)AccionSeleccionada).getProductosAfectados();
            }

        }
    }
}
