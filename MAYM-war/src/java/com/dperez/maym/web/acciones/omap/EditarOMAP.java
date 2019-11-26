/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.omap;

import com.dperez.maym.web.configuraciones.ModalDetecciones;
import com.dperez.maym.web.herramientas.CargarArchivo;
import com.dperez.maymweb.herramientas.Evento;
import com.dperez.maymweb.herramientas.ProgramadorEventos;
import com.dperez.maymweb.herramientas.TipoEvento;
import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.acciones.Mejora;
import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.accion.adjunto.Adjunto;
import com.dperez.maymweb.accion.adjunto.EnumTipoAdjunto;
import com.dperez.maymweb.acciones.Preventiva;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.deteccion.EnumTipoDeteccion;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.herramientas.EventoAccion;
import com.dperez.maymweb.herramientas.EventoActividad;
import com.dperez.maymweb.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;


@Named
@ViewScoped
public class EditarOMAP implements Serializable {
    @Inject
    private FacadeAdministrador fAdmin;
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    @Inject
    private CargarArchivo cArchivo;
    @Inject
    private ProgramadorEventos pEventos;
    
    private int IdAccionSeleccionada;
    private Accion AccionSeleccionada;
    
    private ModalDetecciones modalDetecciones;
    
    private Date FechaDeteccion;
    private String strFechaDeteccion;
    private String Descripcion;
    private String AnalisisCausa;
    
    private TipoAccion TipoAccion;
    
    private String TituloAdjunto;
    private Map<Integer, Adjunto> MapAdjuntos;
    private Part ArchivoAdjunto;
    
    private EnumTipoDeteccion[] TiposDeteccion;
    private EnumTipoDeteccion TipoDeDeteccionSeleccionada;
    private Map<Integer, String> ListaDetecciones;
    private Integer DeteccionSeleccionada;
    
    private Map<Integer, Area> ListaAreasSectores;
    private Integer AreaSectorAccionSeleccionada;
    
    private Map<Integer, String> ListaCodificaciones;
    private Integer CodificacionSeleccionada;
    
    private Map<Integer, Actividad> ListaActividades;
    
    //comprobaciones
    private List<Usuario> ListaUsuarios;
    private int ResponsableImplementacion;
    private int ResponsableEficacia;
    private Date FechaEstimadaImplementacion;
    private String strFechaEstimadaImplementacion;
    private Date FechaEstimadaVerificacion;
    private String strFechaEstimadaVerificacion;
    
    private Comprobacion ComprobacionImplementacion;
    private Comprobacion ComprobacionEficacia;
    
