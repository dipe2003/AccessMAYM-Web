/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.general;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.comprobaciones.ResultadoComprobacion;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import static com.dperez.maymweb.modelo.acciones.actividad.TipoActividad.CORRECTIVA;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeVerificador;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.acciones.actividad.TipoActividad;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
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
public class SeguimientoAccion implements Serializable {
 
    private FacadeLectura fLectura;
    private FacadeDatos fDatos;
    private FacadeVerificador fVerif;
    
    //<editor-fold desc="Atributos">
    private List<Actividad> medidasCorrectivas;
    private List<Actividad> medidasPreventivas;
    private List<Actividad> actividades;
    
    private Date FechaImplementacion;
    
    private String ObservacionesImplementacion;
    private Date FechaComprobacionImplementacion;
    private String strFechaComprobacionImplementacion;
    private ResultadoComprobacion[] Comprobaciones;
    private ResultadoComprobacion ComprobacionSeleccionadaImplementacion;
    
    private String ObservacionesEficacia;
    private Date FechaComprobacionEficacia;
    private String strFechaComprobacionEficacia;
    private ResultadoComprobacion ComprobacionSeleccionadaEficacia;
    
    private List<Usuario> ListaUsuarios;
    private int IdUsuarioSeleccionado;
    
    private Accion AccionSeleccionada;
    
    private TipoAccion tipoDeAccion;
    
    private boolean EstaImplementada;
    
    //</editor-fold>
    
    //<editor-fold desc="Getters">
    
