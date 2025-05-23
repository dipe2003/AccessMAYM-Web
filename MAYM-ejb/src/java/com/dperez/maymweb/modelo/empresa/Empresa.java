/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maymweb.modelo.empresa;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author dipe2
 */
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private String nombreExtra;
    private String ubicacionLogo;
    private String ubicacionLogoAdicional;

    private OpcionesSistema opcionesSistema;

    @OneToMany(mappedBy = "empresaUsuario", cascade = CascadeType.ALL)
    private List<Usuario> usuariosEmpresa = new ArrayList();

    @OneToMany(mappedBy = "empresaAccion", cascade = CascadeType.ALL)
    private List<Accion> accionesEmpresa = new ArrayList();

    @OneToMany(mappedBy = "empresaFortaleza", cascade = CascadeType.ALL)
    private List<Fortaleza> fortalezasEmpresa = new ArrayList();

    public Empresa() {
    }

    public Empresa(String nombre, String direccion, String telefono, String movil, String correo, String nombreExtra) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.movil = movil;
        this.correo = correo;
        this.nombreExtra = nombreExtra;
        this.opcionesSistema = new OpcionesSistema();
    }

    //<editor-fold desc="GETTERS">
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getMovil() {
        return movil;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNombreExtra() {
        return nombreExtra;
    }

    public String getUbicacionLogo() {
        return ubicacionLogo;
    }

    public String getUbicacionLogoAdicional() {
        return ubicacionLogoAdicional;
    }

    public OpcionesSistema getOpcionesSistema() {
        return opcionesSistema;
    }

    public List<Usuario> getUsuariosEmpresa() {
        return usuariosEmpresa;
    }

    public List<Accion> getAccionesEmpresa() {
        return accionesEmpresa;
    }

    public List<Fortaleza> getFortalezasEmpresa() {
        return fortalezasEmpresa;
    }

    //</editor-fold>
    //<editor-fold desc="SETTERS">
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombreExtra(String nombreExtra) {
        this.nombreExtra = nombreExtra;
    }

    public void setUbicacionLogo(String ubicacionLogo) {
        this.ubicacionLogo = ubicacionLogo;
    }

    public void setUbicacionLogoAdicional(String ubicacionLogoAdicional) {
        this.ubicacionLogoAdicional = ubicacionLogoAdicional;
    }

    public void setOpcionesSistema(OpcionesSistema opcionesSistema) {
        this.opcionesSistema = opcionesSistema;
    }

    public void setUsuariosEmpresa(List<Usuario> usuariosEmpresa) {
        this.usuariosEmpresa = usuariosEmpresa;
    }

    public void setAccionesEmpresa(List<Accion> accionesEmpresa) {
        this.accionesEmpresa = accionesEmpresa;
    }

    public void setFortalezasEmpresa(List<Fortaleza> fortalezasEmpresa) {
        this.fortalezasEmpresa = fortalezasEmpresa;
    }

    //</editor-fold>
    //<editor-fold desc="METODOS">
    /**
     * Define un nombre para el directorio eliminando espacios y caracteres
     * invalidos
     *
     * @return
     */
    public String getNombreDeArchivo() {
        return this.nombre.replaceAll("(\\W)", "_");
    }

    //</editor-fold>    
}
