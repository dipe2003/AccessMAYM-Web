/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.area;

import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.fortaleza.Fortaleza;
import com.dperez.maymweb.usuario.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Areas")
public class Area implements Serializable, Comparable<Area> {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String Nombre = new String();
    private String Correo = new String();
    
    @OneToMany(mappedBy = "AreaSectorAccion")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Accion> AccionesEnAreaSector;
    
    @OneToMany(mappedBy = "AreaSectorFortaleza")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Fortaleza> FortalezasEnAreaSector;

    @OneToMany(mappedBy = "AreaSectorUsuario")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Usuario> UsuariosEnAreaSector;
    
    // Constructores
    public Area(){
        this.AccionesEnAreaSector = new ArrayList<>();
        this.FortalezasEnAreaSector = new ArrayList<>();
        this.UsuariosEnAreaSector = new ArrayList<>();
    }
    public Area(String NombreArea, String CorreoArea){
        this.Nombre = NombreArea;
        this.Correo = CorreoArea;
        this.AccionesEnAreaSector = new ArrayList<>();
        this.FortalezasEnAreaSector = new ArrayList<>();
        this.UsuariosEnAreaSector = new ArrayList<>();
    }
    
    // Getters
    public int getId() {return Id;}
    public String getNombre() {return this.Nombre;}
    public String getCorreo() {return this.Correo;}
    public List<Accion> getAccionesEnAreaSector() {return AccionesEnAreaSector;}
    public List<Fortaleza> getFortalezasEnAreaSector() {return FortalezasEnAreaSector;}
    public List<Usuario> getUsuariosEnAreaSector(){return this.UsuariosEnAreaSector;}
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    public void setCorreo(String Correo) {this.Correo = Correo;}
    
    public void setAccionesEnAreaSector(List<Accion> AccionesEnAreaSector) {
        this.AccionesEnAreaSector = AccionesEnAreaSector;
        for(Accion acc: this.AccionesEnAreaSector){
            acc.setAreaSectorAccion(this);
        }
    }
    
    public void setFortalezasEnAreaSector(List<Fortaleza> FortalezasEnAreaSector) {
        this.FortalezasEnAreaSector = FortalezasEnAreaSector;
        for(Fortaleza fort: this.FortalezasEnAreaSector){
            fort.setAreaSectorFortaleza(this);
        }
    }
    
    
    public void setUsuariosEnAreaSector(List<Usuario> UsuariosEnAreaSector){
        this.UsuariosEnAreaSector = UsuariosEnAreaSector;
        for(Usuario usuario: this.UsuariosEnAreaSector){
            if(!usuario.getAreaSectorUsuario().equals(this)){
                usuario.setAreaSectorUsuario(this);
            }
        }
    }
    
    // Listas
    /*
        Acciones
    */
    public void addAccionEnAreaSector(Accion AccionEnAreaSector){
        this.AccionesEnAreaSector.add(AccionEnAreaSector);
        if(AccionEnAreaSector.getAreaSectorAccion()== null || !AccionEnAreaSector.getAreaSectorAccion().equals(this))
            AccionEnAreaSector.setAreaSectorAccion(this);
    }
    
    public void removeAccionEnAreaSector(Accion AccionEnAreaSector){
        this.AccionesEnAreaSector.remove(AccionEnAreaSector);
        if(AccionEnAreaSector.getAreaSectorAccion()!=null && AccionEnAreaSector.getAreaSectorAccion().equals(this))
            AccionEnAreaSector.setAreaSectorAccion(null);
    }
    
    public void removeAccionEnAreaSector(int IdAccionEnAreaSector){
        Iterator<Accion> it = this.AccionesEnAreaSector.iterator() ;
        while(it.hasNext()){
            Accion ac = it.next();
            if(ac.getId()==IdAccionEnAreaSector){
                it.remove();
                if(ac.getAreaSectorAccion()!=null && ac.getAreaSectorAccion().equals(this))
                    ac.setAreaSectorAccion(null);
            }
        }
    }
    
    /*
        Fortalezas
    */
    
    public void addFortalezaEnAreaSector(Fortaleza FortalezaEnAreaSector){
        this.FortalezasEnAreaSector.add(FortalezaEnAreaSector);
        if(FortalezaEnAreaSector.getAreaSectorFortaleza()==null || !FortalezaEnAreaSector.getAreaSectorFortaleza().equals(this))
            FortalezaEnAreaSector.setAreaSectorFortaleza(this);
    }
    
    public void removeFortalezaEnAreaSector(Fortaleza FortalezaEnAreaSector){
        this.FortalezasEnAreaSector.remove(FortalezaEnAreaSector);
        if(FortalezaEnAreaSector.getAreaSectorFortaleza()!=null && FortalezaEnAreaSector.getAreaSectorFortaleza().equals(this))
            FortalezaEnAreaSector.setAreaSectorFortaleza(null);
    }
    
    public void removeFortalezaEnAreaSector(int IdFortalezaEnAreaSector){
        Iterator<Fortaleza> it = this.FortalezasEnAreaSector.iterator() ;
        while(it.hasNext()){
            Fortaleza f = it.next();
            if(f.getId()==IdFortalezaEnAreaSector){
                it.remove();
                if(f.getAreaSectorFortaleza()!=null && f.getAreaSectorFortaleza().equals(this))
                    f.setAreaSectorFortaleza(null);
            }
        }
    }
       
    /*
        Usuario
    */
    public void addUsuarioEnAreaSector(Usuario UsuarioEnAreaSector){
        this.UsuariosEnAreaSector.add(UsuarioEnAreaSector);
        if(UsuarioEnAreaSector.getAreaSectorUsuario()==null || !UsuarioEnAreaSector.getAreaSectorUsuario().equals(this))
            UsuarioEnAreaSector.setAreaSectorUsuario(this);
    }
    
    public void removeUsuarioEnAreaSector(Usuario UsuarioEnAreaSector){
        this.UsuariosEnAreaSector.remove(UsuarioEnAreaSector);
        if(UsuarioEnAreaSector.getAreaSectorUsuario()!=null && UsuarioEnAreaSector.getAreaSectorUsuario().equals(this))
            UsuarioEnAreaSector.setAreaSectorUsuario(null);
    }

    @Override
    /**
     * Compara con otro area.
     * La comparacion se realiza por Nombre de Area.
     * Return 0 si son iguales, -1 si es menor, 1 si es mayor.
     */
    public int compareTo(Area OtraArea) {
        return this.Nombre.compareToIgnoreCase(OtraArea.Nombre);
    }
    }



