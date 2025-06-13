/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.acciones.general;

import com.dperez.maym.web.acciones.filtros.DatosFiltros;
import com.dperez.maym.web.herramientas.exportacion.pdftea.PdfteameListado;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maym.web.herramientas.exportacion.excelsea.Excelsea;
import com.dperez.maym.web.herramientas.exportacion.pdftea.PdfteameRegistro;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import static com.dperez.maymweb.modelo.acciones.TipoAccion.CORRECTIVA;
import static com.dperez.maymweb.modelo.acciones.TipoAccion.MEJORA;
import static com.dperez.maymweb.modelo.acciones.TipoAccion.PREVENTIVA;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
public class ListarAcciones implements Serializable {

    @Inject
    SesionUsuario sesionUsuario;

    private FacadeLectura fLectura;

    private TipoAccion tipoDeAccion;

    private List<Accion> ListaAcciones;

    private Accion accionSeleccionada;

    private ModalVerActividades verActividades;

    private boolean esBusqueda;

    public ListarAcciones() {
        this.fLectura = new FacadeLectura();
    }

    //<editor-fold desc="Pagination">
    private static final int MAX_ITEMS = 30;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Accion> ListaCompletaAcciones;

    public static int getMAX_ITEMS() {
        return MAX_ITEMS;
    }

    public int getCantidadPaginas() {
        return CantidadPaginas;
    }

    public int getPaginaActual() {
        return PaginaActual;
    }

    //</editor-fold>
    //<editor-fold desc="Filtros">
    @Inject
    private DatosFiltros filtros;

    // Filtros Fecha
    private Date fechaInicial;
    private String strFechaInicial;
    private Date fechaFinal;
    private String strFechaFinal;

    // Filtros AREA
    private List<Area> areasEnRegistros = new ArrayList<>();

    // Filtros Deteccion
    private List<Deteccion> deteccionesEnRegistros = new ArrayList<>();

    // Filtros Estado
    private Estado[] estadosEnRegistros = Estado.values();

    // Filtros Codificaciones
    private List<Codificacion> codificacionesEnRegistros = new ArrayList<>();

    // Filtro texto
    private String textoBusqueda;

    //**********************************************************************
    // Metodos de filtro de Fechas
    //**********************************************************************
    /**
     * Llena la lista de acciones con las que contengan las areas seleccionadas.
     *
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorFechas(List<Accion> accionesAFiltrar) {
        return filtros.FiltrarAccionesPorFechas(accionesAFiltrar, this.getFechaInicial(), this.getFechaFinal());
    }

    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetFechasAcciones() {
        this.setFechaInicial(filtros.ExtraerFechas((List<Accion>) (List<?>) ListaCompletaAcciones)[0]);
        this.setFechaFinal(filtros.ExtraerFechas((List<Accion>) (List<?>) ListaCompletaAcciones)[1]);
    }

    public void filtrarPorFecha(AjaxBehaviorEvent event) {
        if (!sesionUsuario.contieneFiltro("fechas")) {
            sesionUsuario.addFiltro("fechas");
        }
        FiltrarAcciones();
    }

    //**********************************************************************
    // Metodos de filtro de Areas
    //**********************************************************************
    /**
     * Llena la lista de acciones con las que contengan las areas seleccionadas.
     *
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorArea(List<Accion> accionesAFiltrar) {
        return filtros.FiltrarAccionesPorArea(accionesAFiltrar, ObtenerIdsSeleccionados(sesionUsuario.getAreasSeleccionadas()));
    }

    /**
     * Llena la lista de acciones con todas las acciones. Carga la pagina
     * nuevamente.
     */
    public void quitarFiltroPorArea() {
        sesionUsuario.removeFiltro("areas");
        ResetListasAreas();
        FiltrarAcciones();
    }

    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasAreas() {
        areasEnRegistros = filtros.ExtraerAreas((List<Accion>) (List<?>) ListaCompletaAcciones);
        sesionUsuario.setAreasSeleccionadas(new String[areasEnRegistros.size()]);
        for (int i = 0; i < areasEnRegistros.size(); i++) {
            sesionUsuario.getAreasSeleccionadas()[i] = areasEnRegistros.get(i).getNombre();
        }
    }

    public void filtrarPorArea() {
        if (!sesionUsuario.contieneFiltro("areas")) {
            sesionUsuario.addFiltro("areas");
        }
        FiltrarAcciones();
    }

