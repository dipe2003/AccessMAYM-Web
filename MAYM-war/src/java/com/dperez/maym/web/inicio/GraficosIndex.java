/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.inicio;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.acciones.Estado;
import static com.dperez.maymweb.modelo.acciones.Estado.CERRADA;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author dperez
 */
@Named
@ViewScoped
public class GraficosIndex implements Serializable{
    
    private FacadeLectura fLectura;
    
    // Valores generales
    private Date fechaInicial;
    private Date fechaFinal;
    private String strFechaInicial;
    private String strFechaFinal;
    private List<Accion> listaTotalAcciones;
    
    // Valores para grafico Estados
    private int TotalAcciones;
    private int AccionesCerradas;
    private int AccionesPendientes;
    private int AccionesDesestimadas;
    private int AccionesProcesoImp;
    private int AccionesProcesoVerif;
    
    // Valores para grafico de tipos
    private int AccionesMejora;
    private int AccionesCorrectivas;
    private int AccionesPreventivas;
    
    // Valores para graficos de proceso
    private String totalesPorProceso;
    private String nombresProcesos;
    private int tamanio;
    
    // Valores para grafico de detecciones
    private int totalDeteccionesInternas;
    private int totalDeteccionesExternas;
    
    // Valores para grafico por codificaciones
    private String  nombresCodificaciones;
    private String totalCodificaciones;
    
    //<editor-fold desc="Fechas">
    // Getters
    public Date getFechaInicial(){return fechaInicial;}
    public Date getFechaFinal() {return fechaFinal;}
    
