/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.general;

import com.dperez.maym.web.acciones.filtros.DatosFiltros;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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
public class ListarAcciones implements Serializable{
    
    private FacadeLectura fLectura;
    
    private TipoAccion tipoDeAccion;
    
    private List<Accion> ListaAcciones;
    
    private Accion accionSeleccionada;
    
    private ModalVerActividades verActividades;
    
    public ListarAcciones(){
        this.fLectura = new FacadeLectura();
    }
    
    //<editor-fold desc="Pagination">
    private static final int MAX_ITEMS = 30;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Accion> ListaCompletaAcciones;
    
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //</editor-fold>
    
    //<editor-fold desc="Filtros">
    @Inject
    private DatosFiltros filtros;
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
    private Estado[] estadosEnRegistros = Estado.values();
    private Estado[] estadosSeleccionados;
    
    // Filtros Codificaciones
    private Map<String, Codificacion> codificacionesEnRegistros = new HashMap<>();
    private String[] codificacionesSeleccionadas;
    
    //**********************************************************************
    // Metodos de filtro de Fechas
    //**********************************************************************
    
    /**
     * Llena la lista de acciones con las que contengan las areas seleccionadas.
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorFechas(List<Accion> accionesAFiltrar){
        return  filtros.FiltrarAccionesPorFechas(accionesAFiltrar, this.getFechaInicial(), this.getFechaFinal());
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
    private List<Accion> filtrarPorArea(List<Accion> accionesAFiltrar){
        return  filtros.FiltrarAccionesPorArea(accionesAFiltrar, ObtenerIdsSeleccionados(areasSeleccionadas));
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
    private List<Accion> filtrarPorDeteccion(List<Accion> accionesAFiltrar){
        return filtros.FiltrarAccionesPorDeteccion(accionesAFiltrar, ObtenerIdsSeleccionados(deteccionesSeleccionadas));
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
    private List<Accion> filtrarPorCodificacion(List<Accion> accionesAFiltrar){
        return filtros.FiltrarAccionesPorCodificacion(accionesAFiltrar, ObtenerIdsSeleccionados(codificacionesSeleccionadas));
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
        codificacionesEnRegistros = filtros.ExtraerCodificaciones(ListaCompletaAcciones);
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
    private List<Accion> filtrarPorEstado(List<Accion> accionesAFiltrar){
        return filtros.FiltrarAccionesPorEstado(accionesAFiltrar, Arrays.asList(estadosSeleccionados));
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
        estadosEnRegistros = Estado.values();
        estadosSeleccionados = estadosEnRegistros;
    }
    
    public void filtrarPorEstado(AjaxBehaviorEvent event){
        if(!filtrosAplicados.contains("estados")){
            filtrosAplicados.add("estados");
        }
        FiltrarAcciones();
    }
    
    /**
     * Aplica los filtros uno sobre otro dependiendo de los que ya se encuentren seleccionados y el orden en que fueron seleccionados.
     * La variable FiltrosAplicados contienen los filtros seleccionados.
     * Sigue el orden predefinido: FECHAS, AREAS, DETECCIONES, CODIFICACIONES, ESTADO.
     *
     */
    private void FiltrarAcciones(){
        List<Accion> accionesFiltradas = ListaCompletaAcciones;
        for (int i = 0; i < filtrosAplicados.size(); i++) {
            String filtro = filtrosAplicados.get(i);
            switch(filtro){
                case "fechas" -> {
                    // aplicar filtro de fechas
                    accionesFiltradas = filtrarPorFechas(accionesFiltradas);
                    // actualizar lista de areas disponibles, detecciones, codificaciones y estados
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                }
                
                case "areas" -> {
                    accionesFiltradas = filtrarPorArea(accionesFiltradas);
                    // actualizar lista de fechas disponibles, detecciones, codificaciones
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                }
                
                case "detecciones" -> {
                    accionesFiltradas = filtrarPorDeteccion(accionesFiltradas);
                    // actualizar lista de fechas disponibles, areas, codificaciones
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                }
                
                case "codificaciones" -> {
                    // aplicar filtro de Codificaciones
                    accionesFiltradas = filtrarPorCodificacion(accionesFiltradas);
                    // actualizar lista de fechas disponibles, areas, detecciones y estados
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                }
                
                case "estados" -> {
                    accionesFiltradas = filtrarPorEstado(accionesFiltradas);
                    // actualizar lista de fechas disponibles, detecciones, codificaciones y estados
                    areasEnRegistros = filtros.ExtraerAreas((List<Accion>)(List<?>)accionesFiltradas);
                    deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>)(List<?>)accionesFiltradas);
                    codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>)(List<?>) accionesFiltradas);
                }
                
                default -> {
                }
            }
        }
        // Cargar pagina
        ListaAcciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, accionesFiltradas);
        ListaAcciones.sort(Comparator.reverseOrder());
        CantidadPaginas = Presentacion.calcularCantidadPaginas(accionesFiltradas.size(), MAX_ITEMS);
    }
    //</editor-fold>
    
    //<editor-fold desc="Getters">
    public List<Accion>getListaAcciones() {return ListaAcciones;}
    public Map<String, Area> getAreasEnRegistros() {return areasEnRegistros;}
    public Map<String, Codificacion> getCodificacionesEnRegistros() {return codificacionesEnRegistros;}
    public Map<String, Deteccion> getDeteccionesEnRegistros() {return deteccionesEnRegistros;}
    public Estado[] getEstadosEnRegistros() {return estadosEnRegistros;}
    public String[] getAreasSeleccionadas() {return areasSeleccionadas;}
    public String[] getDeteccionesSeleccionadas() {return deteccionesSeleccionadas;}
    public Estado[] getEstadosSeleccionados() {return estadosSeleccionados;}
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
    public TipoAccion getTipoDeAccion() {return tipoDeAccion;}
    public Accion getAccionSeleccionada(){return this.accionSeleccionada;}
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setListaAcciones(List<Accion> ListaAcciones) {this.ListaAcciones = ListaAcciones;}
    public void setAreasEnRegistros(Map<String, Area> areasEnRegistros) {this.areasEnRegistros = areasEnRegistros;}
    public void setCodificacionesEnRegistros(Map<String, Codificacion> codificacionesEnRegistros) {this.codificacionesEnRegistros = codificacionesEnRegistros;}
    public void setDeteccionesEnRegistros(Map<String, Deteccion> deteccionesEnRegistros) {this.deteccionesEnRegistros = deteccionesEnRegistros;}
    public void setEstadosEnRegistros(Estado[] estadosEnRegistros) {this.estadosEnRegistros = estadosEnRegistros;}
    public void setAreasSeleccionadas(String[] areasSeleccionadas) {this.areasSeleccionadas = areasSeleccionadas;}
    public void setDeteccionesSeleccionadas(String[] deteccionesSeleccionadas) {this.deteccionesSeleccionadas = deteccionesSeleccionadas;}
    public void setEstadosSeleccionados(Estado[] estadosSeleccionados) {this.estadosSeleccionados = estadosSeleccionados;}
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
    
    public void setTipoDeAccion(TipoAccion tipoDeAccion) {this.tipoDeAccion = tipoDeAccion;}
    public void setAccionSeleccionada(Accion accion){this.accionSeleccionada = accion;}
    //</editor-fold>
    
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
    
    public void SeleccionarAccion(int id){
        accionSeleccionada = ListaAcciones.stream().filter((Accion accion)->accion.getId() == id).findFirst().get();
        verActividades.setAccionSeleccionada(accionSeleccionada);
    }
    
    public void quitarFiltros(){
        filtrosAplicados.remove("estados");
        filtrosAplicados.remove("codificaciones");
        filtrosAplicados.remove("detecciones");
        filtrosAplicados.remove("areas");
        
        ResetFechasAcciones();
        ResetListasAreas ();
        ResetListasDeteccion();
        ResetListasEstado();
        ResetListasCodificacion();
        
        FiltrarAcciones();
    }
    
    //  Inicializacion
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        verActividades = context.getApplication().evaluateExpressionGet(context, "#{modalVerActividades}", ModalVerActividades.class);
        int id = 0;
        tipoDeAccion = TipoAccion.valueOf(request.getParameter("tipo"));
        try{
            id = Integer.parseInt(request.getParameter("buscarid"));
        }catch(NumberFormatException ex){}
        
        // Paginacion
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        
        // llenar la lista de acciones dependiendo si se esta filtrando por Id
        ListaAcciones = new ArrayList<>();
        ListaCompletaAcciones = new ArrayList<>();
        if (id > 0){
            Accion accion = fLectura.GetAccion(id);
            if (accion != null){
                ListaCompletaAcciones.add(accion);
            }
        }else{
            switch(tipoDeAccion){
                case CORRECTIVA -> ListaCompletaAcciones = fLectura.listarAccionesCorrectivas();
                case PREVENTIVA -> ListaCompletaAcciones = fLectura.listarAccionesPreventivas();
                case MEJORA -> ListaCompletaAcciones = fLectura.listarAccionesMejoras();
            }
        }
        
        CantidadPaginas = Presentacion.calcularCantidadPaginas(ListaCompletaAcciones.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
        ListaCompletaAcciones.sort(Comparator.reverseOrder());
        ListaAcciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaAcciones);
        
        // datos para filtros
        ResetFechasAcciones();
        
        ResetListasAreas ();
        
        ResetListasDeteccion();
        
        ResetListasEstado();
        
        ResetListasCodificacion();
    }
    
}
