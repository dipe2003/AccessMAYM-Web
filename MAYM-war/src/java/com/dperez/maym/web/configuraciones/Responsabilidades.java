/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

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
import javax.servlet.http.HttpServletRequest;

@Named
@ViewScoped
public class Responsabilidades implements Serializable {

    private FacadeAdministrador fAdmin;   
    private FacadeLectura fLectura;
    
    private Responsabilidad areaSeleccionada;
    
    private String nombre;
    private int idResponsabilidadSeleccionada;
    
    private List<Responsabilidad> listaResponsabilidades;
    
    // Pagination
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Responsabilidad> listaCompletaResponsabilidades;
    
    //  Getters
    
    public String getNombre() {return nombre;}
    public List<Responsabilidad> getListaResponsabilidades() {return listaResponsabilidades;}
    
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Setters
    
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setListaResponsabilidades(List<Responsabilidad> listaResponsabilidades) {this.listaResponsabilidades = listaResponsabilidades;}

    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        //  Areas
        listaResponsabilidades = new ArrayList<>();
        listaCompletaResponsabilidades = fLectura.listarResponsabilidades();
        
        // Paginas
        CantidadPaginas = Presentacion.calcularCantidadPaginas(listaCompletaResponsabilidades.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
        listaResponsabilidades = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, listaCompletaResponsabilidades);
        listaResponsabilidades.stream().sorted();
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
                .anyMatch(r->r.getNombre().equalsIgnoreCase(nombreResponsabilidad));
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
                    .filter(r->r.getId() == idResponsabilidad)
                    .findFirst()
                    .orElse(null);
            
            this.nombre = areaSeleccionada.getNombre();
            this.idResponsabilidadSeleccionada = idResponsabilidad;
        }
    }
}