/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.acciones.correctivas;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.accion.acciones.Correctiva;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.producto.Producto;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@ViewScoped
@ManagedBean
public class ProductoInvolucrado implements Serializable {
    
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    
    private int IdAccionSeleccionada;
    private Accion AccionSeleccionada;
    
    private Map<String, String> ListaProductosAfectados;
    private String NombreProductoAfectado;
    private String DatosProductoAfectado;
    private String NombreProductoAfectadoOriginal;
    
    public ProductoInvolucrado(){}
    public ProductoInvolucrado(int idAccion){
        IdAccionSeleccionada = idAccion;
    }
    
    //  Getters
    public Accion getAccionSeleccionada(){return AccionSeleccionada;}
    public int getIdAccionSeleccionada() {return IdAccionSeleccionada;    }
    
    public Map<String, String> getListaProductosAfectados(){return this.ListaProductosAfectados;}
    public String getNombreProductoAfectado(){return this.NombreProductoAfectado;}
    public String getDatosProductoAfectado(){return this.DatosProductoAfectado;}
    
    //comprobaciones
    public void setIdAccionSeleccionada(int IdAccionSeleccionada){this.IdAccionSeleccionada = IdAccionSeleccionada;}
    public void setListaProductosAfectados(Map<String, String> ListaProductosAfectados) {this.ListaProductosAfectados = ListaProductosAfectados;}
    public void setNombreProductoAfectado(String NombreProductoAfectado){this.NombreProductoAfectado = NombreProductoAfectado;}
    public void setDatosProductoAfectado(String DatosProductoAfectado){this.DatosProductoAfectado = DatosProductoAfectado;}
    
    //  Metodos
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        // recuperar el id para llenar datos de los producto involucrados (si los hay).
        ListaProductosAfectados = new HashMap<>();
        IdAccionSeleccionada = 0;
        try{
            IdAccionSeleccionada = Integer.parseInt(request.getParameter("id"));
        }catch(NumberFormatException nEx){}
        
