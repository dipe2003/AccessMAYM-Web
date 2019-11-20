/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class Areas implements Serializable {
    @Inject
    private FacadeAdministrador fAdmin;
    @Inject
    private FacadeLectura fLectura;
    
    private Empresa EmpresaLogueada;
    
    private Area AreaSeleccionada;
    
    private String NombreArea;
    private String CorreoArea;
    private int IdAreaSeleccionada;
    
    private boolean AplicaEmpresa;
    private boolean ContieneAcciones;
    
    private List<Area> ListaAreas;
    
    // Pagination
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Area> ListaCompletaAreas;
    
    //  Getters
    
    public String getNombreArea() {return NombreArea;}
    public String getCorreoArea() {return CorreoArea;}
    public List<Area> getListaAreas() {return ListaAreas;}
    
    public Empresa getEmpresaLogueada() {return EmpresaLogueada;}
    public boolean isAplicaEmpresa() {return AplicaEmpresa;}
    public boolean isContieneAcciones() {return ContieneAcciones;}
    
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Setters
    
    public void setNombreArea(String NombreArea) {this.NombreArea = NombreArea;}
    public void setCorreoArea(String CorreoArea) {this.CorreoArea = CorreoArea;}
    public void setListaAreas(List<Area> ListaAreas) {this.ListaAreas = ListaAreas;}
    
    public void setEmpresaLogueada(Empresa EmpresaLogueada) {this.EmpresaLogueada = EmpresaLogueada;}
    public void setAplicaEmpresa(boolean AplicaEmpresa) {this.AplicaEmpresa = AplicaEmpresa;}
    public void setContieneAcciones(boolean ContieneAcciones) {this.ContieneAcciones = ContieneAcciones;}
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        EmpresaLogueada = (Empresa)request.getSession().getAttribute("Empresa");
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        //  Areas
        ListaAreas = new ArrayList<>();
        ListaCompletaAreas = fLectura.ListarAreasSectores();
        
        // Paginas
        CantidadPaginas = Presentacion.calcularCantidadPaginas(ListaCompletaAreas.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
        ListaAreas = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaAreas);
        ListaAreas.stream().sorted();
    }
    
    /**
     * Crea el area con los datos ingresados.
     * Muestra un mensaje de error si no se creo.
     * Se agrega a la lista de areas del bean.
     * @throws java.io.IOException
     */
    public void nuevaArea() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(comprobarNombreArea(NombreArea)){
            context.addMessage("form_areas:btn_nueva_area", new FacesMessage(SEVERITY_ERROR, "Ya existe un area con ese nombre", "Ya existe un area con ese nombre" ));
            context.renderResponse();
        }else{
            Area area;
            if((area = fAdmin.NuevaArea(NombreArea, CorreoArea)) != null){
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Areas.xhtml?pagina=1");
            }else{
                context.addMessage("form_areas:btn_nueva_area", new FacesMessage(SEVERITY_ERROR, "No se pudo crear el area", "No se pudo crear el area" ));
                context.renderResponse();
            }
        }
    }
    
    /**
     * Actualiza el area con lo datos ingresados.
     * Muestra un mensaje de errror si no se actualizo.
     * Agrega los cambios al area de la lista del bean.
     * @throws java.io.IOException
     */
    public void editarArea() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if((fAdmin.EditarArea(IdAreaSeleccionada, NombreArea, CorreoArea))!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Areas.xhtml?pagina="+PaginaActual);
        }else{
            context.addMessage("form_areas:btn_editar_area", new FacesMessage(SEVERITY_ERROR, "No se pudo editar el area", "No se pudo editar el area" ));
            context.renderResponse();
        }
    }
    
    /**
     * Eliminina el area indicada.
     * Muestra un mensaje de error si no se pudo eliminar.
     * Remueve el area de la lista del bean.
     * @param IdAreaSeleccionada
     * @throws java.io.IOException
     */
    public void eliminarArea(int IdAreaSeleccionada) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        // primero se comprueba que pertenezca a la empresa del usuario logueado y que no aplique a otra empresa
        if(fAdmin.EliminarArea(IdAreaSeleccionada)!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Areas.xhtml?pagina=1");
        }else{
            context.addMessage("form_areas:btn_eliminar_"+IdAreaSeleccionada, new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar el Area", "No se pudo eliminar el Area" ));
            context.renderResponse();
        }
    }
    
    /**
     * Comprueba que no exista un Area con el mismo nombre.
     * @param NombreArea
     * @return
     */
    private boolean comprobarNombreArea(String NombreArea){
        return ListaAreas.stream()
                .anyMatch(area->area.getNombre().equalsIgnoreCase(NombreArea));
    }
    
    /**
     * Carga los atributos NombreArea, CorreoArea e IdAreaSeleccionada segun el id especificado en la vista.
     * @param IdArea
     */
    public void cargarDatos(int IdArea){
        if(IdArea < 0 ){
            this.NombreArea = new String();
            this.CorreoArea = new String();
            this.IdAreaSeleccionada = -1;
        }else{
            AreaSeleccionada = ListaAreas.stream()
                    .filter(area->area.getId() == IdArea)
                    .findFirst()
                    .orElse(null);
            
            this.NombreArea = AreaSeleccionada.getNombre();
            this.CorreoArea = AreaSeleccionada.getCorreo();
            this.IdAreaSeleccionada = IdArea;
        }
    }
}