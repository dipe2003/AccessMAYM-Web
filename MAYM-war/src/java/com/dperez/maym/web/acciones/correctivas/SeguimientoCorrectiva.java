/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.correctivas;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.accion.comprobaciones.ResultadoComprobacion;
import com.dperez.maymweb.accion.acciones.Correctiva;
import com.dperez.maymweb.accion.acciones.TipoDesvio;
import com.dperez.maymweb.accion.actividad.Actividad;
import static com.dperez.maymweb.accion.actividad.TipoActividad.CORRECTIVA;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeVerificador;
import com.dperez.maymweb.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
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
public class SeguimientoCorrectiva implements Serializable {
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    @Inject
    private FacadeVerificador fVerif;
    
    private TipoDesvio TipoDesvio;
    
    private Map<Integer, Actividad> MedidasCorrectivas;
    private Map<Integer, Actividad> MedidasPreventivas;
    
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
    
    private Map<Integer, Usuario> ListaUsuarios;
    private int IdUsuarioSeleccionado;
    
    private Accion AccionSeleccionada;
    
    private boolean EstaImplementada;
    
    private Empresa EmpresaAccion;
    
    //  Getters
    
    public TipoDesvio getTipoDesvio() {return TipoDesvio;}
    
    public Map<Integer, Actividad> getMedidasCorrectivas() {return MedidasCorrectivas;}
    public Map<Integer, Actividad> getMedidasPreventivas() {return MedidasPreventivas;}
    
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
    
    public boolean isEstaImplementada() {return EstaImplementada;}
    
    public Map<Integer, Usuario> getListaUsuarios(){return this.ListaUsuarios;}
    public int getIdUsuarioSeleccionado(){return this.IdUsuarioSeleccionado;}
    
    public Empresa getEmpresaAccion() {return EmpresaAccion;}
    
    //  Setters
    
    public void setTipoDesvio(TipoDesvio TipoDesvio) {this.TipoDesvio = TipoDesvio;}
    
    public void setMedidasCorrectivas(Map<Integer, Actividad> MedidasCorrectivas) {this.MedidasCorrectivas = MedidasCorrectivas;}
    public void setMedidasPreventivas(Map<Integer, Actividad> MedidasPreventivas) {this.MedidasPreventivas = MedidasPreventivas;}
    
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
    
    public void setListaUsuarios(Map<Integer, Usuario> ListaUsuarios){this.ListaUsuarios = ListaUsuarios;}
    public void setIdUsuarioSeleccionado(int IdUsuarioSeleccionado){this.IdUsuarioSeleccionado = IdUsuarioSeleccionado;}
    
    
    //  Metodos
    
    /**
     * Agrega fecha de implementacion de la actividad seleccionada.
     * Muestra un mensaje si no se pudo agregar fecha de lo contrario agrega la fecha a la actividad de la lista del bean.
     * @param IdActividad
     * @throws java.io.IOException
     */
    public void setFechaImplementacionMedidaCorrectiva(int IdActividad) throws IOException{
        FechaImplementacion = new Date();
        if((fDatos.SetFechaImplementacionActividad(FechaImplementacion, IdActividad, AccionSeleccionada.getId()))== -1){
            FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_setFechaImplementacion_corr_"+IdActividad, new FacesMessage(SEVERITY_FATAL, "No se pudo agregar fecha", "No se pudo agregar fecha" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // recargar vista de seguimiento
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/Correctivas/SeguimientoCorrectiva.xhtml?id="+AccionSeleccionada.getId());
        }
    }
    /**
     * Agrega fecha de implementacion de la actividad seleccionada.
     * Muestra un mensaje si no se pudo agregar fecha de lo contrario agrega la fecha a la actividad de la lista del bean.
     * @param IdActividad
     * @throws java.io.IOException
     */
    public void setFechaImplementacionMedidaPreventiva(int IdActividad) throws IOException{
        FechaImplementacion = new Date();
        if((fDatos.SetFechaImplementacionActividad(FechaImplementacion, IdActividad, AccionSeleccionada.getId()))== -1){
            FacesContext.getCurrentInstance().addMessage("form_seguimiento_accion:btn_setFechaImplementacion_prev_"+IdActividad, new FacesMessage(SEVERITY_FATAL, "No se pudo agregar fecha", "No se pudo agregar fecha" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // regresar a la pagina listar acciones
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/Correctivas/SeguimientoCorrectiva.xhtml?id="+AccionSeleccionada.getId());
        }
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
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion y el resto de las propiedades.
        int IdAccion = Integer.parseInt(request.getParameter("id"));
        if(IdAccion != 0){
            AccionSeleccionada = (Correctiva) fLectura.GetAccion(IdAccion);
            List<Actividad> actividades = ((Correctiva)AccionSeleccionada).getActividades().stream()
                    .filter(a->a.getTipoActividad() == CORRECTIVA)
                    .collect(Collectors.toList());
                    
            MedidasCorrectivas = new HashMap<>();
            MedidasCorrectivas = actividades.stream()
                    .collect(Collectors.toMap(Actividad::getIdActividad, actividad->actividad));
            actividades.clear();
            
            actividades = ((Correctiva)AccionSeleccionada).getActividades().stream()
                    .filter(a->a.getTipoActividad() == CORRECTIVA)
                    .collect(Collectors.toList());
            
            MedidasPreventivas = new HashMap<>();
            MedidasPreventivas = actividades.stream()
                    .collect(Collectors.toMap(Actividad::getIdActividad, actividad->actividad));
            
            //  Usuarios
            this.ListaUsuarios = new HashMap<>();
            Empresa empresa = (Empresa) request.getSession().getAttribute("Empresa");
            // llenar lista de usuarios para responsables de implementacion que no se hayan dado de baja.
            if(empresa!=null) {
                List<Usuario> tmpUsuarios = fLectura.GetUsuarios(true);
                ListaUsuarios = tmpUsuarios.stream()
                        .sorted()
                        .collect(Collectors.toMap(Usuario::getId, usuario->usuario));
            }
            TipoDesvio = ((Correctiva)AccionSeleccionada).getTipo();
            EstaImplementada = AccionSeleccionada.EstanImplementadasActividades();

            //  Resultado de comprobaciones
            Comprobaciones = ResultadoComprobacion.values();
            if(AccionSeleccionada.getComprobacionImplementacion()==null || AccionSeleccionada.getComprobacionImplementacion().getFechaComprobacion() == null){
                ComprobacionSeleccionadaImplementacion = ResultadoComprobacion.NO_COMPROBADA;
            }else{
                ComprobacionSeleccionadaImplementacion = AccionSeleccionada.getComprobacionImplementacion().getResultado();
                ObservacionesImplementacion = AccionSeleccionada.getComprobacionImplementacion().getObservaciones();
                FechaComprobacionImplementacion = AccionSeleccionada.getComprobacionImplementacion().getFechaComprobacion();
            }
            
            if(AccionSeleccionada.getComprobacionEficacia()==null || AccionSeleccionada.getComprobacionEficacia().getFechaComprobacion() == null){
                ComprobacionSeleccionadaEficacia = ResultadoComprobacion.NO_COMPROBADA;
            }else{
                ComprobacionSeleccionadaEficacia = AccionSeleccionada.getComprobacionEficacia().getResultado();
                ObservacionesEficacia = AccionSeleccionada.getComprobacionEficacia().getObservaciones();
                FechaComprobacionEficacia = AccionSeleccionada.getComprobacionEficacia().getFechaComprobacion();
            }            
        }        
    }    
}
