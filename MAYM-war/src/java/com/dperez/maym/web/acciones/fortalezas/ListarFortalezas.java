/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.fortalezas;

import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.fortaleza.Fortaleza;
import java.io.IOException;
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
public class ListarFortalezas implements Serializable{
    @Inject
    private FacadeLectura fLectura;
    
    private List<Fortaleza> ListaFortalezas;
    
    // Pagination
    private static final int MAX_ITEMS = 30;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Fortaleza> ListaCompletaFortalezas;
    
    //  Getters
    
    public List<Fortaleza> getListaFortalezas() {return ListaFortalezas;}
    // Paginacion
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Setters
    
    public void setListaFortalezas(List<Fortaleza> ListaFortalezas) {this.ListaFortalezas = ListaFortalezas;}
    
    // Metodos
    
    //  Inicializacion
    @PostConstruct
    public void init(){
        // recuperar Empresa para filtrar las fortalezas
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // Paginacion
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        
        ListaFortalezas = new ArrayList<>();
        ListaCompletaFortalezas = fLectura.ListarFortalezas();
        
        CantidadPaginas =  Presentacion.calcularCantidadPaginas(ListaCompletaFortalezas.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
       ListaFortalezas = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaFortalezas);
       ListaFortalezas.stream().sorted();
    }
 
    
    /**
     * Redirige a la vista para realizar la edicion de la accion seleccionada.
     * @param IdFortalezaSeleccionada
     * @throws java.io.IOException
     */
    public void abrirEdicion(int IdFortalezaSeleccionada) throws IOException{
        String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Main/Main.xhtml?id="+IdFortalezaSeleccionada);
    }
}
