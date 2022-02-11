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
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.modelo.usuario.Usuario;
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
public class Responsables implements Serializable {

    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    
    //<editor-fold desc="Atributos">
    private Responsable responsableSeleccionado;
    private String nombre;
    private List<Responsable> listaResponsables;
    private List<Usuario> listaUsuarios;
    private int idUsuarioSeleccionado;
    private List<Responsabilidad> responsabilidades;
    private int idResponsabilidadSeleccionada;
    
    //<editor-fold desc="Pagination">
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Responsable> listaCompletaResponsables;
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="Getters">
    
    public String getNombre() {return nombre;}
    public List<Responsable> getListaResponsables() {return listaResponsables;}
    public List<Usuario> getListaUsuarios(){return this.listaUsuarios;}
    public List<Responsabilidad> getResponsabilidades(){return this.responsabilidades;}
    public int getIdResponsabilidadSeleccionada(){return this.idResponsabilidadSeleccionada;}
    public int getIdUsuarioSeleccionado(){return this.idUsuarioSeleccionado;}
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setListaResponsables(List<Responsable> lista) {this.listaResponsables = lista;}
    public void setListaUsuarios(List<Usuario> lista){this.listaUsuarios = lista;}
    public void setResponsabilidades(List<Responsabilidad> lista){this.responsabilidades = lista;}
    public void setIdResponsabilidadSeleccionada(int id){this.idResponsabilidadSeleccionada = id;}
    public void setIdUsuarioSeleccionado(int usuario){this.idUsuarioSeleccionado = usuario;}
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    
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
        // Responsabilidades
        responsabilidades = fLectura.listarResponsabilidades();
        
        //  Responsables
        listaResponsables = new ArrayList<>();
        listaCompletaResponsables = fLectura.listarResponsables(false);
        listaUsuarios = fLectura.listarUsuarios(true);
                
        // Paginas
        CantidadPaginas = Presentacion.calcularCantidadPaginas(listaCompletaResponsables.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
        listaResponsables = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, listaCompletaResponsables);
        listaResponsables.stream().sorted();
    }
    
    /**
     * Crea el area con los datos ingresados.
     * Muestra un mensaje de error si no se creo.
     * Se agrega a la lista de areas del bean.
     * @throws java.io.IOException
     */
    public void nuevoResponsable() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if((fAdmin.nuevoResponsable(idResponsabilidadSeleccionada, idUsuarioSeleccionado)) != null){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Responsables.xhtml?pagina=1");
        }else{
            context.addMessage("form_responsables:btn_nuevo", new FacesMessage(SEVERITY_ERROR, "No se pudo crear", "No se pudo crear" ));
            context.renderResponse();
        }
    }
    
    /**
     * Actualiza el area con lo datos ingresados.
     * Muestra un mensaje de errror si no se actualizo.
     * Agrega los cambios al area de la lista del bean.
     * @throws java.io.IOException
     */
    public void editarResponsable() throws IOException{
        // TODO: decidir que es lo que va a hacer el metodo.
//        FacesContext context = FacesContext.getCurrentInstance();
//        if((fAdmin.editarResponsable(responsableSeleccionado.getId(), nombre))!=-1){
//            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Responsables.xhtml?pagina="+PaginaActual);
//        }else{
//            context.addMessage("form_responsables:btn_editar_responsable", new FacesMessage(SEVERITY_ERROR, "No se pudo editar", "No se pudo editar" ));
//            context.renderResponse();
//        }
    }
    
    /**
     * Eliminina el area indicada.
     * Muestra un mensaje de error si no se pudo eliminar.
     * Remueve el area de la lista del bean.
     * @param idResponsableSeleccionado
     * @throws java.io.IOException
     */
    public void eliminarResponsable(int idResponsableSeleccionado) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(fAdmin.eliminarResponsable(idResponsableSeleccionado)!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Responsables.xhtml?pagina=1");
        }else{
            context.addMessage("form_areas:btn_eliminar_"+idResponsableSeleccionado, new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar", "No se pudo eliminar" ));
            context.renderResponse();
        }
    }
    
    
    /**
     * Carga los atributos NombreArea, CorreoArea e IdAreaSeleccionada segun el id especificado en la vista.
     * @param idResponsable
     */
    public void cargarDatos(int idResponsable){
        if(idResponsable < 0 ){
            this.nombre = new String();
        }else{
            responsableSeleccionado = listaResponsables.stream()
                    .filter(r->r.getId() == idResponsable)
                    .findFirst()
                    .orElse(null);
            
            this.nombre = responsableSeleccionado.getResponsabilidadResponsable().getNombre();
            this.idUsuarioSeleccionado =responsableSeleccionado.getUsuarioResponsable().getId();
        }
    }
    //</editor-fold>
}