/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.usuario;

import com.dperez.maymweb.area.Area;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Usuarios")
public class Usuario implements Serializable, Comparable<Usuario> {
    @Id
    private int Id;
    private String Nombre = new String();
    private String Apellido = new String();
    private String Correo = new String();
    private boolean RecibeAlertas;
    @Temporal(TemporalType.DATE)
    private Date FechaBaja;
    
    @ManyToOne
    private Area AreaSectorUsuario;
        
    private EnumPermiso PermisoUsuario;
    
    @OneToOne(orphanRemoval = true, mappedBy = "UsuarioCredencial", cascade = CascadeType.PERSIST)
    private Credencial CredencialUsuario;
        
    @OneToMany(mappedBy = "UsuarioResponsable")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Responsable Responsable;
    
    // Constructores
    public Usuario(){
                
    }
    public Usuario(String NombreUsuario, String ApellidoUsuario, String CorreoUsuario, boolean RecibeAlertas, EnumPermiso PermisoUsuario){
        this.Nombre = NombreUsuario;
        this.Apellido = ApellidoUsuario;
        this.Correo = CorreoUsuario;
        this.RecibeAlertas = RecibeAlertas;
        this.PermisoUsuario = PermisoUsuario;
    }
    
    // Getters
    public int getId() {return this.Id;}
    public String getNombre() {return this.Nombre;}
    public String getApellido() {return this.Apellido;}
    public String getCorreo() {return this.Correo;}
    public boolean isRecibeAlertas() {return this.RecibeAlertas;}
    public Date getFechaBaja(){return this.FechaBaja;}
    
    public EnumPermiso getPermisoUsuario() {return PermisoUsuario;}
    
    public Credencial getCredencialUsuario() {return CredencialUsuario;}
    
    public Area getAreaSectorUsuario(){return this.AreaSectorUsuario;}
    
    public Responsable getResponsable() {return Responsable;}    
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    public void setApellido(String Apellido) {this.Apellido = Apellido;}
    public void setCorreo(String Correo) {this.Correo = Correo;}
    public void setRecibeAlertas(boolean RecibeAlertas) {this.RecibeAlertas = RecibeAlertas;}
    public void setFechaBaja(Date FechaBaja){this.FechaBaja = FechaBaja;}
    
    public void setPermisoUsuario(EnumPermiso PermisoUsuario) {
        this.PermisoUsuario = PermisoUsuario;
    }
    
    public void setCredencialUsuario(Credencial CredencialUsuario) {
        this.CredencialUsuario = CredencialUsuario;
        if(CredencialUsuario != null){
            if(CredencialUsuario.getUsuarioCredencial()!=null)
                CredencialUsuario.setUsuarioCredencial(this);
        }
    }
    
    public void setAreaSectorUsuario(Area AreaSectorUsuario){
        this.AreaSectorUsuario = AreaSectorUsuario;
        if(AreaSectorUsuario != null){
            if(!AreaSectorUsuario.getUsuariosEnAreaSector().contains(this)){
                AreaSectorUsuario.getUsuariosEnAreaSector().add(this);
            }
        }
    }    
    
    public void setResponsable(Responsable UsuarioResponsable) {
        this.Responsable = UsuarioResponsable;
        if(UsuarioResponsable != null){
            if(UsuarioResponsable.getUsuarioResponsable()!= this){
                UsuarioResponsable.setUsuarioResponsable(this);
            }
        }
    }
          
    // Metodos
    /***
     * Devuelve el nombre completo del Usuario (Nombre+Apellido)
     * @return
     */
    public String GetNombreCompleto(){
        return this.Nombre + " " + this.Apellido;
    }
    
    /**
     * Comprueba si el usuario esta vigente.
     * @return True: el usuario esta vigente, de lo contrario retorna False.
     */
    public boolean IsVigente(){
        return this.FechaBaja == null;
    }
    
    @Override
    public int compareTo(Usuario OtroUsuario) {
        return this.Apellido.compareTo(OtroUsuario.Apellido);
    }
}
