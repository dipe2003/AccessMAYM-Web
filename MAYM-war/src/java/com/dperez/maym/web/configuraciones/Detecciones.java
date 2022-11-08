/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maym.web.acciones.filtros.ContenedorFiltrable;
import com.dperez.maym.web.acciones.filtros.DatosFiltros;
import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
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
import javax.inject.Named;



@Named
@ViewScoped
public class Detecciones implements Serializable {
    
    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    
    private int IdDeteccionSeleccionada;
    private String NombreDeteccion;
    private List<ContenedorFiltrable<Deteccion>> ListaDetecciones;
    
    private boolean ContieneAcciones;
    
    private TipoDeteccion[] TiposDeteccion;
    private TipoDeteccion TipoDeDeteccionSeleccionada;
    
    private String textoBusqueda;
    
    // Pagination
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<ContenedorFiltrable<Deteccion>> ListaCompletaDetecciones;
    
    //<editor-fold desc="Getters">
    public String getNombreDeteccion() {return NombreDeteccion;}
    public List<ContenedorFiltrable<Deteccion>> getListaDetecciones() {return ListaDetecciones;}
    public boolean isContieneAcciones() {return ContieneAcciones;}
    
    public TipoDeteccion getTipoDeDeteccionSeleccionada(){return this.TipoDeDeteccionSeleccionada;}
    public TipoDeteccion[] getTiposDeteccion(){return this.TiposDeteccion;}
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    public String getTextoBusqueda(){return this.textoBusqueda;}
    //</editor-fold>
    
    //<editor-fold desc="Setter">
    public void setNombreDeteccion(String NombreDeteccion) {this.NombreDeteccion = NombreDeteccion;}
    public void setListaDetecciones(List<ContenedorFiltrable<Deteccion>> ListaDetecciones) {this.ListaDetecciones = ListaDetecciones;}
    public void setTipoDeDeteccionSeleccionada(TipoDeteccion TipoDeteccion){this.TipoDeDeteccionSeleccionada = TipoDeteccion;}
    public void setTiposDeteccion(TipoDeteccion[] TiposDeteccion){this.TiposDeteccion = TiposDeteccion;}
    public void setTextoBusqueda(String textoBusqueda){this.textoBusqueda = textoBusqueda;}
    //</editor-fold>
    
//<editor-fold desc="Metodo">
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        
        PaginaActual = 1;        
        
        ListaDetecciones = new ArrayList<>();
        ListaCompletaDetecciones = new ArrayList<>();
                fLectura.listarDetecciones().stream()
                .forEach((Deteccion det)->{
                    ListaCompletaDetecciones.add(new ContenedorFiltrable<>(det.getNombre(), det));
                });
        
        cambiarPagina(false, PaginaActual);
    }
    
    //<editor-fold desc="Filtros">
    public void cambiarPagina(boolean conFiltros, int numero){
        if(conFiltros){
            PaginaActual = numero;
            filtrarTexto();
        }else{
            ListaCompletaDetecciones = ListaCompletaDetecciones.stream()
                    .sorted((o1, o2) -> {
                        return o1.getContenido().compareTo(o2.getContenido());
                    })
                    .toList();
            
            cargarPagina(ListaCompletaDetecciones);
            textoBusqueda ="";
        }
    }
    
    private void cargarPagina(List<ContenedorFiltrable<Deteccion>> detecciones){
        CantidadPaginas = Presentacion.calcularCantidadPaginas(detecciones.size(), MAX_ITEMS);
        // Corregir el numero de pagina en caso que se apliquen filtros en una página diferente de 1 y luego de filtrar
        // en esa página no hayan datos para mostrar.
        if(PaginaActual>CantidadPaginas)PaginaActual = 1;
        ListaDetecciones = Presentacion.cargarPagina(PaginaActual, MAX_ITEMS, detecciones);
        ListaDetecciones = ListaDetecciones.stream()
                .sorted((o1, o2) -> {
                    return o1.getContenido().compareTo(o2.getContenido());
                })
                .toList();
    }
    
    public void filtrarTexto(){
        cargarPagina(DatosFiltros.FiltrarPorTexto(ListaCompletaDetecciones, textoBusqueda));
    }
    
    public void resetFiltro(){
        textoBusqueda = "";
        cargarPagina(ListaCompletaDetecciones);
    }
    
    //</editor-fold>
    
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
            if((fAdmin.nuevaDeteccion(NombreDeteccion, TipoDeDeteccionSeleccionada)) != null){
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
        if((fAdmin.editarDeteccion(IdDeteccionSeleccionada, NombreDeteccion, TipoDeDeteccionSeleccionada))!=-1){
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
        if(fAdmin.eliminarDeteccion(IdDeteccionSeleccionada)!=-1){
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
            this.TipoDeDeteccionSeleccionada = TipoDeteccion.INTERNA;
            this.IdDeteccionSeleccionada = -1;
        }else{
            Deteccion deteccionSeleccionada = ListaDetecciones.stream()
                    .filter((ContenedorFiltrable<Deteccion> det) -> det.getContenido().getId() == IdDeteccion)
                    .findFirst()
                    .orElse(null).getContenido();
            
            this.NombreDeteccion = deteccionSeleccionada.getNombre();
            this.TipoDeDeteccionSeleccionada = deteccionSeleccionada.getTipoDeDeteccion();
            this.IdDeteccionSeleccionada = IdDeteccion;
            
            // verifica que no tenga acciones con deteccion seleccioada
            // al encontrar la primer accion que pertenezca a la deteccion y empresa ContieneAcciones = true
            ContieneAcciones = !deteccionSeleccionada.getAccionesDeteccion().isEmpty();
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
                .anyMatch((ContenedorFiltrable<Deteccion> det)->det.getContenido().getNombre().equalsIgnoreCase(NombreDeteccion));
    }
//</editor-fold>
}