    public String getStrFechaInicial() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaInicial == null) {
            return this.strFechaInicial;
        }else{
            return fDate.format(fechaInicial);
        }
    }
    public String getStrFechaFinal(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaFinal == null) {
            return this.strFechaFinal;
        }else{
            return fDate.format(fechaFinal);
        }
    }
    
    // Setters
    public void setFechaInicial(Date fechaInicial) {this.fechaInicial = fechaInicial;}
    public void setFechaFinal(Date fechaFinal) {this.fechaFinal = fechaFinal;}
    
    public void setStrFechaInicial(String strFechaInicial) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaInicial));
        }catch(ParseException ex){}
        this.strFechaInicial = strFechaInicial;
        this.fechaInicial = cal.getTime();
    }
    public void setStrFechaFinal(String strFechaFinal) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaFinal));
        }catch(ParseException ex){}
        this.strFechaFinal = strFechaFinal;
        this.fechaFinal = cal.getTime();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Estados">
    // Getters
    public int getTotalAcciones() {return TotalAcciones;}
    public int getAccionesCerradas() {return AccionesCerradas;}
    public int getAccionesPendientes() {return AccionesPendientes;}
    public int getAccionesDesestimadas() {return AccionesDesestimadas;}
    public int getAccionesProcesoImp() {return AccionesProcesoImp;}
    public int getAccionesProcesoVerif() {return AccionesProcesoVerif;}
    
    // Setters
    public void setTotalAcciones(int TotalAcciones) {this.TotalAcciones = TotalAcciones;}
    public void setAccionesCerradas(int AccionesCerradas) {this.AccionesCerradas = AccionesCerradas;}
    public void setAccionesPendientes(int AccionesPendientes) {this.AccionesPendientes = AccionesPendientes;}
    public void setAccionesDesestimadas(int AccionesDesestimadas) {this.AccionesDesestimadas = AccionesDesestimadas;}
    public void setAccionesProcesoImp(int AccionesProcesoImp) {this.AccionesProcesoImp = AccionesProcesoImp;}
    public void setAccionesProcesoVerif(int AccionesProcesoVerif) {this.AccionesProcesoVerif = AccionesProcesoVerif;}
    //</editor-fold>
    
    //<editor-fold desc="Tipos">
    // Getters
    public int getAccionesMejora() {return AccionesMejora;}
    public int getAccionesCorrectivas() {return AccionesCorrectivas;}
    public int getAccionesPreventivas() {return AccionesPreventivas;}
    public int getTamanio() {return tamanio;}
    
    // Setters
    public void setAccionesMejora(int AccionesMejora) {this.AccionesMejora = AccionesMejora;}
    public void setAccionesCorrectivas(int AccionesCorrectivas) {this.AccionesCorrectivas = AccionesCorrectivas;}
    public void setAccionesPreventivas(int AccionesPreventivas) {this.AccionesPreventivas = AccionesPreventivas;}
    public void setTamanio(int tamanio) {this.tamanio = tamanio;}
    
    //</editor-fold>
    
    //<editor-fold desc="Por Procesos">
    // Getters
    public String getTotalesPorProceso() {return totalesPorProceso;}
    public String getNombresProcesos() {return nombresProcesos;}
    
    // Setters
    public void setNombresProcesos(String nombresProcesos) {this.nombresProcesos = nombresProcesos;}
    public void setTotalesPorProceso(String totalesPorProceso){this.totalesPorProceso = totalesPorProceso;}
    //</editor-fold>
    // Getters
    
    //<editor-fold desc="Detecciones">
    public int getTotalDeteccionesInternas() {return totalDeteccionesInternas;}
    public int getTotalDeteccionesExternas() {return totalDeteccionesExternas;}
    
    // Setters
    public void setTotalDeteccionesInternas(int totalDeteccionesInternas) {this.totalDeteccionesInternas = totalDeteccionesInternas;}
    public void setTotalDeteccionesExternas(int totalDeteccionesExternas) {this.totalDeteccionesExternas = totalDeteccionesExternas;}
    
    //</editor-fold>
    
    //<editor-fold desc="Codificaciones">
    // Getters
    public String getNombresCodificaciones() {
        return nombresCodificaciones;
    }
    
    public String getTotalCodificaciones() {
        return totalCodificaciones;
    }
    // Setters
    
    public void setTotalCodificaciones(String totalCodificaciones) {
        this.totalCodificaciones = totalCodificaciones;
    }
    
    public void setNombresCodificaciones(String nombresCodificaciones) {
        this.nombresCodificaciones = nombresCodificaciones;
    }
    
    //</editor-fold>
    
    // Metodos
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        // llenar los valores de estado.
        listaTotalAcciones = fLectura.listarAcciones();
        List<Accion> lstAcciones;
        
        // calcular fechas
        fechaInicial = calcularFechaInicial(listaTotalAcciones);
        fechaFinal = calcularFechaFinal(listaTotalAcciones);
        
        lstAcciones = filtrarFechasAcciones(listaTotalAcciones, fechaInicial, fechaFinal);
        
        // total por tipo de deteccion
        calcularTotalDeteccion(lstAcciones);
        
        // totales por tipo
        calcularTotalPorTipo(lstAcciones);
        
        TotalAcciones = lstAcciones.size();
        
        // totales por estado
        calcularTotalPorEstado(lstAcciones);
        
        // totales por codificacion
        List<Codificacion> codificaciones = extraerCodificacionAcciones(lstAcciones);
        totalCodificaciones = generarTotalesPorCodificaciones(codificaciones);
        nombresCodificaciones = generarNombresPorCodificaciones(codificaciones);
        
        // totales por area
        List<Area> listaAreas = extraerAreasAcciones(lstAcciones);
        tamanio = listaAreas.size();
        
        totalesPorProceso = generarTotalesPorProceso(listaAreas);
        nombresProcesos = generarNombresPorProceso(listaAreas);
    }
    
    private List<Accion> filtrarFechasAcciones(List<Accion> acciones, Date fechaInicial, Date fechaFinal){
        return acciones.stream()
                .filter((Accion accion)-> accion.getFechaDeteccion().after(fechaInicial) && accion.getFechaDeteccion().before(fechaFinal))
                .toList();
    }
    
    /**
     * Calcula el total de cada estado y lo acumula en las variables correspondientes.
     * @param acciones
     */
    private void calcularTotalPorEstado(List<Accion> acciones){
        AccionesCerradas = (int) acciones.stream()
                .filter((Accion accion)->accion.getEstadoDeAccion() == Estado.CERRADA)
                .count();
        AccionesDesestimadas = (int) acciones.stream()
                .filter((Accion accion)->accion.getEstadoDeAccion() == Estado.DESESTIMADA)
                .count();
        AccionesPendientes = (int) acciones.stream()
                .filter((Accion accion)->accion.getEstadoDeAccion() == Estado.PENDIENTE)
                .count();
        AccionesProcesoImp = (int) acciones.stream()
                .filter((Accion accion)->accion.getEstadoDeAccion() == Estado.PROCESO_IMP)
                .count();
        AccionesProcesoVerif = (int) acciones.stream()
                .filter((Accion accion)->accion.getEstadoDeAccion() == Estado.PROCESO_VER)
                .count();
    }
    
    /**
     * Determina el tipo de accion y aumenta el valor correspondiente.
     * @param accion
     */
    private void calcularTotalPorTipo(List<Accion> acciones){
        AccionesCorrectivas = (int) acciones.stream()
                .filter((Accion accion)->accion.getClass().getSimpleName().equalsIgnoreCase("correctiva"))
                .count();
        AccionesPreventivas = (int) acciones.stream()
                .filter((Accion accion)->accion.getClass().getSimpleName().equalsIgnoreCase("preventiva"))
                .count();
        AccionesMejora = (int) acciones.stream()
                .filter((Accion accion)->accion.getClass().getSimpleName().equalsIgnoreCase("mejora")).count();
    }
    
    /**
     * Determina el tipo de deteccion de cada accion y calcula el total.
     * @param acciones
     */
    private void calcularTotalDeteccion(List<Accion> acciones){
        totalDeteccionesInternas = (int) acciones.stream()
                .filter((Accion accion)->accion.getDeteccionAccion().getTipoDeDeteccion() == TipoDeteccion.INTERNA)
                .count();
        totalDeteccionesExternas = (int) acciones.stream()
                .filter((Accion accion)->accion.getDeteccionAccion().getTipoDeDeteccion() == TipoDeteccion.EXTERNA)
                .count();
    }
    
    private Date calcularFechaInicial(List<Accion> acciones){
        return acciones.stream()
                .min(Comparator.naturalOrder()).get().getFechaDeteccion();
    }
    
    private Date calcularFechaFinal(List<Accion> acciones){
        return acciones.stream()
                .max(Comparator.naturalOrder()).get().getFechaDeteccion();
    }
    
    /**
     * Genera una cadena con arreglo del total de acciones en cada area.
     * @param areas
     * @return
     */
    private String generarTotalesPorProceso(List<Area> areas){
        StringBuilder strBuilderTotales = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            strBuilderTotales.append(areas.get(i).getAccionesArea().size());
            if(i+1 < tamanio){
                strBuilderTotales.append(",");
            }
        }
        return strBuilderTotales.insert(0, "[").append("]").toString();
    }
    
    /**
     * Genera una cadena con arreglo de cada nombre de area.
     * @param areas
     * @return
     */
    private String generarNombresPorProceso(List<Area> areas){
        StringBuilder strBuilderNombres = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            strBuilderNombres
                    .append("'")
                    .append(areas.get(i).getNombre())
                    .append("'");
            if(i+1 < tamanio){
                strBuilderNombres.append(",");
            }
        }
        return strBuilderNombres.insert(0, "[").append("]").toString();
    }
    
    /**
     * Genera una cadena con arreglo del total de acciones por codificacion.
     * @param codificaciones
     * @return
     */
    private String generarTotalesPorCodificaciones(List<Codificacion> codificaciones){
        StringBuilder strBuilderTotales = new StringBuilder();
        int total = codificaciones.size();
        for (int i = 0; i < total; i++) {
            strBuilderTotales.append(codificaciones.get(i).getAccionesCodificacion().size());
            if(i+1 < total){
                strBuilderTotales.append(",");
            }
        }
        return strBuilderTotales.insert(0, "[").append("]").toString();
    }
    
    /**
     * Genera una cadena con arreglo de cada nombre de codificacion.
     * @param codificaciones
     * @return
     */
    private String generarNombresPorCodificaciones(List<Codificacion> codificaciones){
        StringBuilder strBuilderNombres = new StringBuilder();
        int total = codificaciones.size();
        for (int i = 0; i < total; i++) {
            strBuilderNombres
                    .append("'")
                    .append(codificaciones.get(i).getNombre())
                    .append("'");
            if(i+1 < total){
                strBuilderNombres.append(",");
            }
        }
        return strBuilderNombres.insert(0, "[").append("]").toString();
    }
    
    /**
     * Extrae una lista de las codificaciones de las acciones.
     * @param acciones
     * @return
     */
    private List<Codificacion> extraerCodificacionAcciones(List<Accion> acciones){
        List<Codificacion> codificaciones = new ArrayList<>();
        acciones.stream()
                .forEach((Accion accion)->{
                    if(!codificaciones.contains(accion.getCodificacionAccion())){
                        codificaciones.add(accion.getCodificacionAccion());
                    }
                });
        return codificaciones;
    }
    
    /**
     * Extrae una lista de las areas de las acciones.
     * @param acciones
     * @return
     */
    private List<Area> extraerAreasAcciones(List<Accion> acciones){
        List<Area> areas = new ArrayList<>();
        acciones.stream()
                .forEach((Accion accion)->{
                    if(!areas.contains(accion.getAreaAccion())){
                        areas.add(accion.getAreaAccion());
                    }
                });
        return areas;
    }
    
    /**
     * Se toman las fechas y se calculan todos los totales.
     */
    public void filtrarPorFecha(){
        
        List<Accion> lstAcciones = filtrarFechasAcciones(listaTotalAcciones, fechaInicial, fechaFinal);
        
        calcularTotalDeteccion(lstAcciones);
        
        calcularTotalPorTipo(lstAcciones);
        
        TotalAcciones = lstAcciones.size();
        
        // totales por estado
        calcularTotalPorEstado(lstAcciones);
        
        // totales por codificacion
        List<Codificacion> codificaciones = extraerCodificacionAcciones(lstAcciones);
        totalCodificaciones = generarTotalesPorCodificaciones(codificaciones);
        nombresCodificaciones = generarNombresPorCodificaciones(codificaciones);
        
        // totales por area
        List<Area> areas = extraerAreasAcciones(lstAcciones);
        tamanio = areas.size();
        
        totalesPorProceso = generarTotalesPorProceso(areas);
        nombresProcesos = generarNombresPorProceso(areas);
    }
    
    public void resetFechas(){
        fechaInicial = calcularFechaInicial(listaTotalAcciones);
        fechaFinal = calcularFechaFinal(listaTotalAcciones);
        filtrarPorFecha();
    }
    
}