    //**********************************************************************
    // Metodos de filtro de Deteccion
    //**********************************************************************
    /**
     * Llena la lista de acciones con las que contengan las detecciones
     * seleccionadas.
     *
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorDeteccion(List<Accion> accionesAFiltrar) {
        return filtros.FiltrarAccionesPorDeteccion(accionesAFiltrar, ObtenerIdsSeleccionados(sesionUsuario.getDeteccionesSeleccionadas()));
    }

    /**
     * Llena la lista de acciones con todas las acciones. Carga la pagina
     * nuevamente.
     */
    public void quitarFiltroPorDeteccion() {
        sesionUsuario.removeFiltro("detecciones");
        ResetListasDeteccion();
        FiltrarAcciones();
    }

    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasDeteccion() {
        deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>) (List<?>) ListaCompletaAcciones);
        sesionUsuario.setDeteccionesSeleccionadas(new String[deteccionesEnRegistros.size()]);
        for (int i = 0; i < deteccionesEnRegistros.size(); i++) {
            sesionUsuario.getDeteccionesSeleccionadas()[i] = deteccionesEnRegistros.get(i).getNombre();
        }
    }

    public void filtrarPorDeteccion() {
        if (!sesionUsuario.contieneFiltro("detecciones")) {
            sesionUsuario.addFiltro("detecciones");
        }
        FiltrarAcciones();
    }

    //**********************************************************************
    // Metodos de filtro de Codificacion
    //**********************************************************************
    /**
     * Llena la lista de acciones con las que contengan las codificaciones
     * seleccionadas.
     *
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorCodificacion(List<Accion> accionesAFiltrar) {
        return filtros.FiltrarAccionesPorCodificacion(accionesAFiltrar, ObtenerIdsSeleccionados(sesionUsuario.getCodificacionesSeleccionadas()));
    }

    /**
     * Llena la lista de acciones con todas las acciones. Carga la pagina
     * nuevamente.
     */
    public void quitarFiltroPorCodificacion() {
        sesionUsuario.removeFiltro("codificaciones");
        ResetListasCodificacion();
        FiltrarAcciones();
    }

    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasCodificacion() {
        codificacionesEnRegistros = filtros.ExtraerCodificaciones(ListaCompletaAcciones);
        sesionUsuario.setCodificacionesSeleccionadas(new String[codificacionesEnRegistros.size()]);
        for (int i = 0; i < codificacionesEnRegistros.size(); i++) {
            sesionUsuario.getCodificacionesSeleccionadas()[i] = codificacionesEnRegistros.get(i).getNombre();
        }
    }

    public void filtrarPorCodificacion() {
        if (!sesionUsuario.contieneFiltro("codificaciones")) {
            sesionUsuario.addFiltro("codificaciones");
        }
        FiltrarAcciones();
    }

    //**********************************************************************
    // Metodos de filtro de Estado
    //**********************************************************************
    /**
     * Llena la lista de acciones con las que contengan los estados
     * seleccionados.
     *
     * @param accionesAFiltrar
     * @return
     */
    private List<Accion> filtrarPorEstado(List<Accion> accionesAFiltrar) {
        return filtros.FiltrarAccionesPorEstado(accionesAFiltrar, Arrays.asList(sesionUsuario.getEstadosSeleccionados()));
    }

    /**
     * Llena la lista de acciones con todas las acciones. Carga la pagina
     * nuevamente.
     */
    public void quitarFiltroPorEstado() {
        sesionUsuario.removeFiltro("estados");
        ResetListasEstado();
        FiltrarAcciones();
    }

    /**
     * Llena las listas para filtros con los valores originales.
     */
    private void ResetListasEstado() {
        estadosEnRegistros = Estado.values();
        sesionUsuario.setEstadosSeleccionados(estadosEnRegistros);
    }

    public void filtrarPorEstado() {
        if (!sesionUsuario.contieneFiltro("estados")) {
            sesionUsuario.addFiltro("estados");
        }
        FiltrarAcciones();
    }

    //**********************************************************************
    // Metodos de filtro de Busqueda
    //**********************************************************************
    private List<Accion> filtrarTexto(List<Accion> accionesAFiltrar) {
        return filtros.FiltrarAccionesPorTexto(accionesAFiltrar, textoBusqueda);
    }

    public void resetTexto() {
        textoBusqueda = "";
    }

    public void quitarFiltroTexto() {
        sesionUsuario.removeFiltro("busqueda");
        FiltrarAcciones();
    }

    public void filtrarTexto() {
        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            if (!sesionUsuario.contieneFiltro("busqueda")) {
                sesionUsuario.addFiltro("busqueda");
            }
            FiltrarAcciones();
        }
    }

    /**
     * Aplica los filtros uno sobre otro dependiendo de los que ya se encuentren
     * seleccionados y el orden en que fueron seleccionados. La variable
     * FiltrosAplicados contienen los filtros seleccionados. Sigue el orden
     * predefinido: FECHAS, AREAS, DETECCIONES, CODIFICACIONES, ESTADO.
     *
     */
    private void FiltrarAcciones() {
        List<Accion> accionesFiltradas = ListaCompletaAcciones;
        if (!accionesFiltradas.isEmpty()) {
            for (int i = 0; i < sesionUsuario.getFiltrosAplicados().size(); i++) {
                String filtro = sesionUsuario.getFiltrosAplicados().get(i);
                switch (filtro) {
                    case "fechas" -> {
                        // aplicar filtro de fechas
                        accionesFiltradas = filtrarPorFechas(accionesFiltradas);
                    }

                    case "areas" -> {
                        accionesFiltradas = filtrarPorArea(accionesFiltradas);
                    }

                    case "detecciones" -> {
                        accionesFiltradas = filtrarPorDeteccion(accionesFiltradas);
                    }

                    case "codificaciones" -> {
                        // aplicar filtro de Codificaciones
                        accionesFiltradas = filtrarPorCodificacion(accionesFiltradas);
                    }

                    case "estados" -> {
                        accionesFiltradas = filtrarPorEstado(accionesFiltradas);
                    }

                    case "busqueda" -> {
                        // aplicar busqueda de texto
                        accionesFiltradas = filtrarTexto(accionesFiltradas);
                    }

                    default -> {
                    }
                }
            }

            areasEnRegistros = filtros.ExtraerAreas((List<Accion>) (List<?>) ListaCompletaAcciones);
            deteccionesEnRegistros = filtros.ExtraerDetecciones((List<Accion>) (List<?>) ListaCompletaAcciones);
            codificacionesEnRegistros = filtros.ExtraerCodificaciones((List<Accion>) (List<?>) ListaCompletaAcciones);
            // Cargar pagina
            accionesFiltradas.sort(Comparator.reverseOrder());
            cargarPagina(accionesFiltradas);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public List<Accion> getListaAcciones() {
        return ListaAcciones;
    }

    public List<Area> getAreasEnRegistros() {
        return areasEnRegistros;
    }

    public List<Codificacion> getCodificacionesEnRegistros() {
        return codificacionesEnRegistros;
    }

    public List<Deteccion> getDeteccionesEnRegistros() {
        return deteccionesEnRegistros;
    }

    public Estado[] getEstadosEnRegistros() {
        return estadosEnRegistros;
    }

    public String[] getAreasSeleccionadas() {
        return sesionUsuario.getAreasSeleccionadas();
    }

    public String[] getDeteccionesSeleccionadas() {
        return sesionUsuario.getDeteccionesSeleccionadas();
    }

    public Estado[] getEstadosSeleccionados() {
        return sesionUsuario.getEstadosSeleccionados();
    }

    public String[] getCodificacionesSeleccionadas() {
        return sesionUsuario.getCodificacionesSeleccionadas();
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public String getStrFechaInicial() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yy");
        if (fechaInicial == null) {
            return this.strFechaInicial;
        } else {
            return fDate.format(fechaInicial);
        }
    }

    public String getStrFechaFinal() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yy");
        if (fechaFinal == null) {
            return this.strFechaFinal;
        } else {
            return fDate.format(fechaFinal);
        }
    }

    public TipoAccion getTipoDeAccion() {
        return tipoDeAccion;
    }

    public Accion getAccionSeleccionada() {
        return this.accionSeleccionada;
    }

    public boolean isEsBusqueda() {
        return esBusqueda;
    }

    public String getTextoBusqueda() {
        return this.textoBusqueda;
    }

    public List<String> getFiltrosAplicados() {
        return sesionUsuario.getFiltrosAplicados();
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setListaAcciones(List<Accion> ListaAcciones) {
        this.ListaAcciones = ListaAcciones;
    }

    public void setAreasEnRegistros(List<Area> areasEnRegistros) {
        this.areasEnRegistros = areasEnRegistros;
    }

    public void setCodificacionesEnRegistros(List<Codificacion> codificacionesEnRegistros) {
        this.codificacionesEnRegistros = codificacionesEnRegistros;
    }

    public void setDeteccionesEnRegistros(List<Deteccion> deteccionesEnRegistros) {
        this.deteccionesEnRegistros = deteccionesEnRegistros;
    }

    public void setEstadosEnRegistros(Estado[] estadosEnRegistros) {
        this.estadosEnRegistros = estadosEnRegistros;
    }

    public void setAreasSeleccionadas(String[] areasSeleccionadas) {
        this.sesionUsuario.setAreasSeleccionadas(areasSeleccionadas);
    }

    public void setDeteccionesSeleccionadas(String[] deteccionesSeleccionadas) {
        sesionUsuario.setDeteccionesSeleccionadas(deteccionesSeleccionadas);
    }

    public void setEstadosSeleccionados(Estado[] estadosSeleccionados) {
        sesionUsuario.setEstadosSeleccionados(estadosSeleccionados);
    }

    public void setCodificacionesSeleccionadas(String[] codificacionesSeleccionadas) {
        sesionUsuario.setCodificacionesSeleccionadas(codificacionesSeleccionadas);
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setStrFechaInicial(String strFechaInicial) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        try {
            this.fechaInicial = sdf.parse(strFechaInicial);
        } catch (ParseException ex) {
        }
        this.strFechaInicial = strFechaInicial;
    }

    public void setStrFechaFinal(String strFechaFinal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        try {
            this.fechaFinal = sdf.parse(strFechaFinal);
        } catch (ParseException ex) {
        }
        this.strFechaFinal = strFechaFinal;
    }

    public void setTipoDeAccion(TipoAccion tipoDeAccion) {
        this.tipoDeAccion = tipoDeAccion;
    }

    public void setAccionSeleccionada(Accion accion) {
        this.accionSeleccionada = accion;
    }

    public void setEsBusqueda(boolean esBusqueda) {
        this.esBusqueda = esBusqueda;
    }

    public void setTextoBusqueda(String textoBusqueda) {
        this.textoBusqueda = textoBusqueda;
    }

    public void setFiltrosAplicados(List<String> filtrosAplicados) {
        this.sesionUsuario.setFiltrosAplicados(filtrosAplicados);
    }

    //</editor-fold>
    //**********************************************************************
    // Metodos de la clase
    //**********************************************************************
    /**
     * Obtiene los ids como enteros del array de strings.
     *
     * @param arrayIds
     * @return
     */
    private List<Integer> ObtenerIdsSeleccionados(String[] arrayIds) {
        List<Integer> ids = new ArrayList<>();
        for (String id : arrayIds) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    public void SeleccionarAccion(int id) {
        accionSeleccionada = ListaAcciones.stream().filter((Accion accion) -> accion.getId() == id).findFirst().get();
        verActividades.setAccionSeleccionada(accionSeleccionada);
    }

    public void quitarFiltros() {
        sesionUsuario.removeFiltro("estados");
        sesionUsuario.removeFiltro("codificaciones");
        sesionUsuario.removeFiltro("detecciones");
        sesionUsuario.removeFiltro("areas");
        sesionUsuario.removeFiltro("busqueda");
        sesionUsuario.removeFiltro("fechas");

        ResetFechasAcciones();
        ResetListasAreas();
        ResetListasDeteccion();
        ResetListasEstado();
        ResetListasCodificacion();
        resetTexto();

        FiltrarAcciones();
    }

    public void quitarFiltro(String filtro) {
        switch (filtro) {
            case "estados" ->
                this.quitarFiltroPorEstado();
            case "codificaciones" ->
                this.quitarFiltroPorCodificacion();
            case "detecciones" ->
                this.quitarFiltroPorDeteccion();
            case "areas" ->
                this.quitarFiltroPorArea();
            case "busqueda" ->
                this.quitarFiltroTexto();
            default ->
                this.quitarFiltros();
        }
    }

    //  Inicializacion
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        verActividades = context.getApplication().evaluateExpressionGet(context, "#{modalVerActividades}", ModalVerActividades.class);
        int id = 0;
        tipoDeAccion = TipoAccion.valueOf(request.getParameter("tipo"));
        try {
            id = Integer.parseInt(request.getParameter("buscarid"));
        } catch (NumberFormatException ex) {
        }

        // Paginacion
        PaginaActual = 1;

        // llenar la lista de acciones dependiendo si se esta filtrando por Id
        ListaAcciones = new ArrayList<>();
        ListaCompletaAcciones = new ArrayList<>();
        if (id > 0) {
            Accion accion = fLectura.getAccion(id);
            if (accion != null) {
                ListaCompletaAcciones.add(accion);
                cambiarPagina(true, PaginaActual);
                esBusqueda = true;
            }
        } else {
            if (!sesionUsuario.getFiltrosAplicados().isEmpty()) {
                if (tipoDeAccion == sesionUsuario.getTipoAccionListada()) {
                    cambiarPagina(true, PaginaActual);
                } else {
                    sesionUsuario.getFiltrosAplicados().clear();
                    sesionUsuario.setTipoAccionListada(tipoDeAccion);
                    cambiarPagina(false, PaginaActual);
                }
            } else {
                sesionUsuario.setTipoAccionListada(tipoDeAccion);
                cambiarPagina(false, PaginaActual);
            }
        }
    }

    public void cambiarPagina(boolean conFiltros, int numero) {
        cargarAcciones();
        if (conFiltros) {
            PaginaActual = numero;
            FiltrarAcciones();
        } else {
            if (!ListaCompletaAcciones.isEmpty()) {
                ListaCompletaAcciones.sort(Comparator.reverseOrder());
            }
            cargarPagina(ListaCompletaAcciones);
            // reset datos filtros
            ResetFechasAcciones();
            ResetListasAreas();
            ResetListasDeteccion();
            ResetListasEstado();
            ResetListasCodificacion();
        }
    }

    private void cargarAcciones() {
        switch (tipoDeAccion) {
            case CORRECTIVA ->
                ListaCompletaAcciones = fLectura.listarAccionesCorrectivas()
                        .stream()
                        .filter(a -> a.getEmpresaAccion().getId() == sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId())
                        .collect(Collectors.toList());
            case PREVENTIVA ->
                ListaCompletaAcciones = fLectura.listarAccionesPreventivas()
                        .stream()
                        .filter(a -> a.getEmpresaAccion().getId() == sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId())
                        .collect(Collectors.toList());
            case MEJORA ->
                ListaCompletaAcciones = fLectura.listarAccionesMejoras()
                        .stream()
                        .filter(a -> a.getEmpresaAccion().getId() == sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId())
                        .collect(Collectors.toList());
        }
    }

    private void cargarPagina(List<Accion> acciones) {
        CantidadPaginas = Presentacion.calcularCantidadPaginas(acciones.size(), MAX_ITEMS);
        // Corregir el numero de pagina en caso que se apliquen filtros en una página diferente de 1 y luego de filtrar
        // en esa página no hayan datos para mostrar.
        if (PaginaActual > CantidadPaginas) {
            PaginaActual = 1;
        }
        ListaAcciones = Presentacion.cargarPagina(PaginaActual, MAX_ITEMS, acciones);
        ListaAcciones.sort(Comparator.reverseOrder());
    }

    public void pdfteaListado() {
        PdfteameListado pdf = new PdfteameListado(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario());
        List<Accion> accionesFiltradas = obtenerAccionesParaExportar();

        StringBuilder titulo = new StringBuilder("Listado de ");
        switch (tipoDeAccion) {
            case CORRECTIVA ->
                titulo.append("acciones correctivas");
            case PREVENTIVA ->
                titulo.append("acciones Preventivas");
            case MEJORA ->
                titulo.append("Oportunidades de mejora");
        }
        if (!accionesFiltradas.isEmpty()) {
            pdf.ExportarListado("Listado de Acciones (tipo " + tipoDeAccion + ").pdf", titulo.toString().toUpperCase(), accionesFiltradas);
        }

    }

    public void pdfteaPlanAccion(int id) {
        PdfteameListado pdf = new PdfteameListado(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario());
        Accion accionFiltrada = ListaCompletaAcciones.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
        if (accionFiltrada != null) {
            List<Accion> accionesPlan = fLectura.listarAcciones().stream()
                    .filter(a -> a.getFechaDeteccion().equals(accionFiltrada.getFechaDeteccion()) && a.getDeteccionAccion() == accionFiltrada.getDeteccionAccion())
                    .collect(Collectors.toList());

            accionesPlan.sort(Comparator.comparing(a -> a.getAreaAccion()));
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
            StringBuilder strTitulo = new StringBuilder();
            StringBuilder strIntroduccion = new StringBuilder();

            strTitulo.append("PLAN DE ACCION: ")
                    .append(accionFiltrada.getDeteccionAccion().getNombre())
                    .append(" - ")
                    .append(df.format(accionFiltrada.getFechaDeteccion()));
            strIntroduccion.append("En respuesta a los hallazgos detectados por ")
                    .append(accionFiltrada.getDeteccionAccion().getNombre())
                    .append(" en el dia ")
                    .append(df.format(accionFiltrada.getFechaDeteccion()))
                    .append(" se presenta el siguiente Plan de Accion:");

            df = new SimpleDateFormat("dd-MM-yy");
            pdf.ExportarPlanAccion("Plan De Accion " + accionFiltrada.getDeteccionAccion().getNombre() + " (" + df.format(accionFiltrada.getFechaDeteccion()) + ").pdf",
                    strTitulo.toString(), accionesPlan, strIntroduccion.toString(), sesionUsuario.getUsuarioLogueado().getEmpresaUsuario());
        }
    }

    public void pdfteaRegistro(int id, boolean excluirComprobaciones) {
        PdfteameRegistro pdf = new PdfteameRegistro(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario());
        Accion accionFiltrada = ListaCompletaAcciones.stream().filter(a -> a.getId() == id).findFirst().orElse(null);

        if (accionFiltrada != null) {
            String titulo = "MAYM Accion "
                    + accionFiltrada.getClass().getSimpleName()
                    + " Id: "
                    + accionFiltrada.getId()
                    + " - "
                    + accionFiltrada.getDeteccionAccion().getNombre();

            pdf.ExportarRegistro("Registro MAYM Id " + accionFiltrada.getId() + ".pdf", titulo.toUpperCase(), accionFiltrada, excluirComprobaciones);
        }
    }

    public void excelsea() {
        Excelsea excel = new Excelsea();
        List<Accion> accionesFiltradas = obtenerAccionesParaExportar();
        if (!accionesFiltradas.isEmpty()) {
            StringBuilder titulo = new StringBuilder("Listado de ");
            switch (tipoDeAccion) {
                case CORRECTIVA ->
                    titulo.append("acciones correctivas");
                case PREVENTIVA ->
                    titulo.append("acciones Preventivas");
                case MEJORA ->
                    titulo.append("Oportunidades de mejora");
            }
            excel.ExportarLibroExcel(accionesFiltradas, titulo.toString(), false);
        }
    }

    private List<Accion> obtenerAccionesParaExportar() {
        List<Accion> accionesFiltradas = ListaCompletaAcciones;
        for (String filtro : sesionUsuario.getFiltrosAplicados()) {
            switch (filtro) {
                case "estados" ->
                    accionesFiltradas = filtrarPorEstado(accionesFiltradas);
                case "areas" ->
                    accionesFiltradas = filtrarPorArea(accionesFiltradas);
                case "codificaciones" ->
                    accionesFiltradas = filtrarPorCodificacion(accionesFiltradas);
                case "detecciones" ->
                    accionesFiltradas = filtrarPorDeteccion(accionesFiltradas);
                case "busqueda" ->
                    accionesFiltradas = filtrarTexto(accionesFiltradas);
                case "fechas" ->
                    accionesFiltradas = filtrarPorFechas(accionesFiltradas);
                default ->
                    accionesFiltradas = ListaCompletaAcciones;
            }
        }
        return accionesFiltradas;
    }

    public void excelseaConActividades() {
        Excelsea excel = new Excelsea();
        List<Accion> accionesFiltradas = obtenerAccionesParaExportar();
        if (!accionesFiltradas.isEmpty()) {
            StringBuilder titulo = new StringBuilder("Listado de ");
            switch (tipoDeAccion) {
                case CORRECTIVA ->
                    titulo.append("acciones correctivas");
                case PREVENTIVA ->
                    titulo.append("acciones Preventivas");
                case MEJORA ->
                    titulo.append("Oportunidades de mejora");
            }
            excel.ExportarLibroExcel(accionesFiltradas, titulo.toString(), true);
        }
    }
}
