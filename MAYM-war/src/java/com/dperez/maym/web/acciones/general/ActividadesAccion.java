/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.general;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.herramientas.Evento;
import com.dperez.maymweb.herramientas.EventoActividad;
import com.dperez.maymweb.herramientas.ProgramadorEventos;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.acciones.actividad.TipoActividad;
import com.dperez.maymweb.modelo.usuario.Responsable;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
 * @author Diego
 */
@Named
@ViewScoped
public class ActividadesAccion implements Serializable {

    private FacadeDatos fDatos;  
    private FacadeLectura fLectura;
    
    @Inject
    private ProgramadorEventos pEventos;
    
    //<editor-fold desc="Atributos">
    private Accion AccionSeleccionada;
    private TipoActividad tipoDeActividad;
    private TipoActividad[] tiposDeActividad;
    private int IdActividadEditar;
    
    private List<Responsable> ListaResponsables;
    
    private Date fechaEstimada;
    private String strFechaEstimada;
    private String descripcion;
    private int responsable;
    
    //</editor-fold>
    
    //<editor-fold desc="Getters">
    public TipoActividad getTipoDeActividad() {return tipoDeActividad;}
    public Accion getAccionSeleccionada() {return AccionSeleccionada;}
    
    public int getIdActividadEditar() {return IdActividadEditar;}
    
    public List<Responsable> getListaResponsables() {return ListaResponsables;}
    
