/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.correctivas;

import com.dperez.maym.web.acciones.filtros.DatosFiltros;
import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.accion.acciones.Correctiva;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.estado.EnumEstado;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maym.web.herramientas.Presentacion;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Diego
 */
@Named
@ViewScoped
public class ListarCorrectivas implements Serializable{
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeAdministrador fAdmin;
    
    private List<Correctiva> ListaAcciones;
    
    // Pagination
    private static final int MAX_ITEMS = 30;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Correctiva> ListaCompletaAcciones;
    
    // Variabels de Filtros
    private final DatosFiltros filtros = new DatosFiltros();
    private List<String> filtrosAplicados = new ArrayList<>();
    
    // Filtros Fecha
    private Date fechaInicial;
    private String strFechaInicial;
    private Date fechaFinal;
    private String strFechaFinal;
    
    // Filtros AREA
    private Map<String, Area> areasEnRegistros = new HashMap<>();
    private String[] areasSeleccionadas;
    
    // Filtros Deteccion
    private Map<String, Deteccion> deteccionesEnRegistros = new HashMap<>();
    private String[] deteccionesSeleccionadas;
    
    // Filtros Estado
    private EnumEstado[] estadosEnRegistros = EnumEstado.values();
    private EnumEstado[] estadosSeleccionados;
    
    // Filtros Codificaciones
    private Map<String, Codificacion> codificacionesEnRegistros = new HashMap<>();
    private String[] codificacionesSeleccionadas;
    
