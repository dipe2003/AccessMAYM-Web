/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.correctivas;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.accion.acciones.Correctiva;
import com.dperez.maymweb.accion.actividad.Actividad;
import static com.dperez.maymweb.accion.actividad.TipoActividad.CORRECTIVA;
import static com.dperez.maymweb.accion.actividad.TipoActividad.PREVENTIVA;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class EditarActividadesAC implements Serializable {
    @Inject
    private FacadeDatos fDatos;
    @Inject
    private FacadeLectura fLectura;
    
    private int IdAccionCorrectivaSeleccionada;
    private String tipoActividad;
    
    private Map<Integer, Usuario> ListaUsuariosEmpresa;
    
    private int IdActividadSeleccionada;
    private Actividad ActividadSeleccionada;
    
    private Date FechaEstimadaImplementacion;
    private String Descripcion;
    private int ResponsableImplementacion;
    
    //  Getters
    public Map<Integer, Usuario> getListaUsuariosEmpresa() {return ListaUsuariosEmpresa;}
    public int getIdActividadSeleccionada() {return IdActividadSeleccionada;}
    public Actividad getActividadSeleccionada(){return this.ActividadSeleccionada;}
    public Date getFechaEstimadaImplementacion() {return FechaEstimadaImplementacion;}
    public String getDescripcion() {return Descripcion;}
    public int getResponsableImplementacion() {return ResponsableImplementacion;}
    
    //  Setters
    public void setListaUsuariosEmpresa(Map<Integer, Usuario> ListaUsuariosEmpresa) {this.ListaUsuariosEmpresa = ListaUsuariosEmpresa;}
    public void setIdActividadSeleccionada(int IdActividadSeleccionada) {this.IdActividadSeleccionada = IdActividadSeleccionada;}
    public void setActividadSeleccionada(Actividad ActividadSeleccionada){this.ActividadSeleccionada = ActividadSeleccionada;}
    public void setFechaEstimadaImplementacion(Date FechaEstimadaImplementacion) {this.FechaEstimadaImplementacion = FechaEstimadaImplementacion;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setResponsableImplementacion(int ResponsableImplementacion) {this.ResponsableImplementacion = ResponsableImplementacion;}
    
    //  Metodos
    /**
     * Edita la medida correctiva o preventiva de la accion correctiva.
     * Se persisten los cambios.
     * Muestra un mensaje de error si no se pudo completar correctamente.
     * Redirige a la pagina con la accion correctiva.
     * @throws java.io.IOException
     */
    public void editarActividad() throws IOException{
        if(!Descripcion.isEmpty() && FechaEstimadaImplementacion != null && ResponsableImplementacion != 0){
            // guardar los cambios y redirigir
            if(fDatos.EditarActividad(IdActividadSeleccionada, Descripcion, ResponsableImplementacion, FechaEstimadaImplementacion) == -1){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_FATAL, "No se pudo editar Medida Correctiva",
                        "No se pudo editar Medida Correctiva" ));
                FacesContext.getCurrentInstance().renderResponse();
            }else{
                // regresar a la pagina de editar accion correctiva enviando el id de la accion como parametro
                String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Main/Main.xhtml");
            }
        }
    }
    
    //  Inicializacion del bean
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion correctiva y el resto de las propiedades.
        IdAccionCorrectivaSeleccionada = Integer.parseInt(request.getParameter("id"));
        IdActividadSeleccionada = Integer.parseInt(request.getParameter("idactividad"));
        tipoActividad = request.getParameter("tipo");
        if(IdAccionCorrectivaSeleccionada != 0){
            Accion AccionCorrectiva = (Correctiva) fLectura.GetAccion(IdAccionCorrectivaSeleccionada);
            List<Actividad> actividades = new ArrayList<>();
            if(tipoActividad.equals("correctiva")){
                actividades = ((Correctiva) AccionCorrectiva).getActividades().stream()
                        .filter(a->a.getTipoActividad() == CORRECTIVA)
                        .collect(Collectors.toList());
            }else{
                actividades.addAll(((Correctiva)AccionCorrectiva).getActividades().stream()
                        .filter(a->a.getTipoActividad() == PREVENTIVA)
                        .collect(Collectors.toList()));
            }
            actividades.stream()
                    .filter(actividad -> actividad.getIdActividad() == IdActividadSeleccionada)
                    .forEachOrdered(actividad -> {
                        ActividadSeleccionada = actividad;
                    });
            if(ActividadSeleccionada!=null){
                this.Descripcion = ActividadSeleccionada.getDescripcion();
                this.FechaEstimadaImplementacion = ActividadSeleccionada.getFechaEstimadaImplementacion();
                this.ResponsableImplementacion = ActividadSeleccionada.getResponsableImplementacion().getId();
            }
            //  Usuarios
            this.ListaUsuariosEmpresa = new HashMap<>();
            Empresa empresa = (Empresa) request.getSession().getAttribute("Empresa");
            // llenar lista de usuarios para responsables de implementacion que no se hayan dado de baja.
            if(empresa!=null) {
                List<Usuario> tmpUsuarios = fLectura.GetUsuarios(true);
                ListaUsuariosEmpresa = tmpUsuarios.stream()
                        .sorted()
                        .collect(Collectors.toMap(Usuario::getId, usuario->usuario));
            }
        }
    }
}
