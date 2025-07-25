/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.inicio;

import com.dperez.maym.web.empresa.Empresa;
import com.dperez.maym.web.herramientas.ManejadorPropiedades;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeMain;
import com.dperez.maymweb.herramientas.IOPropiedades;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dperez
 */
@Named("sesionUsuario")
@ManagedBean
@SessionScoped
public class SesionUsuario implements Serializable {

    private FacadeMain facadeMain;
    private FacadeLectura fLectura;

    @Inject
    private IOPropiedades ioProp;

    private Empresa empresa;

    private String UsuarioSeleccionado;
    private String PasswordUsuario;

    private String NombreUsuario;
    private Map<Integer, Usuario> Usuarios;

    private Usuario UsuarioLogueado;  
    
     // Filtros
    private List<String> filtrosAplicados;
    private String[] areasSeleccionadas;
    private String[] deteccionesSeleccionadas;
    private Estado[] estadosSeleccionados;
    private String[] codificacionesSeleccionadas;
    private TipoAccion tipoAccionListada;

    // Sesion
    // Geters
    public Usuario getUsuarioLogueado() {
        return UsuarioLogueado;
    }

    // Setters
    public void setUsuarioLogueado(Usuario UsuarioLogueado) {
        this.UsuarioLogueado = UsuarioLogueado;
    }

    // Metodos
    public Date getFechaActual() {
        return new Date();
    }

    // Login
    //  Getters
    public String getUsuarioSeleccionado() {
        return UsuarioSeleccionado;
    }

    public void setPasswordUsuario(String PasswordUsuario) {
        this.PasswordUsuario = PasswordUsuario;
    }

    public String getNombreUsuario() {
        return this.NombreUsuario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<String> getFiltrosAplicados() {
        return filtrosAplicados;
    }

    public String[] getAreasSeleccionadas() {
        return areasSeleccionadas;
    }

    public String[] getDeteccionesSeleccionadas() {
        return deteccionesSeleccionadas;
    }

    public Estado[] getEstadosSeleccionados() {
        return estadosSeleccionados;
    }

    public String[] getCodificacionesSeleccionadas() {
        return codificacionesSeleccionadas;
    }

    public TipoAccion getTipoAccionListada() {
        return tipoAccionListada;
    }

    //  Setters
    public void setUsuarioSeleccionado(String UsuarioSeleccionado) {
        this.UsuarioSeleccionado = UsuarioSeleccionado;
    }

    public String getPasswordUsuario() {
        return PasswordUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public void setFiltrosAplicados(List<String> filtrosAplicados) {
        this.filtrosAplicados = filtrosAplicados;
    }

    public void setAreasSeleccionadas(String[] areasSeleccionadas) {
        this.areasSeleccionadas = areasSeleccionadas;
    }

    public void setDeteccionesSeleccionadas(String[] deteccionesSeleccionadas) {
        this.deteccionesSeleccionadas = deteccionesSeleccionadas;
    }

    public void setEstadosSeleccionados(Estado[] estadosSeleccionados) {
        this.estadosSeleccionados = estadosSeleccionados;
    }

    public void setCodificacionesSeleccionadas(String[] codificacionesSeleccionadas) {
        this.codificacionesSeleccionadas = codificacionesSeleccionadas;
    }

    public void setTipoAccionListada(TipoAccion tipoAccionListada) {
        this.tipoAccionListada = tipoAccionListada;
    }

    //  Metodos
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        facadeMain = new FacadeMain();

        cargarEmpresa();
        this.Usuarios = new HashMap<>();
        // llenar la lista de usuarios que no se hayan dado de baja.
        Usuarios = fLectura.listarUsuarios(false).stream()
                .sorted()
                .collect(Collectors.toMap(Usuario::getId, usuario -> usuario));
        if (Usuarios.isEmpty()) {
            try {
                String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/Views/Configuraciones/Usuarios.xhtml");
            } catch (IOException ex) {
            }
        }

        cargarColores();

    }

    public void cargarColores() {
        
        Properties prop = new Properties();
        if (!ManejadorPropiedades.getPropiedades(ioProp.getDirectorio()).isEmpty()) {
            prop = ManejadorPropiedades.getPropiedades(ioProp.getDirectorio());
        }
        empresa.getOpcionesSistema().setColorSuperiorPanelTitulo(prop.getProperty("color-superior") != null ? prop.getProperty("color-superior") : "#2a2a2a");
        empresa.getOpcionesSistema().setColorInferiorPanelTitulo(prop.getProperty("color-inferior") != null ? prop.getProperty("color-inferior") : "black");
        empresa.getOpcionesSistema().setColorPanelTitulo(prop.getProperty("color-titulos") != null ? prop.getProperty("color-titulos") : "#337ab7");
        empresa.getOpcionesSistema().setColorFuentePanelEncabezado(prop.getProperty("color-fuente-encabezado") != null ? prop.getProperty("color-fuente-encabezado") : "#cce8f6");
        empresa.getOpcionesSistema().setColorFuentePanelTitulo(prop.getProperty("color-fuente-titulos") != null ? prop.getProperty("color-fuente-titulos") : "#cce8f6");
        empresa.getOpcionesSistema().setColorBody(prop.getProperty("color-body") != null ? prop.getProperty("color-body") : "#ffffff");
        empresa.getOpcionesSistema().setColorBoton(prop.getProperty("color-boton") != null ? prop.getProperty("color-boton") : "#337ab7");
    }

    public void ingresar() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        //RequestHeaderMap.key("Referer"): contiene la url desde donde se hace el pedido.
        String url = context.getExternalContext().getRequestHeaderMap().get((String) ("Referer"));
        try {
            if (facadeMain.ComprobarValidezPassword(Integer.valueOf(UsuarioSeleccionado), PasswordUsuario)) {
                Usuario usuario = fLectura.GetUsuario(Integer.valueOf(UsuarioSeleccionado));
                if (!usuario.isVigente()) {
                    context.addMessage("formlogin:usr", new FacesMessage(SEVERITY_FATAL, "Usuario no vigente", "El usuario no está habilitado."));
                    context.renderResponse();
                } else {
                    request.getSession().setAttribute("Usuario", usuario);

                    this.UsuarioLogueado = usuario;
                    this.PasswordUsuario = new String();
                    cargarColores();
                    url = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(url + "/index.xhtml");
                }
            } else {
                context.addMessage("formlogin:pwd", new FacesMessage(SEVERITY_FATAL, "No Existe Usuario", "Los datos del usuario no son correctos"));
                context.renderResponse();
            }
        } catch (Exception ex) {
            context.addMessage("formlogin:pwd", new FacesMessage(SEVERITY_FATAL, "No Existe Usuario", "Los datos del usuario no son correctos"));
            context.renderResponse();
        }
    }

