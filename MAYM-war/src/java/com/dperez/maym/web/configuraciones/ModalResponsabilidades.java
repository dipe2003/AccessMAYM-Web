/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ModalResponsabilidades implements Serializable {
    
    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    
    private Usuario usuarioSeleccionado;
    private List<Responsabilidad> listaResponsabilidades;
    private List<Responsabilidad> listaResponsabilidadesSeleccionadas;
    private int idResponsabilidadSeleccionada;
    
    //<editor-fold desc="Getters">
    public List<Responsabilidad> getListaResponsabilidades(){return this.listaResponsabilidades;    }
    
    public List<Responsabilidad> getListaResponsabilidadesSeleccionadas() {
        return listaResponsabilidadesSeleccionadas;
    }
    public int getIdResponsabilidadSeleccionada() {
        return idResponsabilidadSeleccionada;
    }
    
    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setListaResponsabilidades(List<Responsabilidad> ListaResponsabilidades){this.listaResponsabilidades = ListaResponsabilidades;}
    
    public void setListaResponsabilidadesSeleccionadas(List<Responsabilidad> listaResponsabilidadesSeleccionadas) {
        this.listaResponsabilidadesSeleccionadas = listaResponsabilidadesSeleccionadas;
    }
    
    public void setIdResponsabilidadSeleccionada(int idResponsabilidadSeleccionada) {
        this.idResponsabilidadSeleccionada = idResponsabilidadSeleccionada;
    }
    
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        
        this.listaResponsabilidades = new ArrayList<>();
        this.listaResponsabilidadesSeleccionadas = new ArrayList<>();
    }
    
    /**
     * Actualiza la lista de responsabilidades segun las que ya contenga el usuario.
     * @param idUsuarioSeleccionado
     */
    public void actualizarResponsabilidadesSeleccionadas(int idUsuarioSeleccionado){
        this.usuarioSeleccionado = fLectura.GetUsuario(idUsuarioSeleccionado);
        
        this.listaResponsabilidades.clear();
        listaResponsabilidades = fLectura.listarResponsabilidades();
        this.listaResponsabilidadesSeleccionadas.clear();
        if(!usuarioSeleccionado.getResponsablesUsuario().isEmpty()){
            listaResponsabilidadesSeleccionadas= listaResponsabilidades.stream()
                    .filter(r-> usuarioSeleccionado.getResponsablesUsuario().stream()
                            .anyMatch(resp -> resp.getResponsabilidadResponsable().getId() == r.getId()))
                    .collect(Collectors.toList());
        }
        listaResponsabilidadesSeleccionadas.stream().sorted();
        listaResponsabilidades.stream().sorted();
    }
    
    public boolean estaSeleccionada(Responsabilidad responsabilidad){
        return this.listaResponsabilidadesSeleccionadas.stream()
                .anyMatch(r->r.getId() == responsabilidad.getId());
    }
    
    /**
     * Agrega la responsabilidad seleccionada a la lista de seleccionadas y la quita
     * de la lista de disponibles.
     */
    public void agregarResponsabilidad(){
        Responsabilidad responsabilidad = listaResponsabilidades.stream()
                .filter(r->r.getId()== idResponsabilidadSeleccionada)
                .findFirst()
                .get();
        listaResponsabilidadesSeleccionadas.add(responsabilidad);
        listaResponsabilidades.remove(responsabilidad);
    }
    
    /**
     * Quita la responsabilidad seleccionada a la lista de seleccionadas y la quita
     * de las disponibles.
     */
    public void quitarResponsabilidad(){
        Responsabilidad responsabilidad = listaResponsabilidadesSeleccionadas.stream()
                .filter(r->r.getId()== idResponsabilidadSeleccionada)
                .findFirst()
                .get();
        listaResponsabilidades.add(responsabilidad);
        listaResponsabilidadesSeleccionadas.remove(responsabilidad);
    }
    
    //TODO: falta caso de eliminar responsabilidad
    public void guardarCambios(){
        try{
            if(!listaResponsabilidadesSeleccionadas.isEmpty()){
                listaResponsabilidadesSeleccionadas.stream()
                        .forEach(r->{
                            if(usuarioSeleccionado.getResponsablesUsuario().stream()
                                    .anyMatch(responsable->responsable.getResponsabilidadResponsable().getId() == r.getId()) == false){
                                fAdmin.nuevoResponsable(r.getId(), usuarioSeleccionado.getId());
                            }
                        });
                FacesContext.getCurrentInstance().addMessage("form_accion_modal_responsabilidades:btn_guardar_responsabilidades",
                        new FacesMessage(SEVERITY_INFO,  "OK", "Las responsabilidaes se agregaron correctamente." ));
                FacesContext.getCurrentInstance().renderResponse();
            }
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage("form_accion_modal_responsabilidades:btn_guardar_responsabilidades",
                    new FacesMessage(SEVERITY_INFO,  "Error", "Ocurrio un error." ));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    //</editor-fold>
}
