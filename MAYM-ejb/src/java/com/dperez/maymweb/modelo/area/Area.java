/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.area;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Areas")
public class Area implements Serializable, Comparable<Area> {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre = new String();
    private String correo = new String();
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Accion> accionesArea;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Fortaleza> fortalezasArea;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Usuario> usuariosArea;
    
    // Constructores
    public Area(){
        this.accionesArea = new ArrayList<>();
        this.fortalezasArea = new ArrayList<>();
        this.usuariosArea = new ArrayList<>();
    }
    public Area(String nombreArea, String correoArea){
        this.nombre = nombreArea;
        this.correo = correoArea;
        this.accionesArea = new ArrayList<>();
        this.fortalezasArea = new ArrayList<>();
        this.usuariosArea = new ArrayList<>();
    }
    
    // Getters
    public int getId() {return id;}
    public String getNombre() {return this.nombre;}
    public String getCorreo() {return this.correo;}
    public List<Accion> getAccionesArea() {return accionesArea;}
    public List<Fortaleza> getFortalezasArea() {return fortalezasArea;}
    public List<Usuario> getUsuariosArea(){return this.usuariosArea;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setCorreo(String correo) {this.correo = correo;}
    
    public void setAccionesArea(List<Accion> accionesArea) {this.accionesArea = accionesArea;}    
    public void setFortalezasArea(List<Fortaleza> fortalezasArea) {this.fortalezasArea = fortalezasArea;}    
    
    public void setUsuariosArea(List<Usuario> usuariosArea){this.usuariosArea = usuariosArea;}
    
    // Listas
    /*
        Acciones
    */
    public void addAccion(Accion accion){
        this.accionesArea.add(accion);
    }
    
    public void removeAccion(Accion accion){
        this.accionesArea.forEach((Accion item)->{
            if(Objects.equals(accion.getId(), item.getId())){
                this.accionesArea.remove(item);
            }
        });
    }
    
    
    /*
        Fortalezas
    */
    
    public void addFortaleza(Fortaleza fortaleza){
        this.fortalezasArea.add(fortaleza);
    }
    
    public void removeFortaleza(Fortaleza fortaleza){
        this.fortalezasArea.forEach((Fortaleza item)->{
            if(Objects.equals(fortaleza.getId(), item.getId())){
                this.fortalezasArea.remove(item);
        }
        });
    }
       
    /*
        Usuario
    */
    public void addUsuario(Usuario usuario){
        this.usuariosArea.add(usuario);
    }
    
    public void removeUsuario(Usuario usuario){
        this.usuariosArea.forEach((Usuario item)->{
            if(Objects.equals(usuario.getId(), item.getId())){
                this.usuariosArea.remove(item);
            }
        });
    }

    @Override
    /**
     * Compara con otro area.
     * La comparacion se realiza por Nombre de Area.
     * Return 0 si son iguales, -1 si es menor, 1 si es mayor.
     */
    public int compareTo(Area otraArea) {
        return this.nombre.compareToIgnoreCase(otraArea.nombre);
    }
    }



