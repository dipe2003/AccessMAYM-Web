/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.acciones.general;

import com.dperez.maym.web.configuraciones.ModalDetecciones;
import com.dperez.maym.web.herramientas.CargarArchivo;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.herramientas.Evento;
import com.dperez.maymweb.herramientas.ProgramadorEventos;
import com.dperez.maymweb.herramientas.TipoEvento;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.comprobaciones.Comprobacion;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.acciones.adjunto.Adjunto;
import com.dperez.maymweb.modelo.acciones.adjunto.TipoAdjunto;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.herramientas.EventoAccion;
import com.dperez.maymweb.herramientas.EventoActividad;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.producto.Producto;
import com.dperez.maymweb.modelo.usuario.Responsable;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

@Named
@ViewScoped
public class EditarAccion implements Serializable {

    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    private FacadeDatos fDatos;

    @Inject
    private CargarArchivo cArchivo;
    @Inject
    private ProgramadorEventos pEventos;
    @Inject
    private SesionUsuario sesion;

    //<editor-fold desc="Atributos">
    private int IdAccionSeleccionada;
    private Accion AccionSeleccionada;
    private TipoAccion tipoDeAccion;

    private ModalDetecciones modalDetecciones;

    private Date FechaDeteccion;
    private String strFechaDeteccion;
    private String Descripcion;
    private String Referencias;
    private String AnalisisCausa;
    private String Cliente;

    private String TituloAdjunto;
    private List<Adjunto> listaAdjuntos;
    private Part ArchivoAdjunto;
    private int totalImagenes;
    private int totalVideos;
    private int totalOtros;

    private TipoDeteccion[] TiposDeteccion;
    private TipoDeteccion TipoDeDeteccionSeleccionada;
    private List<Deteccion> ListaDetecciones;
    private Integer DeteccionSeleccionada;

    private List<Area> ListaAreasSectores;
    private Integer AreaSectorAccionSeleccionada;

    private List<Codificacion> ListaCodificaciones;
    private Integer CodificacionSeleccionada;

    private List<Producto> ListaProductosAfectados;

    private List<Actividad> actividades;

    //comprobaciones
    private int ResponsableImplementacion;
    private int ResponsableEficacia;
    private Date FechaEstimadaImplementacion;
    private String strFechaEstimadaImplementacion;
    private Date FechaEstimadaVerificacion;
    private String strFechaEstimadaVerificacion;

    private Comprobacion ComprobacionImplementacion;
    private Comprobacion ComprobacionEficacia;

    private List<Responsable> ListaResponsables;

    // Productos
    private int IdProductoAfectado;
    private String NombreProductoAfectado;
    private String DatosProductoAfectado;

    //</editor-fold>
    //<editor-fold desc="Getters">
    public Accion getAccionSeleccionada() {
        return AccionSeleccionada;
    }

    public int getIdAccionSeleccionada() {
        return IdAccionSeleccionada;
    }

    public TipoAccion getTipoDeAccion() {
        return this.tipoDeAccion;
    }

    public Date getFechaDeteccion() {
        return FechaDeteccion;
    }

