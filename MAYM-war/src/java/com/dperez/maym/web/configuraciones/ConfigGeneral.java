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
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
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
    private Part ArchivoLogoInforme;
    @Inject
    private IOPropiedades ioProp;

    private Empresa empresaGuardada;

    private OpcionesSistema ops;
    private String colorSuperior;
    private String colorInferior;
    private String colorFuenteEncabezado;
    private String colorTitulo;
    private String colorFuenteTitulo;
    private String colorFondo;
    private String colorBoton;

    private String nombre;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private String habilitacion;

    private boolean tls;
    private boolean activarAlertas;
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

    public boolean isActivarAlertas() {
        return activarAlertas;
    }

    public void setActivarAlertas(boolean activarAlertas) {
        this.activarAlertas = activarAlertas;
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

    public Part getArchivoLogoInforme() {
        return ArchivoLogoInforme;
    }

    public void setArchivoLogoInforme(Part ArchivoLogoInforme) {
        this.ArchivoLogoInforme = ArchivoLogoInforme;
    }

    public Empresa getEmpresaGuardada() {
        return empresaGuardada;
    }

    public void setEmpresaGuardada(Empresa empresaGuardada) {
        this.empresaGuardada = empresaGuardada;
    }

    public OpcionesSistema getOps() {
        return ops;
    }

    public void setOps(OpcionesSistema ops) {
        this.ops = ops;
    }

    public String getColorSuperior() {
        return colorSuperior;
    }

    public void setColorSuperior(String colorSuperior) {
        this.colorSuperior = colorSuperior;
    }

    public String getColorInferior() {
        return colorInferior;
    }

    public void setColorInferior(String colorInferior) {
        this.colorInferior = colorInferior;
    }

    public String getColorTitulo() {
        return colorTitulo;
    }

    public void setColorTitulo(String colorTitulo) {
        this.colorTitulo = colorTitulo;
    }

    public String getColorFuenteEncabezado() {
        return colorFuenteEncabezado;
    }

    public void setColorFuenteEncabezado(String colorFuenteEncabezado) {
        this.colorFuenteEncabezado = colorFuenteEncabezado;
    }

    public String getColorFuenteTitulo() {
        return colorFuenteTitulo;
    }

    public void setColorFuenteTitulo(String colorFuenteTitulo) {
        this.colorFuenteTitulo = colorFuenteTitulo;
    }

    public String getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(String colorFondo) {
        this.colorFondo = colorFondo;
    }

    public String getColorBoton() {
        return colorBoton;
    }

    public void setColorBoton(String colorBoton) {
        this.colorBoton = colorBoton;
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
        props.put("alertas_on", String.valueOf(activarAlertas));
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

    public void guardarLogoInformes() throws IOException {
        if (ArchivoLogoInforme != null) {
            String ubicacion = cArchivo.guardarLogoEmpresa(ArchivoLogoInforme, "_informes_" + empresaGuardada.getNombreDeArchivo());
            // datosAdjunto[0]: ubicacion | datosAdjunto[1]: extension
            if (!ubicacion.isEmpty()) {
                Map<String, String> props = new HashMap<>();
                props.put("logo_informes_empresa", ubicacion);
                ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
                sesionUsuario.cargarEmpresa();
                empresaGuardada.setUbicacionLogo(ubicacion);
                FacesContext ctx = FacesContext.getCurrentInstance();
                String url = ctx.getExternalContext().getRequestContextPath();
                ctx.getExternalContext().redirect(url + "/Views/Configuraciones/ConfigGeneral.xhtml");
            }
            FacesContext.getCurrentInstance().addMessage("form_color:input-logo-informes", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el logo."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void guardarColor() throws IOException {
        Map<String, String> props = new HashMap<>();
        props.put("color-superior", !colorSuperior.isEmpty() ? colorSuperior : ops.getColorSuperiorPanelTitulo());
        props.put("color-inferior", !colorInferior.isEmpty() ? colorInferior : ops.getColorInferiorPanelTitulo());
        props.put("color-titulos", !colorTitulo.isEmpty() ? colorTitulo : ops.getColorPanelTitulo());
        props.put("color-fuente-encabezado", !colorFuenteEncabezado.isEmpty() ? colorFuenteEncabezado : ops.getColorFuentePanelEncabezado());
        props.put("color-fuente-titulos", !colorFuenteTitulo.isEmpty() ? colorFuenteTitulo : ops.getColorFuentePanelTitulo());
        props.put("color-body", !colorFondo.isEmpty() ? colorFondo : ops.getColorBody());
        props.put("color-boton", !colorBoton.isEmpty() ? colorBoton : ops.getColorBoton());
        ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
        sesionUsuario.cargarColores();
        FacesContext ctx = FacesContext.getCurrentInstance();
        String url = ctx.getExternalContext().getRequestContextPath();
        ctx.getExternalContext().redirect(url + "/Views/Configuraciones/ConfigGeneral.xhtml");
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
        activarAlertas = prop.get("alertas_on") != null ? Boolean.parseBoolean(prop.get("alertas_on").toString()) : false;

        ops = empresaGuardada.getOpcionesSistema();
        colorSuperior = ops.getColorSuperiorPanelTitulo();
        colorFuenteEncabezado = ops.getColorFuentePanelEncabezado();
        colorInferior = ops.getColorInferiorPanelTitulo();
        colorTitulo = ops.getColorPanelTitulo();
        colorFuenteTitulo = ops.getColorFuentePanelTitulo();
        colorFondo = ops.getColorBody();
        colorBoton = ops.getColorBoton();
    }

}
