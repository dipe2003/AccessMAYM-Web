/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.perfil;

import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeMain;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
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
public class PerfilUsuario implements Serializable {

    @Inject
    SesionUsuario sesionUsuario;

    private FacadeLectura fLectura;
    private FacadeMain fMain;

    private List<Accion> correctivas;
    private List<Accion> preventivas;
    private List<Accion> mejoras;

    private String nombre;
    private String apellido;
    private String correo;
    private Integer idAreaSeleccionada;
    private Area areaUsuario;
    private List<Area> areasDisponibles;
    private boolean recibeAlertas;

    private String actualPassword;
    private String nuevoPassword;
    private String repNuevoPassword;

    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        fMain = new FacadeMain();
        Usuario user = sesionUsuario.getUsuarioLogueado();
        correctivas = obtenerRegistrosRelacionados(fLectura.listarAccionesCorrectivas(), user);
        preventivas = obtenerRegistrosRelacionados(fLectura.listarAccionesPreventivas(), user);
        mejoras = obtenerRegistrosRelacionados(fLectura.listarAccionesMejoras(), user);

        // Datos de usuario
        nombre = user.getNombre();
        apellido = user.getApellido();
        areaUsuario = user.getAreaUsuario();
        correo = user.getCorreo();
        recibeAlertas = user.isRecibeAlertas();
        idAreaSeleccionada = user.getAreaUsuario().getId();
        areasDisponibles = fLectura.listarAreas();
    }

    //<editor-fold desc="Metodos">
    private List<Accion> obtenerRegistrosRelacionados(List<Accion> acciones, Usuario user) {
        List<Accion> registros = new ArrayList();
        acciones.stream()
                .filter(a -> a.getEstadoDeAccion() != Estado.CERRADA && a.getEstadoDeAccion() != Estado.DESESTIMADA)
                .toList()
                .stream().forEach(a -> {
                    if (a.estanImplementadasTodasActividades()) {
                        if (a.getComprobacionImplementacion() != null) {
                            if (a.getComprobacionImplementacion().getResponsableComprobacion().getUsuarioResponsable().getId() == user.getId()) {
                                registros.add(a);
                            }
                        }
                        if (!registros.contains(a) && a.getComprobacionEficacia() != null) {
                            if (a.getComprobacionEficacia().getResponsableComprobacion().getUsuarioResponsable().getId() == user.getId()) {
                                registros.add(a);
                            }
                        }
                    } else {
                        a.getActividadesDeAccion().forEach(act -> {
                            if (!registros.contains(a) && !act.estaImplementada()) {
                                if (act.getResponsableImplementacion().getUsuarioResponsable().getId() == user.getId()) {
                                    registros.add(a);
                                }
                            }
                        });
                    }
                });
        return registros;
    }

    public void guardarCambios() {
        int id = sesionUsuario.getUsuarioLogueado().getId();
        if (sesionUsuario.getUsuarioLogueado().getAreaUsuario().getId() != idAreaSeleccionada
                || sesionUsuario.getUsuarioLogueado().getNombre() != nombre || sesionUsuario.getUsuarioLogueado().getApellido() != apellido
                || sesionUsuario.getUsuarioLogueado().getCorreo() != correo || sesionUsuario.getUsuarioLogueado().isRecibeAlertas() == recibeAlertas) {
            FacesContext context = FacesContext.getCurrentInstance();
            if ((fMain.CambiarDatosUsuario(sesionUsuario.getUsuarioLogueado().getId(), nombre, apellido, correo, sesionUsuario.getUsuarioLogueado().getPermisoUsuario(),
                    recibeAlertas, idAreaSeleccionada)) != -1) {
                sesionUsuario.setUsuarioLogueado(fLectura.GetUsuario(id));
                context.addMessage("form_perfil_usuario:btn_guardar_datos", new FacesMessage(FacesMessage.SEVERITY_INFO, "Pefil Actualizado", "Se actualizaron los datos del perfil."));
                context.renderResponse();
            } else {
                context.addMessage("form_perfil_usuario:btn_guardar_datos", new FacesMessage(SEVERITY_FATAL, "No se pudo editar usuario", "No se pudo editar usuario"));
                context.renderResponse();
            }
        }
    }

    /**
     * Cambia las credenciales del usuario. Muestra los mensajes
     * correspondientes por cada error/informacion. Redirige a la pagina si se
     * cambio.
     */
    public void cambiarPassword() {
        int id = sesionUsuario.getUsuarioLogueado().getId();
        FacesContext context = FacesContext.getCurrentInstance();
        if (!fMain.ComprobarValidezPassword(id, actualPassword)) {
            context.addMessage("form_perfil_usuario:btn_cambiar_password", new FacesMessage(SEVERITY_FATAL, "No se pudo cambiar password", "El password actual no es correcto."));
            context.renderResponse();
        } else {
            if (nuevoPassword.equals(repNuevoPassword)) {
                if (!nuevoPassword.isEmpty()) {
                    if (fMain.ResetCredencialUsuario(id, nuevoPassword) != null) {
                        context.addMessage("form_perfil_usuario:btn_cambiar_password", new FacesMessage(SEVERITY_INFO, "Password Actualizado", "Se actualizó el password correctamente."));
                        context.renderResponse();
                    } else {
                        context.addMessage("form_perfil_usuario:btn_cambiar_password", new FacesMessage(SEVERITY_FATAL, "No se pudo cambiar password", "No se pudo cambiar password"));
                        context.renderResponse();
                    }
                } else {
                    context.addMessage("form_perfil_usuario:btn_cambiar_password", new FacesMessage(SEVERITY_FATAL, "El nuevo password esta vacio", "El nuevo password esta vacio"));
                    context.renderResponse();
                }
            } else {
                context.addMessage("form_perfil_usuario:btn_cambiar_password", new FacesMessage(SEVERITY_FATAL, "Los passwords no coniciden", "Los passwords no coniciden"));
                context.renderResponse();
            }
        }
    }

    //</editor-fold>
    //<editor-fold desc="Getters / Setters">
    public List<Accion> getCorrectivas() {
        return correctivas;
    }

    public void setCorrectivas(List<Accion> correctivas) {
        this.correctivas = correctivas;
    }

    public List<Accion> getPreventivas() {
        return preventivas;
    }

    public void setPreventivas(List<Accion> preventivas) {
        this.preventivas = preventivas;
    }

    public List<Accion> getMejoras() {
        return mejoras;
    }

    public void setMejoras(List<Accion> mejoras) {
        this.mejoras = mejoras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Area getAreaUsuario() {
        return areaUsuario;
    }

    public void setAreaUsuario(Area areaUsuario) {
        this.areaUsuario = areaUsuario;
    }

    public List<Area> getAreasDisponibles() {
        return areasDisponibles;
    }

    public void setAreasDisponibles(List<Area> areasDisponibles) {
        this.areasDisponibles = areasDisponibles;
    }

    public boolean isRecibeAlertas() {
        return recibeAlertas;
    }

    public void setRecibeAlertas(boolean recibeAlertas) {
        this.recibeAlertas = recibeAlertas;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getIdAreaSeleccionada() {
        return idAreaSeleccionada;
    }

    public void setIdAreaSeleccionada(Integer idAreaSeleccionada) {
        this.idAreaSeleccionada = idAreaSeleccionada;
    }

    public FacadeLectura getfLectura() {
        return fLectura;
    }

    public void setfLectura(FacadeLectura fLectura) {
        this.fLectura = fLectura;
    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void setActualPassword(String actualPassword) {
        this.actualPassword = actualPassword;
    }

    public String getNuevoPassword() {
        return nuevoPassword;
    }

    public void setNuevoPassword(String nuevoPassword) {
        this.nuevoPassword = nuevoPassword;
    }

    public String getRepNuevoPassword() {
        return repNuevoPassword;
    }

    public void setRepNuevoPassword(String reNuevoPassword) {
        this.repNuevoPassword = reNuevoPassword;
    }

    //</editor-fold>
}
