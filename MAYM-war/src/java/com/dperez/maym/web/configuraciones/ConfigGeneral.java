/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.configuraciones;

import com.dperez.maym.web.empresa.Empresa;
import com.dperez.maym.web.herramientas.CargarArchivo;
import com.dperez.maym.web.herramientas.ManejadorPropiedades;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.herramientas.IOPropiedades;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class ConfigGeneral implements Serializable {

    @Inject
    SesionUsuario sesionUsuario;

    @Inject
    private CargarArchivo cArchivo;

    @Inject
    private IOPropiedades ioProp;

    private Empresa empresaGuardada;

    private String nombre;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private String habilitacion;

    //<editor-fold desc="Setters / Getters">
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getHabilitacion() {
        return habilitacion;
    }

    public void setHabilitacion(String habilitacion) {
        this.habilitacion = habilitacion;
    }
    //</editor-fold>

    //<editor-fold desc="Metodos">
    
    public void guardarDatosEmpresa()throws IOException{     
        Map<String, String> props = new HashMap<>();
        props.put("nombre", nombre);
        props.put("direccion", direccion);
        props.put("telefono", telefono);
        props.put("movil", movil);
        props.put("correo", correo);
        props.put("numHabilitacion", habilitacion);
        try{            
            ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-datos", new FacesMessage(SEVERITY_INFO, "Guardado" ,"Los datos se guardaron correctamente." ));
            FacesContext.getCurrentInstance().renderResponse();
            
            sesionUsuario.getEmpresa().setCorreo(correo);
            sesionUsuario.getEmpresa().setNombre(nombre);
            sesionUsuario.getEmpresa().setDireccion(direccion);
            sesionUsuario.getEmpresa().setTelefono(telefono);
            sesionUsuario.getEmpresa().setMovil(movil);
            sesionUsuario.getEmpresa().setNumHabilitacion(habilitacion);

        }catch(Exception ex){
            ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-datos", new FacesMessage(SEVERITY_INFO, "Error", "Los datos se guardaron correctamente." ));
            FacesContext.getCurrentInstance().renderResponse();
        }        
    }
    
    //</editor-fold>
    @PostConstruct
    public void init() {
        empresaGuardada = sesionUsuario.getEmpresa();

        nombre = empresaGuardada.getNombre();
        direccion = empresaGuardada.getDireccion();
        telefono = empresaGuardada.getTelefono();
        movil = empresaGuardada.getMovil();
        correo = empresaGuardada.getCorreo();
        habilitacion = empresaGuardada.getNumHabilitacion();
    }

}
