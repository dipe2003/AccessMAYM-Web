/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.mejoras;

import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.accion.acciones.Mejora;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Diego
 */
@Named
@ViewScoped
public class ListarMejoras implements Serializable{
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeAdministrador fAdmin;
    
    private List<Mejora> ListaAcciones;
    
    // Pagination
    private static final int MAX_ITEMS = 30;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Mejora> ListaCompletaAcciones;
    
    //  Getters
    public List<Mejora> getListaAcciones() {return ListaAcciones;}
    
    // Paginacion
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Setters
    public void setListaAcciones(List<Mejora> ListaAcciones) {this.ListaAcciones = ListaAcciones;}
    
    // Metodos
    
    //  Inicializacion
    @PostConstruct
    public void init(){
        // recuperar Empresa para filtrar las acciones
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // Paginacion
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        
        // llenar la lista de acciones
        ListaAcciones = new ArrayList<>();
        ListaCompletaAcciones = (List<Mejora>)(List<?>)fLectura.ListarAccionesMejoras();
        
        CantidadPaginas = Presentacion.calcularCantidadPaginas(ListaCompletaAcciones.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
        //cargarPagina(PaginaActual);
        ListaAcciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaAcciones);
        ListaAcciones.stream().sorted();
    }
    
}
