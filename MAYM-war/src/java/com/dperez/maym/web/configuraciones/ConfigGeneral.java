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
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

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

    private Part ArchivoAdjunto;

    @Inject
    private IOPropiedades ioProp;

    private Empresa empresaGuardada;

    private String nombre;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private String habilitacion;

    private boolean tls;
    private String puerto;
    private String host;
    private String from;
    private String usuario;
    private String password;

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

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Part getArchivoAdjunto() {
        return ArchivoAdjunto;
    }

    public void setArchivoAdjunto(Part ArchivoAdjunto) {
        this.ArchivoAdjunto = ArchivoAdjunto;
    }

    public Empresa getEmpresaGuardada() {
        return empresaGuardada;
    }

    public void setEmpresaGuardada(Empresa empresaGuardada) {
        this.empresaGuardada = empresaGuardada;
    }

    
    
    //</editor-fold>
    //<editor-fold desc="Metodos">
    public void guardarDatosEmpresa() throws IOException {
        Map<String, String> props = new HashMap<>();
        props.put("nombre", nombre);
        props.put("direccion", direccion);
        props.put("telefono", telefono);
        props.put("movil", movil);
        props.put("correo", correo);
        props.put("numHabilitacion", habilitacion);
        try {
            ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-datos", new FacesMessage(SEVERITY_INFO, "Guardado", "Los datos se guardaron correctamente."));
            FacesContext.getCurrentInstance().renderResponse();

            sesionUsuario.getEmpresa().setCorreo(correo);
            sesionUsuario.getEmpresa().setNombre(nombre);
            sesionUsuario.getEmpresa().setDireccion(direccion);
            sesionUsuario.getEmpresa().setTelefono(telefono);
            sesionUsuario.getEmpresa().setMovil(movil);
            sesionUsuario.getEmpresa().setNumHabilitacion(habilitacion);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-datos", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Los datos no se guardaron."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void guardarDatosCorreo() throws IOException {
        Map<String, String> props = new HashMap<>();
        props.put("mail_from", from);
        props.put("mail_user", usuario);
        props.put("mail_pass", password);
        props.put("mail_port", puerto);
        props.put("mail_tls", String.valueOf(tls));
        try {
            ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-grupo1", new FacesMessage(SEVERITY_INFO, "Guardado", "Los datos se guardaron correctamente."));
            FacesContext.getCurrentInstance().renderResponse();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-grupo1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Los datos no se guardaron."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void guardarLogo() throws IOException {
        if (ArchivoAdjunto != null) {
            String ubicacion = cArchivo.guardarLogoEmpresa(ArchivoAdjunto, empresaGuardada.getNombreDeArchivo());
            // datosAdjunto[0]: ubicacion | datosAdjunto[1]: extension
            if (!ubicacion.isEmpty()) {
                Map<String, String> props = new HashMap<>();
                props.put("logo_empresa", ubicacion);
                ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
                sesionUsuario.cargarEmpresa();
                empresaGuardada.setUbicacionLogo(ubicacion);
                FacesContext ctx = FacesContext.getCurrentInstance();
                String url = ctx.getExternalContext().getRequestContextPath();
                ctx.getExternalContext().redirect(url + "/Views/Configuraciones/ConfigGeneral.xhtml");
            }
            FacesContext.getCurrentInstance().addMessage("form_config_logo:input-logo", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el logo."));
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

        Properties prop = ManejadorPropiedades.getPropiedades(ioProp.getDirectorio());
        from = prop.get("mail_from") != null ? prop.get("mail_from").toString() : "";
        usuario = prop.get("mail_user") != null ? prop.get("mail_user").toString() : "";
        password = prop.get("mail_pass") != null ? prop.get("mail_pass").toString() : "";
        puerto = prop.get("mail_port") != null ? prop.get("mail_port").toString() : "";
        tls = prop.get("mail_tls") != null ? Boolean.parseBoolean(prop.get("mail_tls").toString()) : false;

    }

}
