/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.inicio;

import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeMain;
import com.dperez.maymweb.herramientas.IOPropiedades;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.empresa.OpcionesApariencia;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private OpcionesApariencia opsApariencia;

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

    public OpcionesApariencia getOpsApariencia() {
        return opsApariencia;
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

    // Setters
    public void setUsuarioLogueado(Usuario UsuarioLogueado) {
        this.UsuarioLogueado = UsuarioLogueado;
    }

    public void setOpsApariencia(OpcionesApariencia opsApariencia) {
        this.opsApariencia = opsApariencia;
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

    //  Metodos
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        filtrosAplicados = new ArrayList<>();

        fLectura = new FacadeLectura();
        facadeMain = new FacadeMain();
        cargarColores();
        if (fLectura.listarEmpresas().isEmpty()) {
            String url = context.getExternalContext().getRequestContextPath();
            try {
                context.getExternalContext().redirect(url + "/Views/Empresas/AdminEmpresa.xhtml");
            } catch (Exception ex) {
                ex.getMessage();
            }
        } else {
            this.Usuarios = new HashMap<>();
            // llenar la lista de usuarios que no se hayan dado de baja.
            Usuarios = fLectura.listarUsuarios(false).stream()
                    .sorted()
                    .collect(Collectors.toMap(Usuario::getId, usuario -> usuario));
        }
    }

    public void cargarColores() {
        try {
            opsApariencia = this.UsuarioLogueado.getEmpresaUsuario().getOpcionesSistema().getOpcionesApariencia();
        } catch (NullPointerException ex) {
            opsApariencia = new OpcionesApariencia();
        }
    }

    public void ingresar() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            if (facadeMain.ComprobarValidezPassword(Integer.valueOf(UsuarioSeleccionado), PasswordUsuario)) {
                Usuario usuario = fLectura.getUsuario(Integer.valueOf(UsuarioSeleccionado));
                if (!usuario.isVigente()) {
                    context.addMessage("formlogin:usr", new FacesMessage(SEVERITY_FATAL, "Usuario no vigente", "El usuario no está habilitado."));
                    context.renderResponse();
                } else {
                    request.getSession().setAttribute("Usuario", usuario);

                    this.UsuarioLogueado = usuario;
                    this.PasswordUsuario = new String();
                    cargarColores();
                    String url = context.getExternalContext().getRequestContextPath();
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

    public void logout() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.getSession().invalidate();
            this.UsuarioLogueado = null;
            cargarColores();
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

    public void cargarEmpresa() {
        this.UsuarioLogueado.setEmpresaUsuario(fLectura.getEmpresa(this.UsuarioLogueado.getEmpresaUsuario().getId()));
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
