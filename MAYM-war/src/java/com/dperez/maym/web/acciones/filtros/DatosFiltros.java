/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.filtros;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class DatosFiltros implements Serializable {
    
    private FacadeLectura fLectura;
    
    private int idAccionBuscada;
    
    public int getIdAccionBuscada() {return idAccionBuscada;}
    public void setIdAccionBuscada(int idAccionBuscada) {this.idAccionBuscada = idAccionBuscada;}
    
    public DatosFiltros(){
        fLectura = new FacadeLectura();
    }
    
    public void init(){
        fLectura = new FacadeLectura();
    }
    
    //**********************************************************************
    // Metodos de buscar Id
    //**********************************************************************
    public void buscarId() throws IOException{
        String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        Accion accion = fLectura.GetAccion(idAccionBuscada);
        TipoAccion tipoDefault = TipoAccion.CORRECTIVA;
        if (accion != null) {
            tipoDefault = TipoAccion.valueOf(accion.getClass().getSimpleName().toUpperCase());
        }        
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(url+"/Views/Acciones/General/ListarAcciones.xhtml?pagina=1&tipo="+tipoDefault+"&buscarid="+idAccionBuscada);       
        
    }
    //**********************************************************************
    // Metodos de filtro de Fechas
    //**********************************************************************
    
    /**
     *
     * @param acciones
     * @return Date[0] fechaDesde , Date[1] fechaHasta
     */
    public Date[] ExtraerFechas(List<Accion> acciones){
        Date[] fechas = new Date[2];
        if (!acciones.isEmpty()){
            fechas[0]  = acciones.stream()
                    .sorted().max(Comparator.naturalOrder()).get().getFechaDeteccion();
            fechas[1]  = acciones.stream()
                    .sorted()
                    .min(Comparator.naturalOrder()).get().getFechaDeteccion();
        }
        return fechas;
    }
    
    /**
     * Devuelve una lista de acciones que fueron detectadas en el rango de fechas.
     * @param acciones
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public List<Accion> FiltrarAccionesPorFechas(List<Accion> acciones, Date fechaDesde, Date fechaHasta){
        return  acciones.stream()
                .filter((accion)-> (accion.getFechaDeteccion().after(fechaDesde) && accion.getFechaDeteccion().before(fechaHasta)))
                .sorted()
                .collect(Collectors.toList());
    }
    
    //**********************************************************************
    // Metodos de filtro de Areas
    //**********************************************************************
    
    /**
     * Devuelve una lista de areas que pertenezcan a las acciones.
     * @param acciones
     * @return
     */
    public Map<String, Area> ExtraerAreas(List<Accion> acciones){
        Map<String, Area> areas = new HashMap<>();
        if(!acciones.isEmpty()){
            acciones.stream()
                    .filter((accion) -> !areas.containsKey(String.valueOf(accion.getAreaAccion().getId())))
                    .forEach((accion)-> {
                        areas.put(String.valueOf(accion.getAreaAccion().getId()), accion.getAreaAccion());
                    });
        }
        return new TreeMap<>(areas);
    }
    
    /**
     * Devuelve una lista de acciones que pertenezcan a las areas indicadas.
     * @param acciones
     * @param areas
     * @return
     */
    public List<Accion> FiltrarAccionesPorArea(List<Accion> acciones, List<Integer> areas){
        return  acciones.stream()
                .filter((accion)-> (areas.contains(accion.getAreaAccion().getId())))
                .sorted()
                .collect(Collectors.toList());
    }
    
    //**********************************************************************
    // Metodos de filtro de Deteccion
    //**********************************************************************
    
    /**
     * Devuelve una lista de Detecciones que pertenezcan a las acciones.
     * @param acciones
     * @return
     */
    public Map<String, Deteccion> ExtraerDetecciones(List<Accion> acciones){
        Map<String, Deteccion> detecciones = new HashMap<>();
        if(!acciones.isEmpty()){
            acciones.stream()
                    .filter((accion) -> (!detecciones.containsKey(String.valueOf(accion.getDeteccionAccion().getId()))))
                    .forEachOrdered((accion) -> {
                        detecciones.put(String.valueOf(accion.getDeteccionAccion().getId()), accion.getDeteccionAccion());
                    });
        }
        return new TreeMap<>(detecciones);
    }
    
    /**
     * Devuelve una lista de acciones que contengan la misma deteccion (generada por).
     * @param acciones
     * @param detecciones
     * @return
     */
    public List<Accion> FiltrarAccionesPorDeteccion(List<Accion> acciones, List<Integer> detecciones){
        return acciones.stream()
                .filter((accion) -> (detecciones.contains(accion.getDeteccionAccion().getId())))
                .sorted()
                .collect(Collectors.toList());
    }
    
    //**********************************************************************
    // Metodos de filtro de Codificacion
    //**********************************************************************
    
    /**
     * Devuelve una lista de codificaciones que pertenezcan a las acciones.
     * @param acciones
     * @return
     */
    public Map<String, Codificacion> ExtraerCodificaciones(List<Accion> acciones){
        Map<String, Codificacion> codificaciones = new HashMap<>();
        if(!acciones.isEmpty()){
            acciones.stream()
                    .filter((accion) -> accion.getCodificacionAccion() != null && !codificaciones.containsKey(String.valueOf(accion.getCodificacionAccion().getId())))
                    .forEachOrdered((accion) -> {
                        codificaciones.put(String.valueOf(accion.getCodificacionAccion().getId()), accion.getCodificacionAccion());
                    });
        }
        return new TreeMap<>(codificaciones);
    }
    
    /**
     * Devuelve una lista de acciones que contengan la misma codificacion.
     * @param acciones
     * @param codificaciones
     * @return
     */
    public List<Accion> FiltrarAccionesPorCodificacion(List<Accion> acciones, List<Integer> codificaciones){
        return acciones.stream()
                .filter((accion) -> (codificaciones.contains(accion.getCodificacionAccion().getId())))
                .sorted()
                .collect(Collectors.toList());
    }
    
    //**********************************************************************
    // Metodos de filtro de Estado
    //**********************************************************************
    
    /**
     * Devuelve una lista de acciones que contengan el mismo Estado
     * @param acciones
     * @param estados
     * @return
     */
    public List<Accion> FiltrarAccionesPorEstado(List<Accion> acciones, List<Estado> estados){
        return acciones.stream()
                .filter((accion) -> (estados.contains(accion.getEstadoDeAccion())))
                .sorted()
                .collect(Collectors.toList());
    }
    
        //**********************************************************************
    // Metodos de filtro de Texto
    //**********************************************************************
    
    /**
     * Devuelve una lista de acciones que contengan el mismo texto
     * @param acciones
     * @param texto
     * @return
     */
    public List<Accion> FiltrarAccionesPorTexto(List<Accion> acciones, String texto){
        return acciones.stream()
                .filter((accion) -> (accion.getDescripcion().toLowerCase().contains(texto.toLowerCase())))
                .sorted()
                .collect(Collectors.toList());
    }
}