        if(IdAccionSeleccionada != 0){
            AccionSeleccionada = (Correctiva) fLectura.GetAccion(IdAccionSeleccionada);
            
            if(!((Correctiva)AccionSeleccionada).getProductosAfectados().isEmpty()){
                List<Producto> ProductosAfectados = ((Correctiva)AccionSeleccionada).getProductosAfectados();
                ListaProductosAfectados = ProductosAfectados.stream()
                        .collect(Collectors.toMap(Producto::getNombre, producto->producto.getNombre()));
            }
        }
    }
    
    public void nuevoProductoAfectadoGenerico(){
        if(IdAccionSeleccionada==0){
            nuevoProductoAfectado();
        }else{
            nuevoProductoAfectado(IdAccionSeleccionada);
        }
    }
    /**
     * Agrega un nuevo producto afectado a la lista de productos afectados para ser persistidos durante la creacion de la accion correctiva.
     * Si el nombre del producto ya existe se sustituye
     * Si el nombre o el datos estan vacios no se crea y se muestra el mensaje correspondiente.
     */
    private void nuevoProductoAfectado(){
        if(NombreProductoAfectado!= null && DatosProductoAfectado !=null){
            if(NombreProductoAfectado.isEmpty() || DatosProductoAfectado.isEmpty()){
                FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nuevo_producto", new FacesMessage(SEVERITY_FATAL, "No se pudo agregar producto", "No se pudo agregar producto" ));
                FacesContext.getCurrentInstance().renderResponse();
            }else{
                this.ListaProductosAfectados.put(NombreProductoAfectado, DatosProductoAfectado);
                this.NombreProductoAfectado = new String();
                this.DatosProductoAfectado = new String();
                FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nuevo_producto", new FacesMessage(SEVERITY_INFO, "El producto fue agregado.", "El producto fue agregado."));
                FacesContext.getCurrentInstance().renderResponse();
            }
        }
    }
    
    /**
     * Agrega un nuevo producto afectado a la lista de productos afectados para ser persistidos durante la creacion de la accion correctiva.
     * Si el nombre del producto ya existe se sustituye
     * Si el nombre o el datos estan vacios no se crea y se muestra el mensaje correspondiente.
     * PRE: debe existir una accion correctiva ya creada
     * @param idAccionSeleccionada
     */
    private void nuevoProductoAfectado(int idAccionSeleccionada){
        if(NombreProductoAfectado!= null && DatosProductoAfectado !=null){
            if(NombreProductoAfectado.isEmpty() || DatosProductoAfectado.isEmpty()){
                FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nuevo_producto", new FacesMessage(SEVERITY_FATAL, "No se pudo agregar producto", "No se pudo agregar producto" ));
                FacesContext.getCurrentInstance().renderResponse();
            }else{
                this.ListaProductosAfectados.put(NombreProductoAfectado, DatosProductoAfectado);
                fDatos.AgregarProductoInvolucrado(idAccionSeleccionada, NombreProductoAfectado, DatosProductoAfectado);
                this.NombreProductoAfectado = new String();
                this.DatosProductoAfectado = new String();
                FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_nuevo_producto", new FacesMessage(SEVERITY_INFO, "El producto fue agregado.", "El producto fue agregado."));
                FacesContext.getCurrentInstance().renderResponse();
            }
        }
    }
    
    public void quitarProductoGenerico(String NombreProducto){
       if(IdAccionSeleccionada==0){
           quitarProducto(NombreProducto);
       } else{
           quitarProducto(NombreProducto, IdAccionSeleccionada);
       }
    }
    
    /**
     * Quita el producto seleccionado de la lista de productos afectados.
     * @param NombreProducto
     */
    private void quitarProducto(String NombreProducto){
        this.ListaProductosAfectados.remove(NombreProducto);
    }
    
    /**
     * Remueve el producto involucrado de la base de datos.
     * Se quita el producto de la lista de productos del bean.
     * Se muestra un mensaje de error si no se elimino.
     * PRE: debe existir una accion correctiva ya creada
     * @param NombreProducto
     * @param idAccionSeleccionada
     */
    private void quitarProducto(String NombreProducto, int idAccionSeleccionada){
        if(fDatos.RemoverProductoInvolucrado(idAccionSeleccionada, NombreProducto)==-1){
            FacesContext.getCurrentInstance().addMessage("form_accion:btn_eliminar_accion", new FacesMessage(SEVERITY_FATAL, "No se pudo quitar producto", "No se pudo quitar producto" ));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            this.ListaProductosAfectados.remove(NombreProducto);
            this.NombreProductoAfectado = new String();
            this.DatosProductoAfectado = new String();
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    
    /**
     * Carga los datos del producto en los campos del formulario.
     * Lo remueve de la lista.
     * @param NombreProducto
     */
    public void cargarDatosProducto(String NombreProducto){
        this.NombreProductoAfectado = NombreProducto;
        this.DatosProductoAfectado = this.ListaProductosAfectados.get(NombreProducto);
        this.NombreProductoAfectadoOriginal = this.NombreProductoAfectado;
    }
    
    public void guardarCambiosProductoGenerico(){
        if(IdAccionSeleccionada==0){
            guardarCambiosProducto();
        }else{
            guardarCambiosProducto(IdAccionSeleccionada);
        }
    }
    /**
     * Guarda los datos del producto de los campos del formulario.
     * Remueve de la lista los datos del producto anterior.
     */
    private void guardarCambiosProducto(){
        if(!NombreProductoAfectado.equals(NombreProductoAfectadoOriginal) || !DatosProductoAfectado.equals(this.ListaProductosAfectados.get(NombreProductoAfectadoOriginal))){
            this.ListaProductosAfectados.remove(NombreProductoAfectadoOriginal);
            this.ListaProductosAfectados.put(NombreProductoAfectado, DatosProductoAfectado);
            this.NombreProductoAfectado = new String();
            this.DatosProductoAfectado = new String();
            FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_editar_producto", new FacesMessage(SEVERITY_INFO, "El producto fue guardado.", "El producto fue guardado."));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    
    /**
     * Guarda los datos del producto de los campos del formulario.
     * Remueve de la lista los datos del producto anterior.
     * PRE: debe existir una accion correctiva ya creada
     * @param idAccionSeleccionada
     */
    private void guardarCambiosProducto(int idAccionSeleccionada){
        if(!NombreProductoAfectado.equals(NombreProductoAfectadoOriginal) || !DatosProductoAfectado.equals(this.ListaProductosAfectados.get(NombreProductoAfectadoOriginal))){
            this.ListaProductosAfectados.remove(NombreProductoAfectadoOriginal);
            this.ListaProductosAfectados.put(NombreProductoAfectado, DatosProductoAfectado);
            fDatos.AgregarProductoInvolucrado(idAccionSeleccionada, NombreProductoAfectado, DatosProductoAfectado);
            fDatos.RemoverProductoInvolucrado(idAccionSeleccionada, NombreProductoAfectadoOriginal);
            this.NombreProductoAfectado = new String();
            this.DatosProductoAfectado = new String();
            FacesContext.getCurrentInstance().addMessage("form_accion_modal:btn_editar_producto", new FacesMessage(SEVERITY_INFO, "El producto fue guardado.", "El producto fue guardado."));
            FacesContext.getCurrentInstance().renderResponse();
        }else{
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    
    public void limpiarModalProducto(){
        this.NombreProductoAfectado = new String();
        this.DatosProductoAfectado = new String();
    }
}
