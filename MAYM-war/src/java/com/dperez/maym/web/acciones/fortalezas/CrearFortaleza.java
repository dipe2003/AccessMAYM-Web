/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.acciones.fortalezas;

import com.dperez.maym.web.configuraciones.ModalDetecciones;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Diego
 */
@Named
@ViewScoped
public class CrearFortaleza implements Serializable {

    @Inject
    SesionUsuario sesionUsuario;

    private FacadeLectura fLectura;
    private FacadeDatos fDatos;

    private ModalDetecciones modalDetecciones;

    private Date FechaDeteccion;
    private String strFechaDeteccion;

    private String Descripcion;

    private TipoDeteccion[] TiposDeteccion;
    private TipoDeteccion TipoDeDeteccionSeleccionada;

    private List<Deteccion> ListaDetecciones;
    private Integer DeteccionSeleccionada;

    private List<Area> ListaAreasSectores;
    private Integer AreaSectorAccionSeleccionada;

    //  Getters
    public Date getFechaDeteccion() {
        return FechaDeteccion;
    }

    public String getStrFechaDeteccion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaDeteccion == null) {
            return this.strFechaDeteccion;
        } else {
            return fDate.format(FechaDeteccion);
        }
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public TipoDeteccion getTipoDeDeteccionSeleccionada() {
        return this.TipoDeDeteccionSeleccionada;
    }

    public TipoDeteccion[] getTiposDeteccion() {
        return this.TiposDeteccion;
    }

    public List<Deteccion> getListaDetecciones() {
        return this.ListaDetecciones;
    }

    public Integer getDeteccionSeleccionada() {
        return this.DeteccionSeleccionada;
    }

    public List<Area> getListaAreasSectores() {
        return this.ListaAreasSectores;
    }

    public Integer getAreaSectorAccionSeleccionada() {
        return AreaSectorAccionSeleccionada;
    }

    //  Setters
    public void setFechaDeteccion(Date FechaDeteccion) {
        this.FechaDeteccion = FechaDeteccion;
    }

    public void setStrFechaDeteccion(String strFechaDeteccion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.FechaDeteccion = sdf.parse(strFechaDeteccion);
        } catch (ParseException ex) {
        }
        this.strFechaDeteccion = strFechaDeteccion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setTipoDeDeteccionSeleccionada(TipoDeteccion TipoDeteccion) {
        this.TipoDeDeteccionSeleccionada = TipoDeteccion;
    }

    public void setTiposDeteccion(TipoDeteccion[] TiposDeteccion) {
        this.TiposDeteccion = TiposDeteccion;
    }

    public void setListaDetecciones(List<Deteccion> ListaDetecciones) {
        this.ListaDetecciones = ListaDetecciones;
    }

    public void setDeteccionSeleccionada(Integer DeteccionSeleccionada) {
        this.DeteccionSeleccionada = DeteccionSeleccionada;
    }

    public void setListaAreaSectores(List<Area> ListaAreasSectores) {
        this.ListaAreasSectores = ListaAreasSectores;
    }

    public void setAreaSectorAccionSeleccionada(Integer AreaSectorAccionSeleccionada) {
        this.AreaSectorAccionSeleccionada = AreaSectorAccionSeleccionada;
    }

    //  Metodos
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init() {
        fDatos = new FacadeDatos();
        fLectura = new FacadeLectura();

        FacesContext context = FacesContext.getCurrentInstance();

        //  Detecciones
        modalDetecciones = context.getApplication().evaluateExpressionGet(context, "#{modalDetecciones}", ModalDetecciones.class);
        TiposDeteccion = TipoDeteccion.values();
        TipoDeDeteccionSeleccionada = TipoDeteccion.INTERNA;
        ListaDetecciones = modalDetecciones.getListaDetecciones();

        // Areas Sectores
        ListaAreasSectores = new ArrayList<>();
        ListaAreasSectores = fLectura.listarAreas().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de
     * deteccion.
     */
    public void actualizarDeteccion() {
        ListaDetecciones = modalDetecciones.getListaDetecciones();
    }

    /**
     * Crea la fortaleza con los datos ingresados. Si no se creo se muestra
     * mensaje de error. Si se creo se redirige a la pagina de edicion de
     * fortalezas.
     *
     * @throws java.io.IOException
     */
    public void crearFortaleza() throws IOException {

        Fortaleza fortaleza = fDatos.nuevaFortaleza(FechaDeteccion, Descripcion, DeteccionSeleccionada, 
                AreaSectorAccionSeleccionada, sesionUsuario.getUsuarioLogueado().getEmpresaUsuario().getId());

        if (fortaleza != null) {
            // redirigir a la lista de fortalezas.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/Views/Acciones/Fortalezas/ListarFortalezas.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage("form_accion:crear_fortaleza", new FacesMessage(SEVERITY_ERROR, "No se pudo crear la Fortaleza", "No se pudo crear la Fortaleza"));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
}
