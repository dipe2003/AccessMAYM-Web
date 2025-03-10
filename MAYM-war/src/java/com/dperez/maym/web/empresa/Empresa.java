/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.empresa;

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
    
    public Empresa(){}

    public Empresa(String nombre, String direccion, String telefono, String movil, String correo, String numHabilitacion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.movil = movil;
        this.correo = correo;
        this.numHabilitacion = numHabilitacion;
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
    
    
}
