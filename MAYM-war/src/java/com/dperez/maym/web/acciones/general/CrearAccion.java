/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.acciones.general;

import com.dperez.maym.web.configuraciones.ModalDetecciones;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@ViewScoped
@ManagedBean
public class CrearAccion implements Serializable {
    
    @Inject
    SesionUsuario sesion;

    private FacadeLectura fLectura;
    private FacadeDatos fDatos;

    //<editor-fold desc="Atributos">
    private ModalDetecciones modalDetecciones;

    private TipoAccion tipoDeAccion;

    private Date FechaDeteccion;
    private String strFechaDeteccion;
    private String Descripcion;
    private String Referencias;

    private TipoDeteccion[] tiposDeteccion;
    private TipoDeteccion TipoDeDeteccionSeleccionada;

    private List<Deteccion> ListaDetecciones;
    private Integer DeteccionSeleccionada;

    private List<Area> ListaAreasSectores;
    private Integer AreaSectorAccionSeleccionada;

    private List<Codificacion> ListaCodificaciones;
    private Integer CodificacionSeleccionada;

    //</editor-fold>
    //<editor-fold desc="Getters">    
    public Date getFechaDeteccion() {
        return FechaDeteccion;
    }

    public String getStrFechaDeteccion() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yy");
        if (FechaDeteccion == null) {
            return this.strFechaDeteccion;
        } else {
            return fDate.format(FechaDeteccion);
        }
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getReferencias() {
        return Referencias;
    }

    public TipoAccion getTipoDeAccion() {
        return tipoDeAccion;
    }

    public TipoDeteccion getTipoDeDeteccionSeleccionada() {
        return this.TipoDeDeteccionSeleccionada;
    }

    public TipoDeteccion[] getTiposDeteccion() {
        return this.tiposDeteccion;
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

    public List<Codificacion> getListaCodificaciones() {
        return ListaCodificaciones;
    }

    public Integer getCodificacionSeleccionada() {
        return CodificacionSeleccionada;
    }

    //</editor-fold>
    //<editor-fold desc="Setters>    
    public void setFechaDeteccion(Date FechaDeteccion) {
        this.FechaDeteccion = FechaDeteccion;
    }

    public void setStrFechaDeteccion(String strFechaDeteccion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        try {
            this.FechaDeteccion = sdf.parse(strFechaDeteccion);
        } catch (ParseException ex) {
        }
        this.strFechaDeteccion = strFechaDeteccion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setReferencias(String Referencias) {
        this.Referencias = Referencias;
    }

    public void setTipoDeAccion(TipoAccion tipoDeAccion) {
        this.tipoDeAccion = tipoDeAccion;
    }

    public void setTipoDeDeteccionSeleccionada(TipoDeteccion TipoDeteccion) {
        this.TipoDeDeteccionSeleccionada = TipoDeteccion;
    }

    public void setTiposDeteccion(TipoDeteccion[] tiposDeteccion) {
        this.tiposDeteccion = tiposDeteccion;
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

    public void setListaCodificaciones(List<Codificacion> ListaCodificaciones) {
        this.ListaCodificaciones = ListaCodificaciones;
    }

    public void setCodificacionSeleccionada(Integer CodificacionSeleccionada) {
        this.CodificacionSeleccionada = CodificacionSeleccionada;
    }

    //</editor-fold>
    //<editor-fold desc="Metodos">
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        fDatos = new FacadeDatos();

        FacesContext context = FacesContext.getCurrentInstance();

        // tipo accion
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        // recuperar el id para llenar datos de la accion y el resto de las propiedades.
        tipoDeAccion = TipoAccion.valueOf(request.getParameter("tipo"));

        //  Detecciones
        modalDetecciones = context.getApplication().evaluateExpressionGet(context, "#{modalDetecciones}", ModalDetecciones.class);
        tiposDeteccion = TipoDeteccion.values();
        TipoDeDeteccionSeleccionada = TipoDeteccion.INTERNA;
        ListaDetecciones = modalDetecciones.getListaDetecciones();

        // Areas Sectores        
        ListaAreasSectores = fLectura.listarAreas().stream()
                .sorted()
                .collect(Collectors.toList());

        //  Codificaciones
        ListaCodificaciones = fLectura.listarCodificaciones().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de
     * deteccion.
     */
    public void actualizarDeteccion() {
        modalDetecciones.setTipoDeDeteccionSeleccionada(TipoDeDeteccionSeleccionada);
        modalDetecciones.actualizarDeteccion();
        ListaDetecciones = modalDetecciones.getListaDetecciones();
    }

    /**
     * Crea la accion correctiva con los datos ingresados. Agrega los productos
     * involucrados si existen {hayProductoAfectado = True} Si no se creo se
     * muestra mensaje de error. Si se creo se redirige a la pagina de edicion
     * para agregar mas datos.
     *
     * @throws java.io.IOException
     */
    public void crearAccionCorrectiva() throws IOException {
        if (Referencias == null) {
            Referencias = new String();
        }
        Accion accion = fDatos.nuevaAccion(tipoDeAccion, FechaDeteccion,
                Descripcion, Referencias, AreaSectorAccionSeleccionada, DeteccionSeleccionada, 
                CodificacionSeleccionada, sesion.getUsuarioLogueado().getEmpresaUsuario().getId());

        if (accion != null) {//          
            // redirigir a la edicion de la accion correctiva.
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            url += "/Views/Acciones/General/EditarAccion.xhtml?&id=" + accion.getId();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } else {
            FacesContext.getCurrentInstance().addMessage("form_accion:crear_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo crear la Accion", "No se pudo crear la Accion"));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    //</editor-fold>
}
