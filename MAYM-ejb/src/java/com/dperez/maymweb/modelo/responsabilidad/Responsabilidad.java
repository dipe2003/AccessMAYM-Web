/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.dperez.maymweb.modelo.responsabilidad;

import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author dipe2
 */
@Entity
public class Responsabilidad implements Serializable{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Responsable> responsables;
    
    public Responsabilidad(){
        this.responsables = new ArrayList<>();
    }
    
    public Responsabilidad(String nombre){
        this.responsables = new ArrayList<>();
        this.nombre = nombre;
    }
    
    public int getId(){return this.id;}
    public String getNombre(){return this.nombre;}
    
    public List<Responsable> getResponsables(){return this.responsables;}
    
    public void setId(int id){this.id = id;}
    public void setNombre(String nombre){this.nombre = nombre;}
    
    public void setResponsables(List<Responsable> lista) {this.responsables = lista;}
    
    public void addResponsable(Responsable responsable){
        this.responsables.add(responsable);
    }
    
    public Responsable crearResponsable(Usuario usuario){
        Responsable responsable = new Responsable(this, usuario);
        this.responsables.add(responsable);
        return responsable;
    }
    
    public Responsable getResponsable(int idResponsable){
        return this.responsables.stream()
                .filter(r->r.getId() == idResponsable)
                .findFirst()
                .orElse(null);
    }
    
    public boolean existeResponsable(int idResponsable){
        return this.responsables.stream()
                .anyMatch(r->r.getId() == idResponsable);
    }
    
    public int darBajaResponsable(int idResponsable, Date fecha){
        if (existeResponsable(idResponsable)){
            getResponsable(idResponsable).setFechaBaja(fecha);
            return 1;
        }
        return -1;
    }
}
