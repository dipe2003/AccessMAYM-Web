/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.omap;

import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
public class ListarOMAP implements Serializable{
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeAdministrador fAdmin;
    
    private List<Accion> ListaAcciones;
    private TipoAccion TipoAccion;
    
    // Pagination
    private static final int MAX_ITEMS = 30;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<Accion> ListaCompletaAcciones;
    
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //  Getters
    public List<Accion> getListaAcciones() {return ListaAcciones;}
    public TipoAccion getTipoAccion() {return TipoAccion;}
    
    //  Setters
    public void setListaAcciones(List<Accion> ListaAcciones) {this.ListaAcciones = ListaAcciones;}
    public void setTipoAccion(TipoAccion TipoAccion) {this.TipoAccion = TipoAccion;}    
    
    // Metodos
    
    //  Inicializacion
    @PostConstruct
    public void init(){
        // recuperar Empresa para filtrar las acciones
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        TipoAccion = TipoAccion.valueOf(request.getParameter("tipo"));
        int id = 0;
        try{
            id = Integer.parseInt(request.getParameter("buscarid"));
        }catch(NumberFormatException ex){}
                
        // Paginacion
        PaginaActual = 1;
        try{
            PaginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(NumberFormatException ex){
            System.out.println("Error en pagina actual: " + ex.getLocalizedMessage());
        }
        
        // llenar la lista de acciones
        ListaAcciones = new ArrayList<>();
        if(TipoAccion == TipoAccion.MEJORA){
            ListaCompletaAcciones = (List<Accion>)(List<?>)fLectura.ListarAccionesMejoras();            
        }else{
            ListaCompletaAcciones = (List<Accion>)(List<?>)fLectura.ListarAccionesPreventivas();
        }
        // llenar la lista de acciones
     
        if (id > 0){
            ListaAcciones = filtrarPorId(ListaCompletaAcciones, id);
        }
        CantidadPaginas = Presentacion.calcularCantidadPaginas(ListaCompletaAcciones.size(), MAX_ITEMS);
        
        // llenar la lista con todas las areas registradas.
        //cargarPagina(PaginaActual);
        ListaAcciones = new Presentacion().cargarPagina(PaginaActual, MAX_ITEMS, ListaCompletaAcciones);
        ListaAcciones.stream().sorted();
    }
    
    private List<Accion> filtrarPorId(List<Accion> acciones, int id){
        return acciones.stream()
                .filter(a->a.getId() == id)
                .collect(Collectors.toList());
    }
    
}
