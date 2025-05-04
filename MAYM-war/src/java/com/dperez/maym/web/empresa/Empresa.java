/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.empresa;

import com.dperez.maym.web.configuraciones.OpcionesSistema;
import com.dperez.maym.web.herramientas.ManejadorPropiedades;
import java.util.Properties;

/**
 *
 * @author dipe2
 */
public class Empresa {
    private String nombre;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private String numHabilitacion;
    private String ubicacionLogo;
    private String ubicacionLogoInformes;
    
    private OpcionesSistema opcionesSistema;
    
    public Empresa(){}

    public Empresa(String nombre, String direccion, String telefono, String movil, String correo, String numHabilitacion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.movil = movil;
        this.correo = correo;
        this.numHabilitacion = numHabilitacion;     
        this.opcionesSistema = new OpcionesSistema();
    }
    
       public Empresa(String nombre, String direccion, String telefono, String movil, String correo, String numHabilitacion, String ubicacionLogo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.movil = movil;
        this.correo = correo;
        this.numHabilitacion = numHabilitacion;   
        this.ubicacionLogo = ubicacionLogo;
        this.opcionesSistema = new OpcionesSistema();
    }

    public String getNombre() {
        return nombre;
    }
    
    // Define un nombre para el directorio eliminando espacios y caracteres invalidos
    public String getNombreDeArchivo() {
        return this.nombre.replaceAll("(\\W)", "_");
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumHabilitacion() {
        return numHabilitacion;
    }

    public void setNumHabilitacion(String numHabilitacion) {
        this.numHabilitacion = numHabilitacion;
    }

    public String getUbicacionLogo() {
        return ubicacionLogo;
    }

    public void setUbicacionLogo(String ubicacionLogo) {
        this.ubicacionLogo = ubicacionLogo;
    }

    public String getUbicacionLogoInformes() {
        return ubicacionLogoInformes;
    }

    public void setUbicacionLogoInformes(String ubicacionLogoInformes) {
        this.ubicacionLogoInformes = ubicacionLogoInformes;
    }

    public OpcionesSistema getOpcionesSistema() {
        return opcionesSistema;
    }

    public void setOpcionesSistema(OpcionesSistema opcionesSistema) {
        this.opcionesSistema = opcionesSistema;
    }
    
    
}
