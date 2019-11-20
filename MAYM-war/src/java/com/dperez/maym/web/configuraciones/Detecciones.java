/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.deteccion.EnumTipoDeteccion;
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
public class Detecciones implements Serializable {
    @Inject
    private FacadeAdministrador fAdmin;
    @Inject
    private FacadeLectura fLectura;
    
    private Empresa EmpresaLogueada;
    
    private int IdDeteccionSeleccionada;
    private String NombreDeteccion;
    private List<Deteccion> ListaDetecciones;
    
    private boolean ContieneAcciones;
    
    private EnumTipoDeteccion[] TiposDeteccion;
    private EnumTipoDeteccion TipoDeDeteccionSeleccionada;
    
    // Pagination
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Deteccion> ListaCompletaDetecciones;
    
    //  Getters
    public String getNombreDeteccion() {return NombreDeteccion;}
    public List<Deteccion> getListaDetecciones() {return ListaDetecciones;}
    public boolean isContieneAcciones() {return ContieneAcciones;}
    
    public EnumTipoDeteccion getTipoDeDeteccionSeleccionada(){return this.TipoDeDeteccionSeleccionada;}
    public EnumTipoDeteccion[] getTiposDeteccion(){return this.TiposDeteccion;}
    public Empresa getEmpresaLogueada() {return EmpresaLogueada;}
    
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Setters
    public void setNombreDeteccion(String NombreDeteccion) {this.NombreDeteccion = NombreDeteccion;}
    public void setListaDetecciones(List<Deteccion> ListaDetecciones) {this.ListaDetecciones = ListaDetecciones;}
    public void setTipoDeDeteccionSeleccionada(EnumTipoDeteccion TipoDeteccion){this.TipoDeDeteccionSeleccionada = TipoDeteccion;}
    public void setTiposDeteccion(EnumTipoDeteccion[] TiposDeteccion){this.TiposDeteccion = TiposDeteccion;}
    
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
        ListaDetecciones = new ArrayList<>();
        ListaCompletaDetecciones = fLectura.ListarDetecciones();
        
        // Paginas
        CantidadPaginas = Presentacion.calcularCantidadPaginas(ListaCompletaDetecciones.size(), MAX_ITEMS);
        
        TiposDeteccion =  EnumTipoDeteccion.values();
        // llenar la lista con todas las areas registradas.
        ListaDetecciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaDetecciones);
        ListaDetecciones.stream().sorted();
    }
    
   
    
    /**
     * Crea nueva deteccion con el tipo interna/externa seleccionado.
     * Si no se crea se muestra un mensaje, de lo contrario se redirige a la pagina para ver los cambios.
     * @throws java.io.IOException
     */
    public void nuevaDeteccion() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(comprobarNombreDeteccion(NombreDeteccion)){
            context.addMessage("form_detecciones:btn_nueva_deteccion", new FacesMessage(SEVERITY_ERROR, "Ya existe una deteccion con ese nombre", "Ya existe una deteccion con ese nombre" ));
            context.renderResponse();
        }else{
            if((fAdmin.NuevaDeteccion(NombreDeteccion, TipoDeDeteccionSeleccionada)) != null){
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Detecciones.xhtml?pagina=1");
            }else{
                context.addMessage("form_detecciones:btn_nueva_deteccion", new FacesMessage(SEVERITY_ERROR, "No se pudo crear la Deteccion", "No se pudo crear la Deteccion" ));
                context.renderResponse();
            }
        }
    }
    
    
    /**
     * Actualiza la deteccion con lo datos ingresados.
     * Si no se edita se muestra un mensaje, de lo contrario se redirige a la pagina para ver los cambios.
     * @throws java.io.IOException
     */
    public void editarDeteccion() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if((fAdmin.EditarDeteccion(IdDeteccionSeleccionada, NombreDeteccion, TipoDeDeteccionSeleccionada))!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Detecciones.xhtml?pagina="+PaginaActual);
        }else{
            context.addMessage("form_detecciones:btn_editar_deteccion", new FacesMessage(SEVERITY_ERROR, "No se pudo editar la deteccion", "No se pudo editar la deteccion" ));
            context.renderResponse();
        }
    }
    
    /**
     * Eliminina la deteccion indicada.
     * Si no se elimina se muestra un mensaje, de lo contrario se redirige a la pagina para ver los cambios.
     * @param IdDeteccionSeleccionada
     * @throws java.io.IOException
     */
    public void eliminarDeteccion(int IdDeteccionSeleccionada) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(fAdmin.EliminarDeteccion(IdDeteccionSeleccionada)!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Detecciones.xhtml?pagina=1");
        }else{
            context.addMessage("form_detecciones:btn_eliminar_deteccion_"+IdDeteccionSeleccionada, new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar la deteccion", "No se pudo eliminar la deteccion" ));
            context.renderResponse();
        }
    }
    
    /**
     * Carga los atributos NombreDeteccion, TipoDeteccion e IdDeteccionSeleccionada segun el id especificado en la vista.
     * @param IdDeteccion
     */
    public void cargarDatos(int IdDeteccion){
        if(IdDeteccion < 0 ){
            this.NombreDeteccion = new String();
            this.TipoDeDeteccionSeleccionada = EnumTipoDeteccion.INTERNA;
            this.IdDeteccionSeleccionada = -1;
        }else{
            Deteccion deteccionSeleccionada = ListaDetecciones.stream()
                    .filter(deteccion -> deteccion.getId() == IdDeteccion)
                    .findFirst()
                    .orElse(null);
            
            this.NombreDeteccion = deteccionSeleccionada.getNombre();
            this.TipoDeDeteccionSeleccionada = deteccionSeleccionada.getTipo();
            this.IdDeteccionSeleccionada = IdDeteccion;
            
            // verifica que no tenga acciones con deteccion seleccioada
            // al encontrar la primer accion que pertenezca a la deteccion y empresa ContieneAcciones = true
            ContieneAcciones = !deteccionSeleccionada.getAccionesDetectadas().isEmpty();
        }
    }
    
    /**
     * Comprueba que no exista una deteccion con el mismo nombre.
     * Se utiliza el tipo de deteccion seleccionado.
     * @param NombreDeteccion
     * @return
     */
    private boolean comprobarNombreDeteccion(String NombreDeteccion){
        return ListaDetecciones.stream()
                .anyMatch(deteccion->deteccion.getNombre().equalsIgnoreCase(NombreDeteccion));
    }    
}