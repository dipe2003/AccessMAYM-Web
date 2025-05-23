/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.empresa;

import com.dperez.maym.web.herramientas.ManejadorPropiedades;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.herramientas.IOPropiedades;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
public class AdminEmpresa implements Serializable {

    @Inject
    private IOPropiedades ioProp;
    @Inject
    private SesionUsuario sesion;

    private FacadeAdministrador fAdmin = new FacadeAdministrador();

    private String nombre;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private String nombreExtra;

    //<editor-fold desc="getter y setters">
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

    public String getNombreExtra() {
        return nombreExtra;
    }

    public void setNombreExtra(String nombreExtra) {
        this.nombreExtra = nombreExtra;
    }
    //</editor-fold>

    public void registrarEmpresa() throws IOException {
        if (fAdmin.nuevaEmpresa(nombre, direccion, telefono, movil, correo, nombreExtra).getId() > 0) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(FacesContext.getCurrentInstance()
                            .getExternalContext().getRequestContextPath() + "/index.xhtml");
        }

    }
}
