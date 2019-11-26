/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.omap;

import com.dperez.maym.web.configuraciones.ModalDetecciones;
import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.deteccion.EnumTipoDeteccion;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;



@Named
@ViewScoped
public class CrearOMAP implements Serializable {
    @Inject
    private FacadeAdministrador fAdmin;
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    
    private ModalDetecciones modalDetecciones;
    
    private Date FechaDeteccion;
    private String strFechaDeteccion;
    private String Descripcion;
    private TipoAccion TipoAccion;
    
    private EnumTipoDeteccion[] TiposDeteccion;
    private EnumTipoDeteccion TipoDeDeteccionSeleccionada;
    private Map<Integer, String> ListaDetecciones;
    private Integer DeteccionSeleccionada;
    
    private Map<Integer, Area> ListaAreasSectores;
    private Integer AreaSectorAccionSeleccionada;
    
    //  Getters
    public Date getFechaDeteccion() {return FechaDeteccion;}
    public String getStrFechaDeteccion(){
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (FechaDeteccion == null) {
            return this.strFechaDeteccion;
        }else{
            return fDate.format(FechaDeteccion);
        }
    }
    public String getDescripcion() {return Descripcion;}
    public TipoAccion getTipoAccion() {return TipoAccion;}    
    
    public EnumTipoDeteccion getTipoDeDeteccionSeleccionada(){return this.TipoDeDeteccionSeleccionada;}
    public EnumTipoDeteccion[] getTiposDeteccion(){return this.TiposDeteccion;}
    public Map<Integer, String> getListaDetecciones(){return this.ListaDetecciones;}
    public Integer getDeteccionSeleccionada(){return this.DeteccionSeleccionada;}
    
    public Map<Integer, Area> getListaAreasSectores(){return this.ListaAreasSectores;}
    public Integer getAreaSectorAccionSeleccionada() {return AreaSectorAccionSeleccionada;}
    
//  Setters
    public void setFechaDeteccion(Date FechaDeteccion) {this.FechaDeteccion = FechaDeteccion;}
    public void setStrFechaDeteccion(String strFechaDeteccion) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try{
            cal.setTime(sdf.parse(strFechaDeteccion));
        }catch(ParseException ex){}
        this.strFechaDeteccion = strFechaDeteccion;
        this.FechaDeteccion = cal.getTime();
    }
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setTipoAccion(TipoAccion TipoAccion) {this.TipoAccion = TipoAccion;}
    
    public void setTipoDeDeteccionSeleccionada(EnumTipoDeteccion TipoDeteccion){this.TipoDeDeteccionSeleccionada = TipoDeteccion;}
    public void setTiposDeteccion(EnumTipoDeteccion[] TiposDeteccion){this.TiposDeteccion = TiposDeteccion;}
    public void setListaDetecciones(Map<Integer, String> ListaDetecciones){this.ListaDetecciones = ListaDetecciones;}
    public void setDeteccionSeleccionada(Integer DeteccionSeleccionada){this.DeteccionSeleccionada = DeteccionSeleccionada;}
    
    public void setListaAreaSectores(Map<Integer, Area> ListaAreasSectores){this.ListaAreasSectores = ListaAreasSectores;}
    public void setAreaSectorAccionSeleccionada(Integer AreaSectorAccionSeleccionada) {this.AreaSectorAccionSeleccionada = AreaSectorAccionSeleccionada;}
    
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        // Empresa
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        TipoAccion = TipoAccion.valueOf(request.getParameter("tipo"));
        
        //  Detecciones
        modalDetecciones = context.getApplication().evaluateExpressionGet(context, "#{modalDetecciones}", ModalDetecciones.class);
        TiposDeteccion = EnumTipoDeteccion.values();
        TipoDeDeteccionSeleccionada = EnumTipoDeteccion.INTERNA;
        ListaDetecciones = new TreeMap<>(modalDetecciones.getListaDetecciones());
        
        // Areas Sectores
        ListaAreasSectores = new HashMap<>();
        List<Area> tmpAreas = fLectura.ListarAreasSectores();
        ListaAreasSectores = tmpAreas.stream()
                .sorted()
                .collect(Collectors.toMap(Area::getId, area->area));
        ListaAreasSectores = new TreeMap<>(ListaAreasSectores);
    }
    
    /**
     * Actualiza la lista de detecciones segun la seleccion de tipo de deteccion.
     */
    public void actualizarDeteccion(){
        ListaDetecciones = new TreeMap<>(modalDetecciones.getListaDetecciones());
    }    
    
    /**
     * Crea la accion correctiva con los datos ingresados.
     * Si no se creo se muestra mensaje de error.
     * Si se creo se redirige a la pagina de edicion para agregar mas datos.
     * @throws java.io.IOException
     */
    public void crearAccion() throws IOException{
        Accion accion = fDatos.NuevaAccion(TipoAccion, FechaDeteccion,
                Descripcion, null, AreaSectorAccionSeleccionada, DeteccionSeleccionada);
        
        if(accion != null){
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Acciones/MejorasYPreventivas/ListarOMAP.xhtml?tipo="+TipoAccion);
        }else{
            FacesContext.getCurrentInstance().addMessage("form_accion:crear_accion", new FacesMessage(SEVERITY_ERROR, "No se pudo crear la Accion", "No se pudo crear la Accion" ));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }    
}
