/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.usuario;

import com.dperez.maymweb.modelo.area.Area;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Usuarios")
public class Usuario implements Serializable, Comparable<Usuario> {
    @Id
    private int id;
    private String nombre = new String();
    private String apellido = new String();
    private String correo = new String();
    private boolean recibeAlertas;
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;
    
    @ManyToOne
    private Area areaUsuario;
    
    private EnumPermiso permisoUsuario;
    
    @OneToOne(mappedBy= "usuarioCredencial" ,orphanRemoval = true, cascade = CascadeType.ALL)
    private Credencial credencialUsuario;
    
    @OneToMany(mappedBy="usuarioResponsable", cascade = CascadeType.ALL)
    private List<Responsable> responsablesUsuario;
    
    // Constructores
    public Usuario(){
        
    }
    public Usuario(String nombreUsuario, String apellidoUsuario, String correoUsuario, boolean recibeAlertas, EnumPermiso permisoUsuario){
        this.nombre = nombreUsuario;
        this.apellido = apellidoUsuario;
        this.correo = correoUsuario;
        this.recibeAlertas = recibeAlertas;
        this.permisoUsuario = permisoUsuario;
    }
    
    // Getters
    public int getId() {return this.id;}
    public String getNombre() {return this.nombre;}
    public String getApellido() {return this.apellido;}
    public String getCorreo() {return this.correo;}
    public boolean isRecibeAlertas() {return this.recibeAlertas;}
    public Date getFechaBaja(){return this.fechaBaja;}
    
    public EnumPermiso getPermisoUsuario() {return permisoUsuario;}    
    public Credencial getCredencialUsuario() {return credencialUsuario;}    
    public Area getAreaUsuario(){return this.areaUsuario;}    
    public List<Responsable> getResponsablesUsuario() {return responsablesUsuario;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public void setCorreo(String correo) {this.correo = correo;}
    public void setRecibeAlertas(boolean recibeAlertas) {this.recibeAlertas = recibeAlertas;}
    public void setFechaBaja(Date fechaBaja){this.fechaBaja = fechaBaja;}
    
    public void setPermisoUsuario(EnumPermiso permisoUsuario) {
        this.permisoUsuario = permisoUsuario;
    }
    
    public void setCredencialUsuario(Credencial credencialUsuario) {
        this.credencialUsuario = credencialUsuario;
    }
    
    public void setAreaUsuario(Area areaUsuario){
        this.areaUsuario = areaUsuario;
    }
    
    public void setResponsablesUsuario(List<Responsable> responsablesUsuario) {
        this.responsablesUsuario = responsablesUsuario;
    }
    
    // Metodos
    /***
     * Devuelve el nombre completo del Usuario (Nombre+Apellido)
     * @return
     */
    public String getNombreCompleto(){
        return this.nombre + " " + this.apellido;
    }
    
    /**
     * Comprueba si el usuario esta vigente.
     * @return True: el usuario esta vigente, de lo contrario retorna False.
     */
    public boolean isVigente(){
        return this.fechaBaja == null;
    }
    
    @Override
    public int compareTo(Usuario OtroUsuario) {
        return this.apellido.compareTo(OtroUsuario.apellido);
    }
    public boolean tieneComprobacionAsignada(){
        if (responsablesUsuario.isEmpty()) return false;
        boolean comprobacionAsignada = false;
        for(Responsable responsable:responsablesUsuario){
            comprobacionAsignada = responsable.getComprobacionesResponsable().stream()
                    .anyMatch(c->c.getResponsableComprobacion().getUsuarioResponsable().getId()== this.id);
        }
        
        boolean implementacionAsignada = false;
        for(Responsable responsable:responsablesUsuario){
            implementacionAsignada = responsable.getActividadesResponsable().stream()
                    .anyMatch(a->a.getResponsableImplementacion().getUsuarioResponsable().getId()==this.id);
        }
        return comprobacionAsignada && implementacionAsignada == false;
    }
    
    /***
     * Comprueba si algun responsable del usuario corresponde a la responsabilidad indicada.
     * @param idResponsabilidad
     * @return True si corresponde.
     */
    public boolean tieneResponsabilidadAsignada(int idResponsabilidad){
        return this.responsablesUsuario.stream()
                .anyMatch(responsable->responsable.getResponsabilidadResponsable().getId() == idResponsabilidad);
    }
}
