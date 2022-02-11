/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
@ManagedBean
public class ModalDetecciones implements Serializable {

    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    
    private TipoDeteccion[] TiposDeteccion;
    private TipoDeteccion TipoDeDeteccionSeleccionada;
    private String NombreNuevaDeteccion;
    private List<Deteccion> ListaDetecciones;
    private Integer DeteccionSeleccionada;
    
    //  Getters
    public TipoDeteccion getTipoDeDeteccionSeleccionada(){return this.TipoDeDeteccionSeleccionada;}
    public TipoDeteccion[] getTiposDeteccion(){return this.TiposDeteccion;}
    public List<Deteccion> getListaDetecciones(){return this.ListaDetecciones;}
    public String getNombreNuevaDeteccion(){return this.NombreNuevaDeteccion;}
    public Integer getDeteccionSeleccionada(){return this.DeteccionSeleccionada;}
    
    //  Setters
    public void setTipoDeDeteccionSeleccionada(TipoDeteccion TipoDeteccion){this.TipoDeDeteccionSeleccionada = TipoDeteccion;}
    public void setTiposDeteccion(TipoDeteccion[] TiposDeteccion){this.TiposDeteccion = TiposDeteccion;}
    public void setListaDetecciones(List<Deteccion> ListaDetecciones){this.ListaDetecciones = ListaDetecciones;}
    public void setNombreNuevaDeteccion(String NombreNuevaDeteccion){this.NombreNuevaDeteccion = NombreNuevaDeteccion;}
    public void setDeteccionSeleccionada(Integer DeteccionSeleccionada){this.DeteccionSeleccionada = DeteccionSeleccionada;}
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        //  Detecciones
        TiposDeteccion = TipoDeteccion.values();
        TipoDeDeteccionSeleccionada = TipoDeteccion.INTERNA;
        this.ListaDetecciones = new ArrayList<>();
        actualizarDeteccion();
    }
    
    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de deteccion.
     */
    public void actualizarDeteccion(){
        this.ListaDetecciones.clear();
        ListaDetecciones = fLectura.listarDetecciones().stream()                
                .sorted(Comparator.comparing(Deteccion::getNombre))
                .collect(Collectors.toList());
    }
    
    /**
     * Crea nueva deteccion con el tipo interna/externa seleccionado.
     * Si no se crea se muestra un mensaje
     */
    public void nuevaDeteccion() {
        // Crear Nueva Deteccion y actualizar lista
        Deteccion det = fAdmin.nuevaDeteccion(NombreNuevaDeteccion, TipoDeDeteccionSeleccionada);
        if(det != null){
            actualizarDeteccion();
            this.DeteccionSeleccionada = det.getId();
            this.NombreNuevaDeteccion = new String();
            this.TipoDeDeteccionSeleccionada = det.getTipoDeDeteccion();
            FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nueva_deteccion", new FacesMessage(SEVERITY_INFO, det.getNombre() + " fue creada.", det.getNombre() + " fue creada." ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nueva_deteccion", new FacesMessage(SEVERITY_INFO, "No se pudo crear nueva deteccion", "No se pudo crear nueva deteccion" ));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    
    public void limpiarModalDeteccion(){
        this.NombreNuevaDeteccion = new String();
    }
}
