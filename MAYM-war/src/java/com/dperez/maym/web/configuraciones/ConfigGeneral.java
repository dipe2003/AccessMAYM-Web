/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.configuraciones;

import com.dperez.maymweb.modelo.empresa.OpcionesSistema;
import com.dperez.maymweb.modelo.empresa.Empresa;
import com.dperez.maym.web.herramientas.CargarArchivo;
import com.dperez.maym.web.herramientas.ManejadorPropiedades;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.herramientas.IOPropiedades;
import com.dperez.maymweb.modelo.empresa.OpcionesApariencia;
import com.dperez.maymweb.modelo.empresa.OpcionesCorreo;
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

    private FacadeAdministrador fAdmin = new FacadeAdministrador();

    @Inject
    private CargarArchivo cArchivo;

    private Part ArchivoAdjunto;
    private Part ArchivoLogoInforme;
    @Inject
    private IOPropiedades ioProp;

    private OpcionesApariencia ops;
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
    private String nombreExtra;

    private boolean tls;
    private boolean activarAlertas;
    private int puerto;
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

    public String getNombreExtra() {
        return nombreExtra;
    }

    public void setNombreExtra(String nombreExtra) {
        this.nombreExtra = nombreExtra;
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

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
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

    public OpcionesApariencia getOps() {
        return ops;
    }

    public void setOps(OpcionesApariencia ops) {
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
        if (fAdmin.cambiarDatosEmpresa(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId(), nombre, direccion, telefono, movil, correo, nombreExtra) > 0) {
            sesionUsuario.cargarEmpresa();
            sesionUsuario.cargarColores();
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-datos", new FacesMessage(SEVERITY_INFO, "Guardado", "Los datos se guardaron correctamente."));
            FacesContext.getCurrentInstance().renderResponse();
        } else {
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-datos", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Los datos no se guardaron."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void guardarDatosCorreo() throws IOException {
        if (fAdmin.setConfiguracionCorreo(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId(), from, usuario, password, host, puerto, tls) > 0) {
            fAdmin.setAlertas(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId(), activarAlertas);
            sesionUsuario.cargarEmpresa();
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-grupo1", new FacesMessage(SEVERITY_INFO, "Guardado", "Los datos se guardaron correctamente."));
            FacesContext.getCurrentInstance().renderResponse();
        } else {
            FacesContext.getCurrentInstance().addMessage("form_config_general:boton-guardar-grupo1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Los datos no se guardaron."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void guardarLogo() throws IOException {
        if (ArchivoAdjunto != null) {
            String ubicacion = cArchivo.guardarLogoEmpresa(ArchivoAdjunto, sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getNombreDeArchivo());
            // datosAdjunto[0]: ubicacion | datosAdjunto[1]: extension
            if (!ubicacion.isEmpty()) {
                Map<String, String> props = new HashMap<>();
                props.put("logo_empresa", ubicacion);
                ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
                sesionUsuario.cargarEmpresa();
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
            String ubicacion = cArchivo.guardarLogoEmpresa(ArchivoLogoInforme, "_informes_" + sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getNombreDeArchivo());
            // datosAdjunto[0]: ubicacion | datosAdjunto[1]: extension
            if (!ubicacion.isEmpty()) {
                Map<String, String> props = new HashMap<>();
                props.put("logo_informes_empresa", ubicacion);
                ManejadorPropiedades.setPropiedades(ioProp.getDirectorio(), props);
                sesionUsuario.cargarEmpresa();                
                FacesContext ctx = FacesContext.getCurrentInstance();
                String url = ctx.getExternalContext().getRequestContextPath();
                ctx.getExternalContext().redirect(url + "/Views/Configuraciones/ConfigGeneral.xhtml");
            }
            FacesContext.getCurrentInstance().addMessage("form_color:input-logo-informes", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el logo."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void guardarColor() throws IOException {
        if (fAdmin.setConfiguracionApariencia(sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId(), colorSuperior, colorInferior,
                colorFuenteEncabezado, colorTitulo, colorFuenteTitulo, colorFondo, colorBoton) > 0) {
            sesionUsuario.cargarColores();
            FacesContext ctx = FacesContext.getCurrentInstance();
            String url = ctx.getExternalContext().getRequestContextPath();
            ctx.getExternalContext().redirect(url + "/Views/Configuraciones/ConfigGeneral.xhtml");
        }
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        Empresa empresaGuardada = sesionUsuario.getUsuarioLogueado().getEmpresaUsuario();

        nombre = empresaGuardada.getNombre();
        direccion = empresaGuardada.getDireccion();
        telefono = empresaGuardada.getTelefono();
        movil = empresaGuardada.getMovil();
        correo = empresaGuardada.getCorreo();
        nombreExtra = empresaGuardada.getNombreExtra();
        
        OpcionesCorreo opsMail = empresaGuardada.getOpcionesSistema().getOpcionesCorreo();
        
        from = opsMail.getMailFrom();
        usuario = opsMail.getMailUser();
        password = opsMail.getMailPass();
        puerto = opsMail.getMailPort();
        tls = opsMail.isMailTLS();
        host = opsMail.getMailHostSMTP();
        activarAlertas = opsMail.isAlertasActivadas();

        ops = empresaGuardada.getOpcionesSistema().getOpcionesApariencia();
        colorSuperior = ops.getColorSuperiorPanelTitulo();
        colorFuenteEncabezado = ops.getColorFuentePanelEncabezado();
        colorInferior = ops.getColorInferiorPanelTitulo();
        colorTitulo = ops.getColorPanelTitulo();
        colorFuenteTitulo = ops.getColorFuentePanelTitulo();
        colorFondo = ops.getColorBody();
        colorBoton = ops.getColorBoton();
    }

}