    public List<Actividad> getMedidasCorrectivas() {return medidasCorrectivas;}
    public List<Actividad> getMedidasPreventivas() {return medidasPreventivas;}
    public List<Actividad> getActividades(){return this.actividades;}
    public Date getFechaImplementacion(){return this.FechaImplementacion;}
    public String getObservacionesImplementacion() {return ObservacionesImplementacion;}
    public Date getFechaComprobacionImplementacion() {return FechaComprobacionImplementacion;}
    public String getStrFechaComprobacionImplementacion(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaComprobacionImplementacion == null) {
            return this.strFechaComprobacionImplementacion;
        }else{
            return fDate.format(FechaComprobacionImplementacion);
        }
    }
    public ResultadoComprobacion[] getComprobaciones() {return Comprobaciones;}
    public ResultadoComprobacion getComprobacionSeleccionadaImplementacion() {return ComprobacionSeleccionadaImplementacion;}
    
    public String getObservacionesEficacia() {return ObservacionesEficacia;}
    public Date getFechaComprobacionEficacia() {return FechaComprobacionEficacia;}
    public String getStrFechaComprobacionEficacia(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaComprobacionEficacia == null) {
            return this.strFechaComprobacionEficacia;
        }else{
            return fDate.format(FechaComprobacionEficacia);
        }
    }
    public ResultadoComprobacion getComprobacionSeleccionadaEficacia() {return ComprobacionSeleccionadaEficacia;}
    
    public Accion getAccionSeleccionada() {return AccionSeleccionada;}
    
    public TipoAccion getTipoDeAccion(){return this.tipoDeAccion;}
    
    public boolean isEstaImplementada() {return EstaImplementada;}
    
    public List<Usuario> getListaUsuarios(){return this.ListaUsuarios;}
    public int getIdUsuarioSeleccionado(){return this.IdUsuarioSeleccionado;}
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setMedidasCorrectivas(List<Actividad> MedidasCorrectivas) {this.medidasCorrectivas = MedidasCorrectivas;}
    public void setMedidasPreventivas(List<Actividad> MedidasPreventivas) {this.medidasPreventivas = MedidasPreventivas;}
    public void setActividades(List<Actividad> actividades){this.actividades = actividades;}
    public void setFechaImplementacion(Date FechaImplementacion){this.FechaImplementacion = FechaImplementacion;}
    
    public void setObservacionesImplementacion(String ObservacionesImplementacion) {this.ObservacionesImplementacion = ObservacionesImplementacion;}
    public void setFechaComprobacionImplementacion(Date FechaComprobacionImplementacion) {this.FechaComprobacionImplementacion = FechaComprobacionImplementacion;}
    public void setStrFechaComprobacionImplementacion(String strFechaComprobacionImplementacion) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaComprobacionImplementacion));
        }catch(ParseException ex){}
        this.strFechaComprobacionImplementacion = strFechaComprobacionImplementacion;
        this.FechaComprobacionImplementacion = cal.getTime();
    }
    public void setComprobaciones(ResultadoComprobacion[] Comprobaciones) {this.Comprobaciones = Comprobaciones;}
    public void setComprobacionSeleccionadaImplementacion(ResultadoComprobacion ComprobacionSeleccionadaImplementacion) {this.ComprobacionSeleccionadaImplementacion = ComprobacionSeleccionadaImplementacion;}
    
    public void setObservacionesEficacia(String ObservacionesEficacia) {this.ObservacionesEficacia = ObservacionesEficacia;}
    public void setFechaComprobacionEficacia(Date FechaComprobacionEficacia) {this.FechaComprobacionEficacia = FechaComprobacionEficacia;}
    public void setStrFechaComprobacionEficacia(String strFechaComprobacionEficacia) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaComprobacionEficacia));
        }catch(ParseException ex){}
        this.strFechaComprobacionEficacia = strFechaComprobacionEficacia;
        this.FechaComprobacionEficacia = cal.getTime();
    }
    public void setComprobacionSeleccionadaEficacia(ResultadoComprobacion ComprobacionSeleccionadaEficacia) {this.ComprobacionSeleccionadaEficacia = ComprobacionSeleccionadaEficacia;}
    
    public void setAccionSeleccionada(Accion AccionSeleccionada) {this.AccionSeleccionada = AccionSeleccionada;}
    
    public void setTipoDeAccion(TipoAccion tipo){this.tipoDeAccion = tipo;}
    
    public void setListaUsuarios(List<Usuario> ListaUsuarios){this.ListaUsuarios = ListaUsuarios;}
    public void setIdUsuarioSeleccionado(int IdUsuarioSeleccionado){this.IdUsuarioSeleccionado = IdUsuarioSeleccionado;}
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    /**
     * Agrega fecha de implementacion de la actividad seleccionada.Muestra un mensaje si no se pudo agregar fecha de lo contrario agrega la fecha a la actividad de la lista del bean.
     * @param IdActividad
     * @param tipo
     * @throws java.io.IOException
     */
    public void setFechaImplementacionActividad(int IdActividad, String tipo) throws IOException{
        TipoActividad tipoActividad = TipoActividad.valueOf(tipo.toUpperCase());
        FechaImplementacion = new Date();
        if((fDatos.setFechaImplementacionActividad(FechaImplementacion, IdActividad, AccionSeleccionada.getId()))== -1){
            switch(tipoActividad){
                case CORRECTIVA->mostrarErrorCorrectiva(IdActividad);
                case PREVENTIVA->mostrarErrorPreventiva(IdActividad);
                default->mostrarErrorActividad(IdActividad);
            }
        }else{
            // recargar vista de seguimiento
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/General/SeguimientoAccion.xhtml?id="+AccionSeleccionada.getId());
        }
    }
    
    private void mostrarErrorCorrectiva(int idActividad){
        FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_setFechaImplementacion_corr_"+idActividad, new FacesMessage(SEVERITY_FATAL, "No se pudo agregar fecha", "No se pudo agregar fecha" ));
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    private void mostrarErrorPreventiva(int idActividad){
        FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_setFechaImplementacion_prev_"+idActividad, new FacesMessage(SEVERITY_FATAL, "No se pudo agregar fecha", "No se pudo agregar fecha" ));
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    private void mostrarErrorActividad(int idActividad){
        FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_setFechaImplementacion_act_"+idActividad,
                new FacesMessage(SEVERITY_FATAL, "No se pudo agregar fecha", "No se pudo agregar fecha" ));
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    /**
     * Agrega una comprobacion de implementacion de la accion seleccionada.
     * Se debe comprobar que el estado sea proceso de Implementacion y todas las actividades hayan sido implementadas.
     * Muestra un mensaje si no se pudo agregar, de lo contrario se regresa a la lista de acciones.
     * @throws java.io.IOException
     */
    public void comprobarImplementacionAccion() throws IOException{
        if((fVerif.SetComprobacionImplementacionAccion(FechaComprobacionImplementacion, ObservacionesImplementacion, ComprobacionSeleccionadaImplementacion, AccionSeleccionada.getId()))== -1){
            FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_comprobar_implementacion", new FacesMessage(SEVERITY_FATAL, "No se pudo comprobar implementacion", "No se pudo comprobar implementacion" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // regresar a la pagina listar acciones
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/Correctivas/SeguimientoCorrectiva.xhtml?id="+AccionSeleccionada.getId());
        }
    }
    
    /**
     * Agrega una comprobacion de eficacia de la accion seleccionada.
     * Se debe comprobar que el estado sea proceso de verificacion.
     * Muestra un mensaje si no se pudo agregar, de lo contrario se regresa a la lista de acciones.
     * @throws java.io.IOException
     */
    public void comprobarEficaciaAccion() throws IOException{
        if((fVerif.SetVerificacionEficaciaAccion(FechaComprobacionEficacia, ObservacionesEficacia, ComprobacionSeleccionadaEficacia, AccionSeleccionada.getId()))== -1){
            FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_comprobar_eficacia", new FacesMessage(SEVERITY_FATAL, "No se pudo comprobar eficacia", "No se pudo comprobar eficacia" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // regresar a la pagina listar acciones
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/Correctivas/SeguimientoCorrectiva.xhtml?id="+AccionSeleccionada.getId());
        }
    }
    
    /**
     * Cambia el estado de la accion a desestimada y registra el motivo.
     * Se muestra un mensaje si no se pudo desestimar, de lo contrario regrega a la lista de acciones.
     * @throws IOException
     */
    public void desestimarAccion() throws IOException{
        if(fVerif.DesestimarAccion(ObservacionesImplementacion, AccionSeleccionada.getId())== -1){
            FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_desestimar_accion", new FacesMessage(SEVERITY_FATAL, "No se pudo desestimar la accion", "No se pudo desestimar la accion" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // regresar a la pagina listar acciones
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/Correctivas/ListarCorrectivas.xhtml");
        }
    }
    
    
    //  Inicializcion
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fDatos = new FacadeDatos();
        fVerif = new FacadeVerificador();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion y el resto de las propiedades.
        int IdAccion = Integer.parseInt(request.getParameter("id"));
        if(IdAccion != 0){
            AccionSeleccionada = fLectura.GetAccion(IdAccion);
            actividades = AccionSeleccionada.getActividadesDeAccion().stream()
                    .collect(Collectors.toList());
            tipoDeAccion = TipoAccion.valueOf(AccionSeleccionada.getClass().getSimpleName().toUpperCase());
            
            if(tipoDeAccion == TipoAccion.CORRECTIVA){
                medidasCorrectivas  = new ArrayList();
                medidasPreventivas  = new ArrayList();
                actividades.stream()
                        .forEach(a->{
                            if(a.getTipoDeActividad() == CORRECTIVA){
                                medidasCorrectivas.add(a);
                            }else{
                                medidasPreventivas.add(a);
                            }
                        });
            }
            
            //  Usuarios
            
            ListaUsuarios = fLectura.listarUsuarios(true).stream()
                    .sorted()
                    .collect(Collectors.toList());
            
            EstaImplementada = AccionSeleccionada.estanImplementadasTodasActividades();
            
            //  Resultado de comprobaciones
            Comprobaciones = ResultadoComprobacion.values();
            if(AccionSeleccionada.getComprobacionImplementacion()==null || AccionSeleccionada.getComprobacionImplementacion().getFechaComprobacion() == null){
                ComprobacionSeleccionadaImplementacion = ResultadoComprobacion.NO_COMPROBADA;
            }else{
                ComprobacionSeleccionadaImplementacion = AccionSeleccionada.getComprobacionImplementacion().getResultadoComprobacion();
                ObservacionesImplementacion = AccionSeleccionada.getComprobacionImplementacion().getObservaciones();
                FechaComprobacionImplementacion = AccionSeleccionada.getComprobacionImplementacion().getFechaComprobacion();
            }
            
            if(AccionSeleccionada.getComprobacionEficacia()==null || AccionSeleccionada.getComprobacionEficacia().getFechaComprobacion() == null){
                ComprobacionSeleccionadaEficacia = ResultadoComprobacion.NO_COMPROBADA;
            }else{
                ComprobacionSeleccionadaEficacia = AccionSeleccionada.getComprobacionEficacia().getResultadoComprobacion();
                ObservacionesEficacia = AccionSeleccionada.getComprobacionEficacia().getObservaciones();
                FechaComprobacionEficacia = AccionSeleccionada.getComprobacionEficacia().getFechaComprobacion();
            }
        }
    }
    //</editor-fold>
}