    //  Getters
    public List<Correctiva>getListaAcciones() {return ListaAcciones;}
    public Map<String, Area> getAreasEnRegistros() {return areasEnRegistros;}
    public Map<String, Codificacion> getCodificacionesEnRegistros() {return codificacionesEnRegistros;}
    public Map<String, Deteccion> getDeteccionesEnRegistros() {return deteccionesEnRegistros;}
    public EnumEstado[] getEstadosEnRegistros() {return estadosEnRegistros;}
    public String[] getAreasSeleccionadas() {return areasSeleccionadas;}
    public String[] getDeteccionesSeleccionadas() {return deteccionesSeleccionadas;}
    public EnumEstado[] getEstadosSeleccionados() {return estadosSeleccionados;}
    public String[] getCodificacionesSeleccionadas() {return codificacionesSeleccionadas;}
    public Date getFechaInicial() {return fechaInicial;}
    public Date getFechaFinal() {return fechaFinal;}
    public String getStrFechaInicial(){
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
    
    // Paginacion
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Setters
    public void setListaAcciones(List<Correctiva> ListaAcciones) {this.ListaAcciones = ListaAcciones;}
    public void setAreasEnRegistros(Map<String, Area> areasEnRegistros) {this.areasEnRegistros = areasEnRegistros;}
    public void setCodificacionesEnRegistros(Map<String, Codificacion> codificacionesEnRegistros) {this.codificacionesEnRegistros = codificacionesEnRegistros;}
    public void setDeteccionesEnRegistros(Map<String, Deteccion> deteccionesEnRegistros) {this.deteccionesEnRegistros = deteccionesEnRegistros;}
    public void setEstadosEnRegistros(EnumEstado[] estadosEnRegistros) {this.estadosEnRegistros = estadosEnRegistros;}
    public void setAreasSeleccionadas(String[] areasSeleccionadas) {this.areasSeleccionadas = areasSeleccionadas;}
    public void setDeteccionesSeleccionadas(String[] deteccionesSeleccionadas) {this.deteccionesSeleccionadas = deteccionesSeleccionadas;}
    public void setEstadosSeleccionados(EnumEstado[] estadosSeleccionados) {this.estadosSeleccionados = estadosSeleccionados;}
    public void setCodificacionesSeleccionadas(String[] codificacionesSeleccionadas) {this.codificacionesSeleccionadas = codificacionesSeleccionadas;}
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
    
    //**********************************************************************
    // Metodos de filtro de Fechas
    //**********************************************************************
    
    /**
     * Llena la lista de acciones con las que contengan las areas seleccionadas.
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorFechas(List<Correctiva> accionesAFiltrar){
        return  filtros.FiltrarAccionesPorFechas((List<Accion>)(List<?>) accionesAFiltrar, this.getFechaInicial(), this.getFechaFinal());
    }
    
    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetFechasAcciones(){
        this.setFechaInicial(filtros.ExtraerFechas((List<Accion>)(List<?>) ListaCompletaAcciones)[0]);
        this.setFechaFinal(filtros.ExtraerFechas((List<Accion>)(List<?>) ListaCompletaAcciones)[1]);
    }
    
    public void filtrarPorFecha(AjaxBehaviorEvent event){
        if(!filtrosAplicados.contains("fechas")){
            filtrosAplicados.add("fechas");
        }
        FiltrarAcciones();
    }
    
    //**********************************************************************
    // Metodos de filtro de Areas
    //**********************************************************************
    /**
     * Llena la lista de acciones con las que contengan las areas seleccionadas.
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorArea(List<Correctiva> accionesAFiltrar){
        return  filtros.FiltrarAccionesPorArea((List<Accion>)(List<?>) accionesAFiltrar, ObtenerIdsSeleccionados(areasSeleccionadas));
    }
    
    /**
     * Llena la lista de acciones con todas las acciones.
     * Carga la pagina nuevamente.
     */
    public void quitarFiltroPorArea(){
        filtrosAplicados.remove("areas");
        ResetListasAreas();
        FiltrarAcciones();
    }
    
    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasAreas(){
        areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>) ListaCompletaAcciones);
        areasSeleccionadas = areasEnRegistros.keySet().toArray(new String[areasEnRegistros.size()]);
    }
    
    public void filtrarPorArea(AjaxBehaviorEvent event){
        if(!filtrosAplicados.contains("areas")){
            filtrosAplicados.add("areas");
        }
        FiltrarAcciones();
    }
    
    //**********************************************************************
    // Metodos de filtro de Deteccion
    //**********************************************************************
    
    /**
     * Llena la lista de acciones con las que contengan las detecciones seleccionadas.
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorDeteccion(List<Correctiva> accionesAFiltrar){
        return filtros.FiltrarAccionesPorDeteccion((List<Accion>)(List<?>) accionesAFiltrar, ObtenerIdsSeleccionados(deteccionesSeleccionadas));
    }
    
    /**
     * Llena la lista de acciones con todas las acciones.
     * Carga la pagina nuevamente.
     */
    public void quitarFiltroPorDeteccion(){
        filtrosAplicados.remove("detecciones");
        ResetListasDeteccion();
        FiltrarAcciones();
    }
    
    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasDeteccion(){
        deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>) ListaCompletaAcciones);
        deteccionesSeleccionadas = deteccionesEnRegistros.keySet().toArray(new String[deteccionesEnRegistros.size()]);
    }
    
    public void filtrarPorDeteccion(AjaxBehaviorEvent event){
        if(!filtrosAplicados.contains("detecciones")){
            filtrosAplicados.add("detecciones");
        }
        FiltrarAcciones();
    }
    
    //**********************************************************************
    // Metodos de filtro de Codificacion
    //**********************************************************************
    
    /**
     * Llena la lista de acciones con las que contengan las codificaciones seleccionadas.
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorCodificacion(List<Correctiva> accionesAFiltrar){
        return filtros.FiltrarAccionesPorCodificacion((List<Accion>)(List<?>) accionesAFiltrar, ObtenerIdsSeleccionados(codificacionesSeleccionadas));
    }
    
    /**
     * Llena la lista de acciones con todas las acciones.
     * Carga la pagina nuevamente.
     */
    public void quitarFiltroPorCodificacion(){
        filtrosAplicados.remove("codificaciones");
        ResetListasCodificacion();
        FiltrarAcciones();
    }
    
    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasCodificacion(){
        codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) ListaCompletaAcciones);
        codificacionesSeleccionadas = codificacionesEnRegistros.keySet().toArray(new String[codificacionesEnRegistros.size()]);
    }
    
    public void filtrarPorCodificacion(AjaxBehaviorEvent event){
        if(!filtrosAplicados.contains("codificaciones")){
            filtrosAplicados.add("codificaciones");
        }
        FiltrarAcciones();
    }
    
    //**********************************************************************
    // Metodos de filtro de Estado
    //**********************************************************************
    
    /**
     * Llena la lista de acciones con las que contengan los estados seleccionados.
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorEstado(List<Correctiva> accionesAFiltrar){
        return filtros.FiltrarAccionesPorEstado((List<Accion>)(List<?>) accionesAFiltrar, Arrays.asList(estadosSeleccionados));
    }
    
    /**
     * Llena la lista de acciones con todas las acciones.
     * Carga la pagina nuevamente.
     */
    public void quitarFiltroPorEstado(){
        filtrosAplicados.remove("estados");
        ResetListasEstado();
        FiltrarAcciones();
    }
    
    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasEstado(){
        estadosEnRegistros = EnumEstado.values();
        estadosSeleccionados = estadosEnRegistros;
    }
    
    public void filtrarPorEstado(AjaxBehaviorEvent event){
        if(!filtrosAplicados.contains("estados")){
            filtrosAplicados.add("estados");
        }
        FiltrarAcciones();
    }
    
    //**********************************************************************
    // Metodos de la clase
    //**********************************************************************
    
    /**
     * Obtiene los ids como enteros del array de strings.
     * @param arrayIds
     * @return
     */
    private List<Integer> ObtenerIdsSeleccionados(String[] arrayIds){
        List<Integer> ids = new ArrayList<>();
        for(String id:arrayIds){
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
    
    /**
     * Aplica los filtros uno sobre otro dependiendo de los que ya se encuentren seleccionados y el orden en que fueron seleccionados.
     * La variable FiltrosAplicados contienen los filtros seleccionados.
     * Sigue el orden predefinido: FECHAS, AREAS, DETECCIONES, CODIFICACIONES, ESTADO.
     *
     */
    private void FiltrarAcciones(){
        List<Correctiva> accionesFiltradas = ListaCompletaAcciones;
        for (int i = 0; i < filtrosAplicados.size(); i++) {
            String filtro = filtrosAplicados.get(i);
            switch(filtro){
                case "fechas":
                    // aplicar filtro de fechas
                    accionesFiltradas = (List<Correctiva>)(List<?>) filtrarPorFechas(accionesFiltradas);
                    // actualizar lista de areas disponibles, detecciones, codificaciones y estados
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                    break;
                    
                case "areas":
                    accionesFiltradas = (List<Correctiva>)(List<?>)filtrarPorArea(accionesFiltradas);
                    // actualizar lista de fechas disponibles, detecciones, codificaciones
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                    break;
                    
                case "detecciones":
                    accionesFiltradas = (List<Correctiva>)(List<?>) filtrarPorDeteccion(accionesFiltradas);
                    // actualizar lista de fechas disponibles, areas, codificaciones
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                    break;
                    
                case "codificaciones":
                    // aplicar filtro de Codificaciones
                    accionesFiltradas = (List<Correctiva>)(List<?>)filtrarPorCodificacion(accionesFiltradas);
                    // actualizar lista de fechas disponibles, areas, detecciones y estados
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    break;
                    
                case "estados":
                    accionesFiltradas = (List<Correctiva>)(List<?>)filtrarPorEstado(accionesFiltradas);
                    // actualizar lista de fechas disponibles, detecciones, codificaciones y estados
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                    break;
                    
                default: break;
            }
        }
        // Cargar pagina
        ListaAcciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, accionesFiltradas);
        ListaAcciones.stream().sorted();
        CantidadPaginas = Presentacion.calcularCantidadPaginas(accionesFiltradas.size(), MAX_ITEMS);
    }
    
    
    //  Inicializacion
    @PostConstruct
    public void init(){
        // recuperar Empresa para filtrar las acciones
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // Paginacion
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        
        // llenar la lista de acciones
        ListaAcciones = new ArrayList<>();
        ListaCompletaAcciones = (List<Correctiva>)(List<?>)fLectura.ListarAccionesCorrectivas();
        
        CantidadPaginas = Presentacion.calcularCantidadPaginas(ListaCompletaAcciones.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.  
        ListaAcciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaAcciones);
        ListaAcciones.stream().sorted();
        
// datos para filtros
        ResetFechasAcciones();        
        
        ResetListasAreas ();
        
        ResetListasDeteccion();
        
        ResetListasEstado();
        
        ResetListasCodificacion();
    }
}
