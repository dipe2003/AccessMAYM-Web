/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.acciones.fortalezas;

import com.dperez.maym.web.configuraciones.ModalDetecciones;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.facades.FacadeAdministrador;
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
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@ViewScoped
public class EditarFortaleza implements Serializable {

    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    private FacadeDatos fDatos;

    private ModalDetecciones modalDetecciones;

    private int IdFortaleza;
    
    private Fortaleza FortalezaSeleccionada;

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
    public int getIdFortaleza() {
        return IdFortaleza;
    }

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

    public Fortaleza getFortalezaSeleccionada() {
        return FortalezaSeleccionada;
    }

    //  Setters
    public void serIdAccionCorrectiva(int IdAccionCorrectiva) {
        this.IdFortaleza = IdAccionCorrectiva;
    }

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
    public void setFortalezaSeleccionada(Fortaleza FortalezaSeleccionada) {
        this.FortalezaSeleccionada = FortalezaSeleccionada;
    }
    //  Metodos
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        fDatos = new FacadeDatos();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        // recuperar el id para llenar datos de la accion de mejora y el resto de las propiedades.
        IdFortaleza = Integer.parseInt(request.getParameter("id"));
        if (IdFortaleza != 0) {
            FortalezaSeleccionada = fLectura.getFortaleza(IdFortaleza);
            FechaDeteccion = FortalezaSeleccionada.getFechaDeteccion();
            Descripcion = FortalezaSeleccionada.getDescripcion();

            //  Detecciones
            modalDetecciones = context.getApplication().evaluateExpressionGet(context, "#{modalDetecciones}", ModalDetecciones.class);
            TiposDeteccion = TipoDeteccion.values();
            ListaDetecciones = modalDetecciones.getListaDetecciones();
            TipoDeDeteccionSeleccionada = FortalezaSeleccionada.getDeteccionFortaleza().getTipoDeDeteccion();
            DeteccionSeleccionada = FortalezaSeleccionada.getDeteccionFortaleza().getId();

            // Areas Sectores
            ListaAreasSectores = new ArrayList<>();

            ListaAreasSectores = fLectura.listarAreas().stream()
                    .sorted()
                    .collect(Collectors.toList());
            AreaSectorAccionSeleccionada = FortalezaSeleccionada.getAreaFortaleza().getId();
        }

    }

    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de
     * deteccion.
     */
    public void actualizarDeteccion() {
        ListaDetecciones = modalDetecciones.getListaDetecciones();
    }

    /**
     * Actualiza la fortaleza con los datos nuevos. Si no se actualizo se
     * muestra mensaje de error. Si se creo se redirige a la pagina de listado
     * de fortalezas.
     *
     * @throws java.io.IOException
     */
    public void editarFortaleza() throws IOException {
        // actualizar fortaleza
        if (fDatos.editarFortaleza(IdFortaleza, FechaDeteccion, Descripcion, DeteccionSeleccionada, AreaSectorAccionSeleccionada) == -1) {
            // Si no se actualizo muestra mensaje de error.
            FacesContext.getCurrentInstance().addMessage("form_accion:guardar_fortaleza", new FacesMessage(SEVERITY_INFO, "No se pudo editar la fortaleza", "No se pudo editar la fortaleza"));
            FacesContext.getCurrentInstance().renderResponse();
        } else {
            FacesContext.getCurrentInstance().addMessage("form_accion:guardar_fortaleza", new FacesMessage(SEVERITY_INFO, "Los datos se guardaron.", "Los datos se guardaron."));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    /**
     * Elimina la fortaleza de la base de datos.
     *
     * @throws java.io.IOException
     */
    public void eliminarFortaleza() throws IOException {
        if (fAdmin.eliminarFortaleza(IdFortaleza) == -1) {
            // Si no se elimino muestra mensaje de error.
            FacesContext.getCurrentInstance().addMessage("form_accion:eliminar_fortaleza", new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar la Accion", "No se pudo eliminar la Accion"));
            FacesContext.getCurrentInstance().renderResponse();
        } else {
            // Si la eliminacion se realizo correctamente redirige a lista de fortalezas.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/Views/Acciones/Fortalezas/ListarFortalezas.xhtml?PAGINA");
        }
    }

}