    public Date getFechaEstimada() {return fechaEstimada;}
    public String getStrFechaEstimada(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaEstimada == null) {
            return this.strFechaEstimada;
        }else{
            return fDate.format(fechaEstimada);
        }
    }
    public String getDescripcion() {return descripcion;}
    public int getResponsable() {return responsable;}
    
    public TipoActividad[] getTiposDeActividad(){return this.tiposDeActividad;}
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setTipoDeActividad(TipoActividad tipoActividad) {this.tipoDeActividad = tipoActividad;}
    public void setAccionSeleccionada(Accion AccionSeleccionada) {this.AccionSeleccionada = AccionSeleccionada;}
    
    public void setIdActividadEditar(int IdActividadEditar) {this.IdActividadEditar = IdActividadEditar;}
    
    public void setListaResponsables(List<Responsable> ListaResponsables) {this.ListaResponsables = ListaResponsables;}
    
    public void setFechaEstimada(Date fechaEstimada) {this.fechaEstimada = fechaEstimada;}
    public void setStrFechaEstimada(String strFechaEstimada) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaEstimada));
        }catch(ParseException ex){}
        this.strFechaEstimada = strFechaEstimada;
        this.fechaEstimada = cal.getTime();
    }
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setResponsable(int responsable) {this.responsable = responsable;}
    
    public void setTiposDeActividad(TipoActividad[] tipos){this.tiposDeActividad = tipos;}
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    /**
     * Agrega una nueva medida correctiva a la accion correctiva.     *
     * Se persisten los cambios.
     * Se redirige a la vista de editar accion
     * Muestra un mensaje de error si no se pudo completar correctamente.
     * @throws java.io.IOException
     */
    public void agregarActividad() throws IOException{
        Actividad actividad = null;
        if((actividad = fDatos.agregarActividad(AccionSeleccionada.getId(), fechaEstimada, descripcion,
                responsable, tipoDeActividad))!=null){
            FacesContext.getCurrentInstance().addMessage("form_agregar_actividades:btn_agregar_actividad", new FacesMessage(SEVERITY_FATAL, "No se pudo agregar Actividad", "No se pudo agregar Actividad" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // agregar el evento en el programador de tareas.
            Evento eventoNuevo = new EventoActividad(actividad);
            pEventos.ProgramarEvento(fechaEstimada, eventoNuevo);
            // redirigir a la edicion de la accion correctiva.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/General/EditarAccion.xhtml?id="+AccionSeleccionada.getId());
        }
    }
  
    /**
     * Remueve la medida correctiva de la accion correctiva.
     * Se persisten los cambios.
     * Muestra un mensaje de error si no se pudo completar correctamente.
     * @throws java.io.IOException
     */
    public void removerActividad() throws IOException{
        if(fDatos.removerActividad(AccionSeleccionada.getId(), IdActividadEditar)<0){
            FacesContext.getCurrentInstance().addMessage("form_agregar_actividades:btn_remover_actividad", new FacesMessage(SEVERITY_FATAL, "No se pudo quitar Actividad", "No se pudo quitar Actividad" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // remover el evento del programador de tareas.
            Actividad actividad = AccionSeleccionada.findActividad(IdActividadEditar);
            Evento eventoActividad = new EventoActividad(actividad);
            if (pEventos.ExisteEventoActividad(eventoActividad)){
                pEventos.RemoverEventoActividad(eventoActividad);
            }
            // redirigir a la edicion de la accion correctiva.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/General/EditarAccion.xhtml?id="+AccionSeleccionada.getId());
        }
    }
    
  
    
    /**
     * Guarda los cambios realizados en la medida correctiva y los persiste.
     * Se redirige a la vista de editar la accion correctiva correspondiente si se guardo, de lo contrario muestra un mensaje.
     * @throws IOException
     */
    public void guardarActividad() throws IOException{
        if(fDatos.editarActividad(AccionSeleccionada.getId(), IdActividadEditar, descripcion, responsable,
                fechaEstimada) < 0){
            FacesContext.getCurrentInstance().addMessage("form_agregar_actividades:btn_agregar_actividad",
                    new FacesMessage(SEVERITY_FATAL, "No se pudo guardar la Actividad", "No se pudo guardar la Actividad" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            // modificar evento
            Actividad actividad = AccionSeleccionada.findActividad(IdActividadEditar);
            Evento eventoActividad = new EventoActividad(actividad);
            if (pEventos.ExisteEventoActividad(eventoActividad)){
                pEventos.RemoverEventoActividad(eventoActividad);
                Evento eventoNuevo = new EventoActividad(actividad);
                pEventos.ProgramarEvento(fechaEstimada, eventoNuevo);
            }
            // redirigir a la edicion de la accion correctiva.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/General/EditarAccion.xhtml?id="+AccionSeleccionada.getId());
        }
    }
    
    //  Inicializacion del bean
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fDatos = new FacadeDatos();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion y el resto de las propiedades.
        int idAccion = 0;
        
        try{
            idAccion = Integer.parseInt(request.getParameter("id"));
            IdActividadEditar = Integer.parseInt(request.getParameter("editar"));
        }catch(Exception ex){
            IdActividadEditar = 0;
        }
        if(idAccion != 0){
            AccionSeleccionada = fLectura.GetAccion(idAccion);
            definirTiposActividad();
            tipoDeActividad = tiposDeActividad[0];
            if(IdActividadEditar > 0){
                if(!AccionSeleccionada.getActividadesDeAccion().isEmpty()){
                    List<Actividad> actividades = AccionSeleccionada.getActividadesDeAccion();
                    actividades.stream()
                            .filter(a->a.getId() == IdActividadEditar)
                            .forEach(medida->{
                                fechaEstimada = medida.getFechaEstimadaImplementacion();
                                descripcion =  medida.getDescripcion();
                                responsable = medida.getResponsableImplementacion().getId();
                                tipoDeActividad = medida.getTipoDeActividad();
                            });
                }
            }
            //  Usuarios
            ListaResponsables = fLectura.listarResponsables(true).stream()
                    .sorted(Comparator.comparing((Responsable r)-> r.getResponsabilidadResponsable().getNombre()))
                    .collect(Collectors.toList());                    
        }
    }
    
    private void definirTiposActividad(){
         switch(TipoAccion.valueOf(AccionSeleccionada.getClass().getSimpleName().toUpperCase())){
                case CORRECTIVA->{
                    tiposDeActividad = new TipoActividad[2];
                    tiposDeActividad[0] = TipoActividad.CORRECTIVA;
                    tiposDeActividad[1] = TipoActividad.PREVENTIVA;
                }
                case PREVENTIVA->{
                    tiposDeActividad = new TipoActividad[1];
                    tiposDeActividad[0] = TipoActividad.PREVENTIVA;
                }
                case MEJORA->{
                    tiposDeActividad = new TipoActividad[1];
                    tiposDeActividad[0] = TipoActividad.MEJORA;}
            }
    }
    //</editor-fold>
}
