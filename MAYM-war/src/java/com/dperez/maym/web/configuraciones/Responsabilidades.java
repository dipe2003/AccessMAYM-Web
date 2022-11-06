/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maym.web.acciones.filtros.ContenedorFiltrable;
import com.dperez.maym.web.acciones.filtros.DatosFiltros;
import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
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
public class Responsabilidades implements Serializable {
    
    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    
    private Responsabilidad areaSeleccionada;
    
    private String nombre;
    private int idResponsabilidadSeleccionada;
    
    private List<ContenedorFiltrable<Responsabilidad>> listaResponsabilidades;
    
    private String textoBusqueda;
    
    // Pagination
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<ContenedorFiltrable<Responsabilidad>> listaCompletaResponsabilidades;
    
    //<editor-fold desc="Getters">
    
    public String getNombre() {return nombre;}
    public List<ContenedorFiltrable<Responsabilidad>> getListaResponsabilidades() {return listaResponsabilidades;}
    public String getTextoBusqueda(){return this.textoBusqueda;}
    
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setListaResponsabilidades(List<ContenedorFiltrable<Responsabilidad>> listaResponsabilidades) {this.listaResponsabilidades = listaResponsabilidades;}
    public void setTextoBusqueda(String textoBusqueda){this.textoBusqueda = textoBusqueda;}
    //</editor-fold>
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        
        PaginaActual = 1;
        
        //  Areas
        listaResponsabilidades = new ArrayList<>();
        listaCompletaResponsabilidades = new ArrayList<>();
        fLectura.listarResponsabilidades().stream()
                .forEach((Responsabilidad resp)->{
                    listaCompletaResponsabilidades.add(new ContenedorFiltrable<>(resp.getNombre(), resp));
                });
        
        cambiarPagina(false, PaginaActual);
    }
    
    public void cambiarPagina(boolean conFiltros, int numero){
        if(conFiltros){
            PaginaActual = numero;
            filtrarTexto();
        }else{
            listaCompletaResponsabilidades = listaCompletaResponsabilidades.stream()
                    .sorted((o1, o2) -> {
                        return o1.getContenido().compareTo(o2.getContenido());
                    })
                    .toList();
            
            cargarPagina(listaCompletaResponsabilidades);
            textoBusqueda ="";
        }
    }
    
    private void cargarPagina(List<ContenedorFiltrable<Responsabilidad>> responsabilidades){
        CantidadPaginas = Presentacion.calcularCantidadPaginas(responsabilidades.size(), MAX_ITEMS);
        // Corregir el numero de pagina en caso que se apliquen filtros en una página diferente de 1 y luego de filtrar
        // en esa página no hayan datos para mostrar.
        if(PaginaActual>CantidadPaginas)PaginaActual = 1;
        listaResponsabilidades = Presentacion.cargarPagina(PaginaActual, MAX_ITEMS, responsabilidades);
        listaResponsabilidades = listaResponsabilidades.stream()
                .sorted((o1, o2) -> {
                    return o1.getContenido().compareTo(o2.getContenido());
                })
                .toList();
    }
    
    public void filtrarTexto(){
        List<ContenedorFiltrable<Responsabilidad>> tmpResponsabilidades;
        tmpResponsabilidades = DatosFiltros.FiltrarPorTexto(listaCompletaResponsabilidades, textoBusqueda);
        cargarPagina(tmpResponsabilidades);
    }
    
    public void resetFiltro(){
        textoBusqueda = "";
        cargarPagina(listaCompletaResponsabilidades);
    }
    
    /**
     * Crea la Responsabilidad con los datos ingresados.
     * Muestra un mensaje de error si no se creo.
     * Se agrega a la lista de Responsabilidades del bean.
     * @throws java.io.IOException
     */
    public void nuevaResponsabilidad() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(comprobarNombreResponsabilidad(nombre)){
            context.addMessage("form_responsabilidades:btn_nueva_responsabilidad", new FacesMessage(SEVERITY_ERROR, "Ya existe un Responsabilidad con ese nombre", "Ya existe un Responsabilidad con ese nombre" ));
            context.renderResponse();
        }else{
            if((fAdmin.nuevaResponsabilidad(nombre)) != null){
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Responsabilidades.xhtml?pagina=1");
            }else{
                context.addMessage("form_responsabilidades:btn_nueva_responsabilidad", new FacesMessage(SEVERITY_ERROR, "No se pudo crear la Responsabilidad", "No se pudo crear la Responsabilidad" ));
                context.renderResponse();
            }
        }
    }
    
    /**
     * Actualiza la Responsabilidad con lo datos ingresados.
     * Muestra un mensaje de errror si no se actualizo.
     * Agrega los cambios la responsabilidad de la lista del bean.
     * @throws java.io.IOException
     */
    public void editarResponsabilidad() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if((fAdmin.editarResponsabilidad(idResponsabilidadSeleccionada, nombre))!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Responsabilidades.xhtml?pagina="+PaginaActual);
        }else{
            context.addMessage("form_responsabilidades:btn_editar_responsabilidad", new FacesMessage(SEVERITY_ERROR, "No se pudo editar la responsabilidad", "No se pudo editar la responsabilidad" ));
            context.renderResponse();
        }
    }
    
    /**
     * Eliminina la responsabilidad indicada.Muestra un mensaje de error si no se pudo eliminar.
     * Remueve la responsabilidad de la lista del bean.
     * @param idResponsabilidadSeleccionada
     * @throws java.io.IOException
     */
    public void eliminarResponsabilidad(int idResponsabilidadSeleccionada) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        // primero se comprueba que pertenezca a la empresa del usuario logueado y que no aplique a otra empresa
        if(fAdmin.eliminarResponsabilidad(idResponsabilidadSeleccionada)!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Responsabilidades.xhtml?pagina=1");
        }else{
            context.addMessage("form_responsabilidades:btn_eliminar_"+idResponsabilidadSeleccionada, new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar la Responsabilidad", "No se pudo eliminar la Responsabilidad" ));
            context.renderResponse();
        }
    }
    
    /**
     * Comprueba que no exista una Responsabilidad con el mismo nombre.
     * @param nombreResponsabilidad
     * @return
     */
    private boolean comprobarNombreResponsabilidad(String nombreResponsabilidad){
        return listaResponsabilidades.stream()
                .anyMatch((ContenedorFiltrable<Responsabilidad>r)->r.getContenido().getNombre().equalsIgnoreCase(nombreResponsabilidad));
    }
    
    /**
     * Carga los atributos de Responsabilidad segun el id especificado en la vista.
     * @param idResponsabilidad
     */
    public void cargarDatos(int idResponsabilidad){
        if(idResponsabilidad < 0 ){
            this.nombre = new String();
            this.idResponsabilidadSeleccionada = -1;
        }else{
            areaSeleccionada = listaResponsabilidades.stream()
                    .filter((ContenedorFiltrable<Responsabilidad>r)->r.getContenido().getId() == idResponsabilidad)
                    .findFirst()
                    .orElse(null).getContenido();
            
            this.nombre = areaSeleccionada.getNombre();
            this.idResponsabilidadSeleccionada = idResponsabilidad;
        }
    }
}