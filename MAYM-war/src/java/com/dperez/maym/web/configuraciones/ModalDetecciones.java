/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.deteccion.EnumTipoDeteccion;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@ManagedBean
public class ModalDetecciones implements Serializable {
    @Inject
    private FacadeAdministrador fAdmin;
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    
    private EnumTipoDeteccion[] TiposDeteccion;
    private EnumTipoDeteccion TipoDeDeteccionSeleccionada;
    private String NombreNuevaDeteccion;
    private Map<Integer, String> ListaDetecciones;
    private Integer DeteccionSeleccionada;
    
    //  Getters
    public EnumTipoDeteccion getTipoDeDeteccionSeleccionada(){return this.TipoDeDeteccionSeleccionada;}
    public EnumTipoDeteccion[] getTiposDeteccion(){return this.TiposDeteccion;}
    public Map<Integer, String> getListaDetecciones(){return this.ListaDetecciones;}
    public String getNombreNuevaDeteccion(){return this.NombreNuevaDeteccion;}
    public Integer getDeteccionSeleccionada(){return this.DeteccionSeleccionada;}
    
    //  Setters
    public void setTipoDeDeteccionSeleccionada(EnumTipoDeteccion TipoDeteccion){this.TipoDeDeteccionSeleccionada = TipoDeteccion;}
    public void setTiposDeteccion(EnumTipoDeteccion[] TiposDeteccion){this.TiposDeteccion = TiposDeteccion;}
    public void setListaDetecciones(Map<Integer, String> ListaDetecciones){this.ListaDetecciones = ListaDetecciones;}
    public void setNombreNuevaDeteccion(String NombreNuevaDeteccion){this.NombreNuevaDeteccion = NombreNuevaDeteccion;}
    public void setDeteccionSeleccionada(Integer DeteccionSeleccionada){this.DeteccionSeleccionada = DeteccionSeleccionada;}
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        //  Detecciones
        TiposDeteccion = EnumTipoDeteccion.values();
        TipoDeDeteccionSeleccionada = EnumTipoDeteccion.INTERNA;
        this.ListaDetecciones = new HashMap<>();
        List<Deteccion> tmpDetecciones = fLectura.ListarDetecciones();
        Collections.sort(tmpDetecciones, Comparator.comparing(Deteccion::getNombre));
        tmpDetecciones.stream()
                .forEachOrdered(deteccion->{
                    ListaDetecciones.put(deteccion.getId(), deteccion.getNombre());
                });
    }
    
    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de deteccion.
     */
    public void actualizarDeteccion(){
        List<Deteccion> tmpDetecciones = fLectura.ListarDetecciones();
        this.ListaDetecciones.clear();
        Collections.sort(tmpDetecciones, Comparator.comparing(Deteccion::getNombre));
        tmpDetecciones.stream()
                .forEachOrdered(deteccion->{
                    ListaDetecciones.put(deteccion.getId(), deteccion.getNombre());
                });
    }
    
    /**
     * Crea nueva deteccion con el tipo interna/externa seleccionado.
     * Si no se crea se muestra un mensaje
     */
    public void nuevaDeteccion() {
        // Crear Nueva Deteccion y actualizar lista
        Deteccion det = fAdmin.NuevaDeteccion(NombreNuevaDeteccion, TipoDeDeteccionSeleccionada);
        if(det != null){
            actualizarDeteccion();
            this.DeteccionSeleccionada = det.getId();
            this.NombreNuevaDeteccion = new String();
            this.TipoDeDeteccionSeleccionada = det.getTipo();
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