    public void cargarEmpresa() {
        if (!ManejadorPropiedades.getPropiedades(ioProp.getDirectorio()).isEmpty()) {
            Properties prop = ManejadorPropiedades.getPropiedades(ioProp.getDirectorio());
            empresa = new Empresa(prop.getProperty("nombre"),
                    prop.getProperty("direccion"),
                    prop.getProperty("telefono"),
                    prop.getProperty("movil"),
                    prop.getProperty("correo"),
                    prop.getProperty("nombreExtra"));
            if (prop.get("logo_empresa") != null) {
                empresa.setUbicacionLogo(prop.getProperty("logo_empresa"));
            }
            if (prop.get("logo_informes_empresa") != null) {
                empresa.setUbicacionLogoInformes(prop.getProperty("logo_informes_empresa"));
            }
        } else {
            try {
                String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/Views/Empresas/AdminEmpresa.xhtml");
            } catch (IOException ex) {
            }
        }
    }

    public void logout() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.getSession().invalidate();
            this.UsuarioLogueado = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (Exception ex) {
            System.out.println("Error en logout: " + ex.getMessage());
        }
    }

    /**
     * Comprueba la existencia del usuario y devuelve si existe.
     */
    public void comprobarIdUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            int id = Integer.valueOf(UsuarioSeleccionado);
            if (id != 0 && facadeMain.ExisteUsuario(id)) {
                Usuario usuario = Usuarios.get(id);
                NombreUsuario = usuario.getNombreCompleto();
            } else {
                context.addMessage("formlogin:usr", new FacesMessage(SEVERITY_FATAL, "No Existe Usuario", "Los datos del usuario no son correctos"));
                context.renderResponse();
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            NombreUsuario = "";
            context.addMessage("formlogin:usr", new FacesMessage(SEVERITY_FATAL, "No Existe Usuario", "Los datos del usuario no son correctos"));
            context.renderResponse();
        }
    }
    
    //<editor-fold desc="Filtros">
    public void addFiltro(String filtro) {
        filtrosAplicados.add(filtro);
    }

    public void removeFiltro(String filtro) {
        filtrosAplicados.remove(filtro);
    }

    public boolean contieneFiltro(String filtro) {
        return filtrosAplicados.contains(filtro);
    }
    //</editor-fold>
}
