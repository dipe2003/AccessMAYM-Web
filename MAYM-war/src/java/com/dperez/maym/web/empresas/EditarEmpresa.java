/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.empresas;

import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeDatos;
import com.dperez.maymweb.facades.FacadeLectura;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dipe2
 */
@Stateless
@Named
public class EditarEmpresa implements Serializable{
    @Inject
    private FacadeAdministrador fAdmin;
    @Inject
    private FacadeLectura fLectura;
    @Inject
    private FacadeDatos fDatos;
    
    private Map<Integer, Area> ListaAreas;
    private Map<Integer, Codificacion> ListaCodificaciones;
    
    private Empresa EmpresaSeleccionada;
    
    private String Nombre;
    private String Direccion;
    private String Telefono;
    private String Correo;
    private String Fax;
    private String Descripcion;
    private String Imagen;
    
    //  Getters
    public Map<Integer, Area> getListaAreas() {return ListaAreas;}
    public Map<Integer, Codificacion> getListaCodificaciones() {return ListaCodificaciones;}
    public Empresa getEmpresaSeleccionada() {return EmpresaSeleccionada;}
    public String getNombre() {return Nombre;}
    public String getDireccion() {return Direccion;}
    public String getTelefono() {return Telefono;}
    public String getCorreo() {return Correo;}
    public String getFax() {return Fax;}
    public String getDescripcion() {return Descripcion;}
    public String getImagen() {return Imagen;}
    
    //  Setters
    
    public void setListaAreas(Map<Integer, Area> ListaAreas) {this.ListaAreas = ListaAreas;}
    public void setListaCodificaciones(Map<Integer, Codificacion> ListaCodificaciones) {this.ListaCodificaciones = ListaCodificaciones;}
    public void setEmpresaSeleccionada(Empresa EmpresaSeleccionada) {this.EmpresaSeleccionada = EmpresaSeleccionada;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    public void setDireccion(String Direccion) {this.Direccion = Direccion;}
    public void setTelefono(String Telefono) {this.Telefono = Telefono;}
    public void setCorreo(String Correo) {this.Correo = Correo;}
    public void setFax(String Fax) {this.Fax = Fax;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setImagen(String Imagen) {this.Imagen = Imagen;}
    
    // Metodos
    @PostConstruct
    public void init(){
        ListaAreas = new HashMap<>();
        ListaCodificaciones = new HashMap<>();
        // llenar la lista con todas las areas registadas.
        List<Area> areas = fLectura.ListarAreasSectores();
        ListaAreas = areas.stream()
                .sorted()
                .collect(Collectors.toMap(Area::getId, area->area));
        
        List<Codificacion> codificaciones = fLectura.ListarCodificaciones();
        ListaCodificaciones = codificaciones.stream()
                .sorted()
                .collect(Collectors.toMap(Codificacion::getId, codificacion->codificacion));
    }
  
}