    public String getStrFechaDeteccion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaDeteccion == null) {
            return this.strFechaDeteccion;
        } else {
            return fDate.format(FechaDeteccion);
        }
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getReferencias() {
        return Referencias;
    }

    public String getAnalisisCausa() {
        return AnalisisCausa;
    }

    public String getCliente() {
        return Cliente;
    }

    public String getTituloAdjunto() {
        return this.TituloAdjunto;
    }

    public Part getArchivoAdjunto() {
        return ArchivoAdjunto;
    }

    public List<Adjunto> getListaAdjuntos() {
        return listaAdjuntos;
    }

    public List<Codificacion> getListaCodificaciones() {
        return this.ListaCodificaciones;
    }

    public Integer getCodificacionSeleccionada() {
        return CodificacionSeleccionada;
    }

    public TipoDeteccion getTipoDeDeteccionSeleccionada() {
        return this.TipoDeDeteccionSeleccionada;
    }

    public TipoDeteccion[] getTiposDeteccion() {
        return this.TiposDeteccion;
    }

    public List<Deteccion> getListaDetecciones() {
        return this.ListaDetecciones;
    }

    public Integer getDeteccionSeleccionada() {
        return this.DeteccionSeleccionada;
    }

    public List<Area> getListaAreasSectores() {
        return this.ListaAreasSectores;
    }

    public Integer getAreaSectorAccionSeleccionada() {
        return AreaSectorAccionSeleccionada;
    }

    public List<Producto> getListaProductosAfectados() {
        return this.ListaProductosAfectados;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    //comprobaciones
    public int getResponsableImplementacion() {
        return ResponsableImplementacion;
    }

    public int getResponsableEficacia() {
        return ResponsableEficacia;
    }

    public Date getFechaEstimadaImplementacion() {
        return FechaEstimadaImplementacion;
    }

    public String getStrFechaEstimadaImplementacion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaEstimadaImplementacion == null) {
            return this.strFechaEstimadaImplementacion;
        } else {
            return fDate.format(FechaEstimadaImplementacion);
        }
    }

    public Date getFechaEstimadaVerificacion() {
        return FechaEstimadaVerificacion;
    }

    public String getStrFechaEstimadaVerificacion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaEstimadaVerificacion == null) {
            return this.strFechaEstimadaVerificacion;
        } else {
            return fDate.format(FechaEstimadaVerificacion);
        }
    }

    public Comprobacion getComprobacionImplementacion() {
        return ComprobacionImplementacion;
    }

    public Comprobacion getComprobacionEficacia() {
        return ComprobacionEficacia;
    }

    public List<Responsable> getListaResponsables() {
        return ListaResponsables;
    }

    public int getIdProductoAfectado() {
        return IdProductoAfectado;
    }

    public String getNombreProductoAfectado() {
        return NombreProductoAfectado;
    }

    public String getDatosProductoAfectado() {
        return DatosProductoAfectado;
    }

    public int getTotalImagenes() {
        return totalImagenes;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public int getTotalOtros() {
        return totalOtros;
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setIdAccionSeleccionada(int IdAccionSeleccionada) {
        this.IdAccionSeleccionada = IdAccionSeleccionada;
    }

    public void setTipoDeAccion(TipoAccion tipo) {
        this.tipoDeAccion = tipo;
    }

    public void setFechaDeteccion(Date FechaDeteccion) {
        this.FechaDeteccion = FechaDeteccion;
    }

    public void setStrFechaDeteccion(String strFechaDeteccion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.FechaDeteccion = sdf.parse(strFechaDeteccion);
        } catch (ParseException ex) {
        }
        this.strFechaDeteccion = strFechaDeteccion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setReferencias(String Referencias) {
        this.Referencias = Referencias;
    }

    public void setAnalisisCausa(String AnalisisCausa) {
        this.AnalisisCausa = AnalisisCausa;
    }

    public void setCliente(String Cliente) {
        this.Cliente = Cliente;
    }

    public void setTituloAdjunto(String TituloAdjunto) {
        this.TituloAdjunto = TituloAdjunto;
    }

    public void setArchivoAdjunto(Part ArchivoAdjunto) {
        this.ArchivoAdjunto = ArchivoAdjunto;
    }

    public void setListaAdjuntos(List<Adjunto> listaAdjuntos) {
        this.listaAdjuntos = listaAdjuntos;
    }

    public void setListaCodificaciones(List<Codificacion> ListaCodificaciones) {
        this.ListaCodificaciones = ListaCodificaciones;
    }

    public void setCodificacionSeleccionada(Integer CodificacionSeleccionada) {
        this.CodificacionSeleccionada = CodificacionSeleccionada;
    }

    public void setTipoDeDeteccionSeleccionada(TipoDeteccion TipoDeteccion) {
        this.TipoDeDeteccionSeleccionada = TipoDeteccion;
    }

    public void setTiposDeteccion(TipoDeteccion[] TiposDeteccion) {
        this.TiposDeteccion = TiposDeteccion;
    }

    public void setListaDetecciones(List<Deteccion> ListaDetecciones) {
        this.ListaDetecciones = ListaDetecciones;
    }

    public void setDeteccionSeleccionada(Integer DeteccionSeleccionada) {
        this.DeteccionSeleccionada = DeteccionSeleccionada;
    }

    public void setListaAreasSectores(List<Area> ListaAreasSectores) {
        this.ListaAreasSectores = ListaAreasSectores;
    }

    public void setAreaSectorAccionSeleccionada(Integer AreaSectorAccionSeleccionada) {
        this.AreaSectorAccionSeleccionada = AreaSectorAccionSeleccionada;
    }

    public void setListaProductosAfectados(List<Producto> ListaProductosAfectados) {
        this.ListaProductosAfectados = ListaProductosAfectados;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    // comprobaciones
    public void setResponsableEficacia(int ResponsableEficacia) {
        this.ResponsableEficacia = ResponsableEficacia;
    }

    public void setResponsableImplementacion(int ResponsableImplementacion) {
        this.ResponsableImplementacion = ResponsableImplementacion;
    }

    public void setFechaEstimadaImplementacion(Date FechaEstimadaImplementacion) {
        this.FechaEstimadaImplementacion = FechaEstimadaImplementacion;
    }

    public void setStrFechaEstimadaImplementacion(String strFechaEstimadaImplementacion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.FechaEstimadaImplementacion = sdf.parse(strFechaEstimadaImplementacion);
        } catch (ParseException ex) {
        }
        this.strFechaEstimadaImplementacion = strFechaEstimadaImplementacion;
    }

    public void setFechaEstimadaVerificacion(Date FechaEstimadaVerificacion) {
        this.FechaEstimadaVerificacion = FechaEstimadaVerificacion;
    }

    public void setStrFechaEstimadaVerificacion(String strFechaEstimadaVerificacion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.FechaEstimadaVerificacion = sdf.parse(strFechaEstimadaVerificacion);
        } catch (ParseException ex) {
        }
        this.strFechaEstimadaVerificacion = strFechaEstimadaVerificacion;
    }

    public void setComprobacionImplementacion(Comprobacion ComprobacionImplementacion) {
        this.ComprobacionImplementacion = ComprobacionImplementacion;
    }

    public void setComprobacionEficacia(Comprobacion ComprobacionEficacia) {
        this.ComprobacionEficacia = ComprobacionEficacia;
    }

    public void setListaResponsables(List<Responsable> ListaResponsables) {
        this.ListaResponsables = ListaResponsables;
    }

    public void setIdProductoAfectado(int IdProductoAfectado) {
        this.IdProductoAfectado = IdProductoAfectado;
    }

    public void setNombreProductoAfectado(String NombreProductoAfectado) {
        this.NombreProductoAfectado = NombreProductoAfectado;
    }

    public void setDatosProductoAfectado(String DatosProductoAfectado) {
        this.DatosProductoAfectado = DatosProductoAfectado;
    }

    public void setTotalImagenes(int totalImagenes) {
        this.totalImagenes = totalImagenes;
    }

    public void setTotalVideos(int totalVideos) {
        this.totalVideos = totalVideos;
    }

    public void setTotalOtros(int totalOtros) {
        this.totalOtros = totalOtros;
    }

    //</editor-fold>
    //<editor-fold desc="Metodos">
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        fDatos = new FacadeDatos();
        fAdmin = new FacadeAdministrador();

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ListaProductosAfectados = new ArrayList<>();

        IdAccionSeleccionada = 0;
        try {
            IdAccionSeleccionada = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException nEx) {
        };

        if (IdAccionSeleccionada != 0) {
            AccionSeleccionada = fLectura.GetAccion(IdAccionSeleccionada);
            tipoDeAccion = TipoAccion.valueOf(AccionSeleccionada.getClass().getSimpleName().toUpperCase());
            FechaDeteccion = AccionSeleccionada.getFechaDeteccion();
            Descripcion = AccionSeleccionada.getDescripcion();
            Referencias = AccionSeleccionada.getReferencias();
            AnalisisCausa = AccionSeleccionada.getAnalisisCausa();
            ListaProductosAfectados = AccionSeleccionada.getProductosInvolucrados();

            //  Codificaciones
            ListaCodificaciones = fLectura.listarCodificaciones().stream()
                    .sorted()
                    .collect(Collectors.toList());

            CodificacionSeleccionada = AccionSeleccionada.getCodificacionAccion().getId();

            //  Detecciones
            modalDetecciones = context.getApplication().evaluateExpressionGet(context, "#{modalDetecciones}", ModalDetecciones.class);
            TiposDeteccion = TipoDeteccion.values();

            TipoDeDeteccionSeleccionada = AccionSeleccionada.getDeteccionAccion().getTipoDeDeteccion();
            DeteccionSeleccionada = AccionSeleccionada.getDeteccionAccion().getId();
            actualizarDeteccion();

            // Actividades: Medidas Correctivas y Preventivas
            actividades = AccionSeleccionada.getActividadesDeAccion();

            // Areas Sectores
            ListaAreasSectores = fLectura.listarAreas().stream()
                    .sorted()
                    .collect(Collectors.toList());
            AreaSectorAccionSeleccionada = AccionSeleccionada.getAreaAccion().getId();

            listaAdjuntos = new ArrayList<>();
            actualizarListaAdjuntos();

            // Comprobaciones
            //  Llenar los responsables
            ListaResponsables = fLectura.listarResponsables(true);
            ListaResponsables.sort(Comparator.naturalOrder());

            ComprobacionImplementacion = AccionSeleccionada.getComprobacionImplementacion();
            ComprobacionEficacia = AccionSeleccionada.getComprobacionEficacia();

            // para editar
            if (ComprobacionImplementacion != null && ComprobacionImplementacion.getFechaEstimada() != null) {
                this.setFechaEstimadaImplementacion(ComprobacionImplementacion.getFechaEstimada());
            }
            if (ComprobacionImplementacion != null && ComprobacionImplementacion.getResponsableComprobacion() != null) {
                this.ResponsableImplementacion = ComprobacionImplementacion.getResponsableComprobacion().getId();
            }
            if (ComprobacionEficacia != null && ComprobacionEficacia.getFechaEstimada() != null) {
                this.setFechaEstimadaVerificacion(ComprobacionEficacia.getFechaEstimada());
            }
            if (ComprobacionEficacia != null && ComprobacionEficacia.getResponsableComprobacion() != null) {
                this.ResponsableEficacia = ComprobacionEficacia.getResponsableComprobacion().getId();
            }
        }
    }

    private void actualizarListaAdjuntos() {
        Accion accionSeguida = fLectura.GetAccion(IdAccionSeleccionada);
        if (!accionSeguida.getAdjuntosDeAccion().isEmpty()) {
            listaAdjuntos = accionSeguida.getAdjuntosDeAccion();
            actualizarTotalesAdjuntos();
        }
    }

    private void actualizarListaProductos() {
        Accion accionSeguida = fLectura.GetAccion(IdAccionSeleccionada);
        if (!accionSeguida.getProductosInvolucrados().isEmpty()) {
            ListaProductosAfectados = accionSeguida.getProductosInvolucrados();
        }
    }

    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de
     * deteccion.
     */
    public void actualizarDeteccion() {
        modalDetecciones.setTipoDeDeteccionSeleccionada(TipoDeDeteccionSeleccionada);
        modalDetecciones.actualizarDeteccion();
        ListaDetecciones = modalDetecciones.getListaDetecciones();
    }

    /**
     * Carga el adjunto en la lista de adjuntos. Deja vacios los campos para un
     * nuevo adjunto.
     */
    public void agregarAdjunto() {
        String msj = "";
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (this.TituloAdjunto.isEmpty()) {
            msj = "El Titulo está vacío.";
        } else {
            if (this.listaAdjuntos.stream()
                    .anyMatch(a -> a.getTitulo().toLowerCase().equals(TituloAdjunto.toLowerCase()))) {
                msj = "Ya existe un adjunto con ese nombre.";
                ctx.addMessage("form_accion_modal:input-adjunto", new FacesMessage(SEVERITY_ERROR, "No se pudo cargar.", msj));
                ctx.renderResponse();
            } else {
                try {
                    String datosAdjunto[] = cArchivo.guardarArchivo("Accion_" + String.valueOf(IdAccionSeleccionada), ArchivoAdjunto, TituloAdjunto, sesion.getEmpresa().getNombreDeArchivo());
                    // datosAdjunto[0]: ubicacion | datosAdjunto[1]: extension
                    if (!datosAdjunto[0].isEmpty()) {
                        TipoAdjunto tipoAdjunto = TipoAdjunto.IMAGEN;
                        String extension = datosAdjunto[1];
                        switch (extension.toLowerCase().trim()) {
                            case "jpeg", "jpg", "png", "gif" ->
                                tipoAdjunto = TipoAdjunto.IMAGEN;
                            case "mp4", "m4a", "m4b" ->
                                tipoAdjunto = TipoAdjunto.VIDEO;
                            default ->
                                tipoAdjunto = tipoAdjunto.DOCUMENTO;
                        }
                        if ((fDatos.agregarArchivoAdjunto(IdAccionSeleccionada, TituloAdjunto, datosAdjunto[0], tipoAdjunto)) != -1) {
                            actualizarListaAdjuntos();
                            this.TituloAdjunto = new String();
                            this.ArchivoAdjunto = null;
                        } else {
                            cArchivo.BorrarArchivo(datosAdjunto[0]);
                        }
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                    msj = "No se pudo cargar, comprueba el titulo del adjunto o el archivo.";
                    ctx.addMessage("form_accion_modal:input-adjunto", new FacesMessage(SEVERITY_ERROR, "No se pudo cargar.", msj));
                    ctx.renderResponse();
                }
            }
        }
    }

    /**
     * Quita el adjunto de la lista de adjuntos.
     *
     * @param IdAdjunto
     * @throws IOException
     */
    public void quitarAdjunto(int IdAdjunto) throws IOException {
        try {
            Adjunto adjunto = this.listaAdjuntos.stream()
                    .filter(a -> a.getIda() == IdAdjunto)
                    .findFirst().get();
            if ((fDatos.removerAdjunto(IdAccionSeleccionada, IdAdjunto)) != -1) {
                if (adjunto != null) {
                    cArchivo.BorrarArchivo(adjunto.getUbicacion());
                    this.listaAdjuntos.remove(adjunto);
                    actualizarTotalesAdjuntos();
                }
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Quita la actividad seleccionada de la accion. Muestra un mensaje si no se
     * pudo quitar de lo contrario redidirge a la vista de edicion de la accion.
     *
     * @param IdActividad
     * @throws IOException
     */
    public void quitarActividad(int IdActividad) throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Actividad actividad = AccionSeleccionada.findActividad(IdActividad);
        if (fDatos.removerActividad(IdAccionSeleccionada, IdActividad) == -1) {
            // Si no se actualizo muestra mensaje de error.
            ctx.addMessage("form_accion:guardar_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo editar la Accion", "No se pudo editar la Accion"));
            ctx.renderResponse();
        } else {
            // remover el evento del programador de tareas.
            Evento eventoActividad = new EventoActividad(actividad);
            if (pEventos.ExisteEventoActividad(eventoActividad)) {
                pEventos.RemoverEventoActividad(eventoActividad);
            }
            // Si la eliminacion se realizo correctamente redirige a lista de acciones.
            String url = ctx.getExternalContext().getRequestContextPath();
            ctx.getExternalContext().redirect(url + "/Views/Acciones/General/EditarAccion.xhtml?id=" + IdAccionSeleccionada);
        }
    }

    /**
     * Actualiza la accion correctiva con los datos nuevos. Si se muestra
     * mensaje de confirmacion.
     *
     * @throws java.io.IOException
     */
    public void editarAccion() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        // actualizar accion
        if (Referencias == null) {
            Referencias = new String();
        }
        if (fDatos.editarAccion(IdAccionSeleccionada, FechaDeteccion, Descripcion, Referencias, AnalisisCausa,
                AreaSectorAccionSeleccionada, DeteccionSeleccionada, CodificacionSeleccionada) == -1) {
            // Si no se actualizo muestra mensaje de error.
            ctx.addMessage("form_accion:guardar_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo editar la Accion", "No se pudo editar la Accion"));
            ctx.renderResponse();
        } else {
            if (!this.actividades.isEmpty()) {
                fDatos.setComprobacionImplementacion(this.FechaEstimadaImplementacion, this.ResponsableImplementacion, IdAccionSeleccionada);
                if (AccionSeleccionada.getComprobacionImplementacion() != null) {
                    ajustarEvento(TipoEvento.IMPLEMENTACION_ACCION);
                }
                fDatos.setVerificacionEficacia(this.FechaEstimadaVerificacion, this.ResponsableEficacia, IdAccionSeleccionada);
                if (AccionSeleccionada.getComprobacionEficacia() != null) {
                    ajustarEvento(TipoEvento.VERIFICACION_EFICACIA);
                }
            }
        }
        // Si la actualizacion se realizo correctamente redirige a lista de acciones.
        ctx.addMessage("form_accion:guardar_accion", new FacesMessage(SEVERITY_INFO, "Los datos se guardaron.", "Los datos se guardaron."));
        ctx.renderResponse();
    }

    /**
     * Actualiza la accion correctiva con los datos nuevos. Si se muestra
     * mensaje de confirmacion.
     *
     * @throws java.io.IOException
     */
    public void guardarAnalisisCausa() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        // actualizar accion
        if (AnalisisCausa == null || AnalisisCausa.isBlank()) {
            ctx.addMessage("form_accion:guardar_analisis_causa", new FacesMessage(SEVERITY_ERROR, "No se pudo guardar.", "Analisis de Causa no puede estar vacío."));
            ctx.renderResponse();
        } else {
            if (fDatos.guardarAnalisisCausa(IdAccionSeleccionada, AnalisisCausa) == -1) {
                // Si no se actualizo muestra mensaje de error.
                ctx.addMessage("form_accion:guardar_analisis_causa", new FacesMessage(SEVERITY_ERROR, "No se pudo guardar.", "No se pudo guardar el analisis de causa de la Accion"));
                ctx.renderResponse();
            } else {
                // Si la actualizacion se realizo correctamente redirige a lista de acciones.More actions
                ctx.addMessage("form_accion:guardar_analisis_causa", new FacesMessage(SEVERITY_INFO, "Los datos se guardaron.", "Los datos se guardaron."));
                ctx.renderResponse();
            }
        }
    }

    /**
     * *
     * Comprueba si el evento existe. Si existe se remplaza, de lo contrario se
     * crea uno nuevo.
     *
     * @param tipo
     */
    private void ajustarEvento(TipoEvento tipo) {
        Evento eventoAccion = new EventoAccion(tipo, AccionSeleccionada);
        if (pEventos.ExisteEventoAccion(eventoAccion)) {
            pEventos.RemoverEventoAccion(eventoAccion);
        }
        switch (tipo) {
            case IMPLEMENTACION_ACCION ->
                pEventos.ProgramarEvento(FechaEstimadaImplementacion, eventoAccion);
            default ->
                pEventos.ProgramarEvento(FechaEstimadaVerificacion, eventoAccion);
        }
    }

    /**
     * Elimina la accion de la base de datos. Se eliminan todos los datos
     * relacionados (actividades, adjuntos, comprobaciones y productos)
     *
     * @throws java.io.IOException
     */
    public void eliminarAccion() throws IOException {
        if (fAdmin.eliminarAccion(IdAccionSeleccionada) == -1) {
            // Si no se elimino muestra mensaje de error.
            FacesContext.getCurrentInstance().addMessage("form_accion:eliminar_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar la Accion", "No se pudo eliminar la Accion"));
            FacesContext.getCurrentInstance().renderResponse();
        } else {
            // Eliminar todos los archivos adjuntos del disco.
            if (!listaAdjuntos.isEmpty()) {
                listaAdjuntos.forEach((adjunto) -> {
                    cArchivo.BorrarArchivo(adjunto.getUbicacion());
                });
            }
            // Eliminar Eventos
            pEventos.RemoverEventos(IdAccionSeleccionada);
            // Si la eliminacion se realizo correctamente redirige a lista de acciones.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/Views/Acciones/General/ListarAcciones.xhtml?tipo=" + tipoDeAccion);
        }
    }

    /**
     * Agrega un nuevo producto afectado a la lista de productos afectados para
     * ser persistidos durante la creacion de la accion correctiva. Si el nombre
     * del producto ya existe se sustituye Si el nombre o el datos estan vacios
     * no se crea y se muestra el mensaje correspondiente. PRE: debe existir una
     * accion correctiva ya creada
     */
    public void nuevoProductoAfectado() {
        if (NombreProductoAfectado != null && DatosProductoAfectado != null) {
            if (NombreProductoAfectado.isEmpty() || DatosProductoAfectado.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nuevo_producto", new FacesMessage(SEVERITY_FATAL, "No se pudo agregar producto", "No se pudo agregar producto"));
                FacesContext.getCurrentInstance().renderResponse();
            } else {
                if (fDatos.agregarProductoInvolucrado(IdAccionSeleccionada, NombreProductoAfectado, DatosProductoAfectado) > 0) {
                    actualizarListaProductos();
                    limpiarModalProducto();
                    FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nuevo_producto", new FacesMessage(SEVERITY_INFO, "El producto fue agregado.", "El producto fue agregado."));
                    FacesContext.getCurrentInstance().renderResponse();
                }
            }
        }
    }

    /**
     * Carga los datos del producto en los campos del formulario.Lo remueve de
     * la lista.
     *
     * @param idProducto
     */
    public void cargarDatosProducto(int idProducto) {
        IdProductoAfectado = idProducto;
        Producto prod = ListaProductosAfectados.stream()
                .filter(p -> p.getId() == idProducto)
                .findFirst()
                .orElse(null);
        if (prod != null) {
            this.NombreProductoAfectado = prod.getNombre();
            this.DatosProductoAfectado = prod.getDatos();
        }
    }

    /**
     * Guarda los datos del producto de los campos del formulario. Remueve de la
     * lista los datos del producto anterior. PRE: debe existir una accion
     * correctiva ya creada
     */
    public void guardarCambiosProducto() {
        Producto prod = ListaProductosAfectados.stream()
                .filter(p -> p.getId() == IdProductoAfectado)
                .findFirst()
                .orElse(null);
        if (prod != null) {
            if (!NombreProductoAfectado.equals(prod.getNombre()) || !DatosProductoAfectado.equals(prod.getDatos())) {
                if (fDatos.agregarProductoInvolucrado(IdAccionSeleccionada, NombreProductoAfectado, DatosProductoAfectado) > 0) {
                    limpiarModalProducto();
                    // remover producto anterior
                    fDatos.removerProductoInvolucrado(IdAccionSeleccionada, prod.getId());
                    actualizarListaProductos();
                    FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_editar_producto", new FacesMessage(SEVERITY_INFO, "El producto fue guardado.", "El producto fue guardado."));
                    FacesContext.getCurrentInstance().renderResponse();
                }
            } else {
                FacesContext.getCurrentInstance().renderResponse();
            }
        }
    }

    public void quitarProductoInvolucrado(int idProducto) {
        if (fDatos.removerProductoInvolucrado(IdAccionSeleccionada, idProducto) == IdAccionSeleccionada) {
            actualizarListaProductos();
            FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_editar_producto", new FacesMessage(SEVERITY_INFO, "El producto fue quitado.", "El producto fue quitado."));
            FacesContext.getCurrentInstance().renderResponse();
        } else {
            FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_editar_producto", new FacesMessage(SEVERITY_FATAL, "El producto no fue quitado.", "El producto no fue quitado."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void limpiarModalProducto() {
        this.NombreProductoAfectado = new String();
        this.DatosProductoAfectado = new String();
        this.IdProductoAfectado = 0;
    }

    private void actualizarTotalesAdjuntos() {
        totalImagenes = Long.valueOf(listaAdjuntos.stream().filter(a -> a.getTipoDeAdjunto() == TipoAdjunto.IMAGEN).count()).intValue();
        totalVideos = Long.valueOf(listaAdjuntos.stream().filter(a -> a.getTipoDeAdjunto() == TipoAdjunto.VIDEO).count()).intValue();
        totalOtros = Long.valueOf(listaAdjuntos.stream().filter(a -> a.getTipoDeAdjunto() == TipoAdjunto.DOCUMENTO).count()).intValue();
    }
    //</editor-fold>
}
