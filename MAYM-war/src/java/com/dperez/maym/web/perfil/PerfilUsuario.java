/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.perfil;

import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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

    private List<Accion> correctivas;
    private List<Accion> preventivas;
    private List<Accion> mejoras;

    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        Usuario user = sesionUsuario.getUsuarioLogueado();        
        correctivas = obtenerRegistrosRelacionados(fLectura.listarAccionesCorrectivas(), user);
        preventivas =   obtenerRegistrosRelacionados(fLectura.listarAccionesPreventivas(),user);
        mejoras = obtenerRegistrosRelacionados(fLectura.listarAccionesMejoras(), user);
        
    }

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

    //</editor-fold>
}