    //  Getters
    public int getIdAccionSeleccionada(){return IdAccionSeleccionada;}
    public Accion getAccionSeleccionada() {return AccionSeleccionada;}
    public Date getFechaDeteccion() {return FechaDeteccion;}
    public String getStrFechaDeteccion(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaDeteccion == null) {
            return this.strFechaDeteccion;
        }else{
            return fDate.format(FechaDeteccion);
        }
    }
    public String getDescripcion() {return Descripcion;}
    public String getAnalisisCausa() {return AnalisisCausa;}
    public TipoAccion getTipoAccion() {return TipoAccion;}
    
    public String getTituloAdjunto(){return this.TituloAdjunto;}
    public Part getArchivoAdjunto() {return ArchivoAdjunto;}
    public Map<Integer, Adjunto> getMapAdjuntos() {return MapAdjuntos;}
    
    public Map<Integer, String> getListaCodificaciones(){return this.ListaCodificaciones;}
    public Integer getCodificacionSeleccionada() {return CodificacionSeleccionada;}
    
    public EnumTipoDeteccion getTipoDeDeteccionSeleccionada(){return this.TipoDeDeteccionSeleccionada;}
    public EnumTipoDeteccion[] getTiposDeteccion(){return this.TiposDeteccion;}
    public Map<Integer, String> getListaDetecciones(){return this.ListaDetecciones;}
    public Integer getDeteccionSeleccionada(){return this.DeteccionSeleccionada;}
    
    public Map<Integer, Area> getListaAreasSectores() {return this.ListaAreasSectores;}
    public Integer getAreaSectorAccionSeleccionada() {return AreaSectorAccionSeleccionada;}
    
    public Map<Integer, Actividad> getListaActividades() {return ListaActividades;}
    
    //comprobaciones
    
    public List<Usuario> getListaUsuarios() {return ListaUsuarios;}
    public int getResponsableImplementacion() {return ResponsableImplementacion;}
    public int getResponsableEficacia() {return ResponsableEficacia;}
    public Date getFechaEstimadaImplementacion() {return FechaEstimadaImplementacion;}
    public String getStrFechaEstimadaImplementacion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaEstimadaImplementacion == null) {
            return this.strFechaEstimadaImplementacion;
        }else{
            return fDate.format(FechaEstimadaImplementacion);
        }
    }
    public Date getFechaEstimadaVerificacion() {return FechaEstimadaVerificacion;}
    public String getStrFechaEstimadaVerificacion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaEstimadaVerificacion == null) {
            return this.strFechaEstimadaVerificacion;
        }else{
            return fDate.format(FechaEstimadaVerificacion);
        }
    }
    public Comprobacion getComprobacionImplementacion() {return ComprobacionImplementacion;}
    public Comprobacion getComprobacionEficacia() {return ComprobacionEficacia;}
    
    //  Setters
    public void setIdAccionSeleccionada(int IdAccionSeleccionada) {this.IdAccionSeleccionada = IdAccionSeleccionada;}
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
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setAnalisisCausa(String AnalisisCausa) {this.AnalisisCausa = AnalisisCausa;}
    public void setTipoAccion(TipoAccion TipoAccion) {this.TipoAccion = TipoAccion;}
    
    public void setTituloAdjunto(String TituloAdjunto){this.TituloAdjunto = TituloAdjunto;}
    public void setArchivoAdjunto(Part ArchivoAdjunto) {this.ArchivoAdjunto = ArchivoAdjunto;}
    public void setMapAdjuntos(Map<Integer, Adjunto> MapAdjuntos) {this.MapAdjuntos = MapAdjuntos;}
    
    public void setListaCodificaciones(Map<Integer, String> ListaCodificaciones){this.ListaCodificaciones = ListaCodificaciones;}
    public void setCodificacionSeleccionada(Integer CodificacionSeleccionada) {this.CodificacionSeleccionada = CodificacionSeleccionada;}
    
    public void setTipoDeDeteccionSeleccionada(EnumTipoDeteccion TipoDeteccion){this.TipoDeDeteccionSeleccionada = TipoDeteccion;}
    public void setTiposDeteccion(EnumTipoDeteccion[] TiposDeteccion){this.TiposDeteccion = TiposDeteccion;}
    public void setListaDetecciones(Map<Integer, String> ListaDetecciones){this.ListaDetecciones = ListaDetecciones;}
    public void setDeteccionSeleccionada(Integer DeteccionSeleccionada){this.DeteccionSeleccionada = DeteccionSeleccionada;}
    
    public void setListaAreasSectores(Map<Integer, Area> ListaAreasSectores) {this.ListaAreasSectores = ListaAreasSectores;}
    public void setAreaSectorAccionSeleccionada(Integer AreaSectorAccionSeleccionada) {this.AreaSectorAccionSeleccionada = AreaSectorAccionSeleccionada;}
    
    public void setListaActividades(Map<Integer, Actividad> ListaActividades) {this.ListaActividades = ListaActividades;}
    
    // comprobaciones
    public void setListaUsuarios(List<Usuario> ListaUsuarios) {this.ListaUsuarios = ListaUsuarios;}
    public void setResponsableEficacia(int ResponsableEficacia) {this.ResponsableEficacia = ResponsableEficacia;}
    public void setResponsableImplementacion(int ResponsableImplementacion) {this.ResponsableImplementacion = ResponsableImplementacion;}
    public void setFechaEstimadaImplementacion(Date FechaEstimadaImplementacion) {this.FechaEstimadaImplementacion = FechaEstimadaImplementacion;}
    public void setStrFechaEstimadaImplementacion(String strFechaEstimadaImplementacion) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaEstimadaImplementacion));
        }catch(ParseException ex){}
        this.strFechaEstimadaImplementacion = strFechaEstimadaImplementacion;
        this.FechaEstimadaImplementacion = cal.getTime();
    }
    public void setFechaEstimadaVerificacion(Date FechaEstimadaVerificacion) {this.FechaEstimadaVerificacion = FechaEstimadaVerificacion;}
    public void setStrFechaEstimadaVerificacion(String strFechaEstimadaVerificacion) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaEstimadaVerificacion));
        }catch(ParseException ex){}
        this.strFechaEstimadaVerificacion = strFechaEstimadaVerificacion;
        this.FechaEstimadaVerificacion = cal.getTime();
    }
    public void setComprobacionImplementacion(Comprobacion ComprobacionImplementacion) {this.ComprobacionImplementacion = ComprobacionImplementacion;}
    public void setComprobacionEficacia(Comprobacion ComprobacionEficacia) {this.ComprobacionEficacia = ComprobacionEficacia;}
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        TipoAccion = TipoAccion.valueOf(request.getParameter("tipo"));
        // recuperar el id para llenar datos de la accion de mejora y el resto de las propiedades.
        IdAccionSeleccionada = Integer.parseInt(request.getParameter("id"));
        if(IdAccionSeleccionada != 0){
            AccionSeleccionada = fLectura.GetAccion(IdAccionSeleccionada);
            FechaDeteccion = AccionSeleccionada.getFechaDeteccion();
            Descripcion = AccionSeleccionada.getDescripcion();
            AnalisisCausa = AccionSeleccionada.getAnalisisCausa();
            
            //  Codificaciones
            ListaCodificaciones = new TreeMap<>();
            List<Codificacion> tmpLista = fLectura.ListarCodificaciones();
            ListaCodificaciones = tmpLista.stream()
                    .sorted()
                    .collect(Collectors.toMap(Codificacion::getId, codificacion->codificacion.getNombre()));
            CodificacionSeleccionada = AccionSeleccionada.getCodificacionAccion().getId();
            
            //  Detecciones
            modalDetecciones = context.getApplication().evaluateExpressionGet(context, "#{modalDetecciones}", ModalDetecciones.class);
            ListaDetecciones = new TreeMap<>(modalDetecciones.getListaDetecciones());
            TiposDeteccion = EnumTipoDeteccion.values();
            TipoDeDeteccionSeleccionada = AccionSeleccionada.getDeteccion().getTipo();
            DeteccionSeleccionada = AccionSeleccionada.getDeteccion().getId();
            
            // Actividades
            List<Actividad> actividades = AccionSeleccionada.getActividades();
            this.ListaActividades = new HashMap<>();
            ListaActividades = actividades.stream()
                    .collect(Collectors.toMap(Actividad::getIdActividad, actividad->actividad));
            
            // Areas Sectores
            ListaAreasSectores = new HashMap<>();
            List<Area> tmpAreas = fLectura.ListarAreasSectores();
            ListaAreasSectores = tmpAreas.stream()
                    .sorted()
                    .collect(Collectors.toMap(Area::getId, area->area));
            AreaSectorAccionSeleccionada = AccionSeleccionada.getAreaSectorAccion().getId();
            
            // Adjuntos
            MapAdjuntos = new HashMap<>();
            actualizarListaAdjuntos();
            
            // Comprobaciones
            // llena la lista con los usuarios que no se hayan dado de baja.
            ListaUsuarios = fLectura.GetUsuarios(true);
            
            ComprobacionImplementacion = AccionSeleccionada.getComprobacionImplementacion();
            ComprobacionEficacia = AccionSeleccionada.getComprobacionEficacia();
            
            // para editar
            if(ComprobacionImplementacion != null && ComprobacionImplementacion.getFechaEstimada() != null){
                this.setFechaEstimadaImplementacion(ComprobacionImplementacion.getFechaEstimada());
            }
            if(ComprobacionImplementacion != null && ComprobacionImplementacion.getResponsable() != null){
                this.ResponsableImplementacion = ComprobacionImplementacion.getResponsable().getId();
            }
            if(ComprobacionEficacia != null && ComprobacionEficacia.getFechaEstimada() != null){
                this.setFechaEstimadaVerificacion(ComprobacionEficacia.getFechaEstimada());
            }
            if(ComprobacionEficacia != null && ComprobacionEficacia.getResponsable() != null){
                this.ResponsableEficacia = ComprobacionEficacia.getResponsable().getId();
            }
        }
    }
    
    private void actualizarListaAdjuntos(){
        Accion accion = fLectura.GetAccion(IdAccionSeleccionada);
        if(!accion.getAdjuntos().isEmpty()){
            List<Adjunto> listAdjuntos = accion.getAdjuntos();
            this.MapAdjuntos = listAdjuntos.stream()
                    .collect(Collectors.toMap(Adjunto::getId, adjunto->adjunto));
        }
    }
    
    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de deteccion.
     */
    public void actualizarDeteccion(){
        ListaDetecciones = new TreeMap<>(modalDetecciones.getListaDetecciones());
    }
    
    /**
     * Carga el adjunto en la lista de adjuntos.
     * Deja vacios los campos para un nuevo adjunto.
     */
    public void agregarAdjunto(){
        String datosAdjunto[] = cArchivo.guardarArchivo(TipoAccion + String.valueOf(IdAccionSeleccionada), ArchivoAdjunto, TituloAdjunto, "Nombre Empresa");
        // datosAdjunto[0]: ubicacion | datosAdjunto[1]: extension
        if(!datosAdjunto[0].isEmpty()){
            EnumTipoAdjunto tipoAdjunto = EnumTipoAdjunto.IMAGEN;
            String extension = datosAdjunto[1];
            List<String> tipos = new ArrayList<>();
            tipos.add("jpeg");
            tipos.add("jpg");
            tipos.add("png");
            tipos.add("gif");
            if(!tipos.contains(extension.toLowerCase().trim())){
                tipoAdjunto = EnumTipoAdjunto.DOCUMENTO;
            }
            if((fDatos.AgregarArchivoAdjunto(IdAccionSeleccionada, TituloAdjunto, datosAdjunto[0], tipoAdjunto))!=-1){
                actualizarListaAdjuntos();
                this.TituloAdjunto = new String();
                this.ArchivoAdjunto =  null;
                
            }else{
                cArchivo.BorrarArchivo(datosAdjunto[0]);
            }
        }
    }
    
    /**
     * Quita el adjunto de la lista de adjuntos.
     * @param IdAdjunto
     * @throws IOException
     */
    public void quitarAdjunto(int IdAdjunto) throws IOException{
        if(cArchivo.BorrarArchivo(this.MapAdjuntos.get(IdAdjunto).getUbicacion())){
            if((fDatos.RemoverAdjunto(IdAccionSeleccionada, IdAdjunto))!=-1){
                this.MapAdjuntos.remove(IdAdjunto);
            }
        }
    }
    
    /**
     * Quita la actividad seleccionada de la accion.
     * Muestra un mensaje si no se pudo quitar de lo contrario redidirge a la vista de edicion de la accion.
     * @param IdActividad
     * @throws IOException
     */
    public void quitarActividad(int IdActividad) throws IOException{
        FacesContext ctx = FacesContext.getCurrentInstance();
        Actividad actividad = ListaActividades.get((int)IdActividad);
        if(fDatos.RemoverActividad(IdAccionSeleccionada, IdActividad)==-1){
            // Si no se actualizo muestra mensaje de error.
            ctx.addMessage("form_accion:guardar_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo editar la Accion", "No se pudo editar la Accion" ));
            ctx.renderResponse();
        }else{
            // remover el evento del programador de tareas.
            
            Evento eventoActividad = new EventoActividad(actividad);
            if (pEventos.ExisteEventoActividad(eventoActividad)){
                pEventos.RemoverEventoActividad(eventoActividad);
            }
            // Si la eliminacion se realizo correctamente redirige a lista de acciones.
            String url = ctx.getExternalContext().getRequestContextPath();
            ctx.getExternalContext().redirect(url+"/Views/Acciones/MejorasYPreventivas/EditarOMAP.xhtml?id="+IdAccionSeleccionada+"&amp;tipo="+TipoAccion);
        }
    }
    
    /**
     * Actualiza la accion correctiva con los datos nuevos.
     * Si se muestra mensaje de confirmacion.
     * @throws java.io.IOException
     */
    public void editarAccion() throws IOException{
        // actualizar accion
        if(fDatos.EditarAccion(IdAccionSeleccionada, TipoAccion, FechaDeteccion, Descripcion, AnalisisCausa,null,
                AreaSectorAccionSeleccionada, DeteccionSeleccionada, CodificacionSeleccionada) == -1){
            // Si no se actualizo muestra mensaje de error.
            FacesContext.getCurrentInstance().addMessage("form_accion:guardar_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo editar la Accion", "No se pudo editar la Accion" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            if(!this.ListaActividades.isEmpty()){
                if(AccionSeleccionada.getComprobacionImplementacion() != null){
                    if(AccionSeleccionada.getComprobacionImplementacion().getFechaEstimada() != this.FechaEstimadaImplementacion
                            || AccionSeleccionada.getComprobacionImplementacion().getResponsable().getId() != this.ResponsableImplementacion){
                        fDatos.SetComprobacionImplementacion(this.FechaEstimadaImplementacion, this.ResponsableImplementacion, IdAccionSeleccionada);
                        // se crea el evento con los datos guardados para comparar reemplazar con el anterior en el caso que hayan cambios
                        Evento eventoAccion = new EventoAccion(TipoEvento.IMPLEMENTACION_ACCION, AccionSeleccionada);
                        if (pEventos.ExisteEventoAccion(eventoAccion)){
                            pEventos.RemoverEventoAccion(eventoAccion);
                            Evento eventoNuevo = new EventoAccion(TipoEvento.IMPLEMENTACION_ACCION, AccionSeleccionada);
                            pEventos.ProgramarEvento(FechaEstimadaImplementacion, eventoNuevo);
                        }
                    }
                }else{
                    fDatos.SetComprobacionImplementacion(this.FechaEstimadaImplementacion, this.ResponsableImplementacion, IdAccionSeleccionada);
                    Evento eventoNuevo = new EventoAccion(TipoEvento.IMPLEMENTACION_ACCION, AccionSeleccionada);
                    pEventos.ProgramarEvento(FechaEstimadaImplementacion, eventoNuevo);
                }
                if(AccionSeleccionada.getComprobacionEficacia()!= null){
                    if(AccionSeleccionada.getComprobacionEficacia().getFechaEstimada() != this.FechaEstimadaVerificacion
                            || AccionSeleccionada.getComprobacionEficacia().getResponsable().getId() != this.ResponsableEficacia){
                        fDatos.SetVerificacionEficacia(this.FechaEstimadaVerificacion, this.ResponsableEficacia, IdAccionSeleccionada);
                        // se crea el evento con los datos guardados para comparar reemplazar con el anterior en el caso que hayan cambios
                        Evento eventoAccion = new EventoAccion(TipoEvento.VERIFICACION_EFICACIA, AccionSeleccionada);
                        if (pEventos.ExisteEventoAccion(eventoAccion)){
                            pEventos.RemoverEventoAccion(eventoAccion);
                            Evento eventoNuevo = new EventoAccion(TipoEvento.VERIFICACION_EFICACIA, AccionSeleccionada);
                            pEventos.ProgramarEvento(FechaEstimadaVerificacion, eventoNuevo);
                        }
                    }
                }else{
                    fDatos.SetVerificacionEficacia(this.FechaEstimadaVerificacion, this.ResponsableEficacia, IdAccionSeleccionada);
                    Evento eventoNuevo = new EventoAccion(TipoEvento.VERIFICACION_EFICACIA, AccionSeleccionada);
                    pEventos.ProgramarEvento(FechaEstimadaVerificacion, eventoNuevo);
                }
            }
            FacesContext.getCurrentInstance().addMessage("form_accion:guardar_accion", new FacesMessage(SEVERITY_INFO, "Los datos se guardaron.", "Los datos se guardaron." ));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    
    /**
     * Elimina la accion de la base de datos.
     * Se eliminan todos los datos relacionados (actividades, adjuntos, comprobaciones)
     * @throws java.io.IOException
     */
    public void eliminarAccion() throws IOException{
        if(fAdmin.EliminarAccion(IdAccionSeleccionada)==-1){
            // Si no se elimino muestra mensaje de error.
            FacesContext.getCurrentInstance().addMessage("form_accion:eliminar_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar la Accion", "No se pudo eliminar la Accion" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // Eliminar todos los archivos adjuntos del disco.
            if(!MapAdjuntos.isEmpty()){
                for(Adjunto adjunto: MapAdjuntos.values()){
                    cArchivo.BorrarArchivo(adjunto.getUbicacion());
                }
            }
            // Eliminar Eventos
            pEventos.RemoverEventos(IdAccionSeleccionada);
            // Si la eliminacion se realizo correctamente redirige a lista de acciones.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/MejorasYPreventivas/ListarOMAP.xhtml?tipo="+TipoAccion);
        }
    }
}
