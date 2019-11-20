/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.usuario;

import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.usuario.permiso.EnumPermiso;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
    
    @OneToMany(mappedBy = "ResponsableImplementacion")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Actividad> Actividades;
    
    @OneToMany(mappedBy = "Responsable")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comprobacion> Comprobaciones;
    
    // Constructores
    public Usuario(){
        this.Actividades = new ArrayList<>();
        this.Comprobaciones = new ArrayList<>();
    }
    public Usuario(String NombreUsuario, String ApellidoUsuario, String CorreoUsuario, boolean RecibeAlertas, EnumPermiso PermisoUsuario){
        this.Nombre = NombreUsuario;
        this.Apellido = ApellidoUsuario;
        this.Correo = CorreoUsuario;
        this.RecibeAlertas = RecibeAlertas;
        this.PermisoUsuario = PermisoUsuario;
        this.Actividades = new ArrayList<>();
        this.Comprobaciones = new ArrayList<>();
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
    
    public List<Actividad> getActividades() {return Actividades;}
    
    public List<Comprobacion> getComprobaciones() {return Comprobaciones;}
    
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
    
    public void setActividades(List<Actividad> Actividades) {
        this.Actividades = Actividades;
        for(Actividad med: this.Actividades){
            med.setResponsableImplementacion(this);
        }
    }
    
    public void setComprobaciones(List<Comprobacion> Comprobaciones) {
        this.Comprobaciones = Comprobaciones;
        for(Comprobacion comprobacion: this.Comprobaciones){
            comprobacion.setResponsable(this);
        }
    }
    
    // Listas
    public void AddActividad(Actividad actividad){
        if(actividad != null){
            this.Actividades.add(actividad);
            if(actividad.getResponsableImplementacion() != null && !actividad.getResponsableImplementacion().equals(this))
                actividad.setResponsableImplementacion(this);
        }
    }
    
    public void RemoveActividad(Actividad actividad){
        if(actividad != null){
            this.Actividades.remove(actividad);
            if(actividad.getResponsableImplementacion() != null &&
                    actividad.getResponsableImplementacion().equals(this))
                actividad.setResponsableImplementacion(null);
        }
    }
    
    public void RemoveActividad(int actividad){
        Iterator<Actividad> it = this.Actividades.iterator() ;
        while(it.hasNext()){
            Actividad m = it.next();
            if(m.getIdActividad()== actividad){
                it.remove();
                if(m.getResponsableImplementacion()!=null && m.getResponsableImplementacion().equals(this))
                    m.setResponsableImplementacion(null);
            }
        }
    }
    
    /**
     * Devuelve la actividad indicada por su id.
     * @param IdActividad
     * @return Actividad, null si no se encuentra.
     */
    public Actividad GetActividad(int IdActividad){
        return Actividades.stream()
                .filter(actividad->actividad.getIdActividad() == IdActividad)
                .findFirst()
                .orElse(null);
    }
    
    public void AddComprobacion(Comprobacion comprobacion){
        if(comprobacion != null){
            this.Comprobaciones.add(comprobacion);
            if(comprobacion.getResponsable() != null && !comprobacion.getResponsable().equals(this))
                comprobacion.setResponsable(this);
        }
    }
    
    public void RemoveComprobacion(Comprobacion comprobacion){
        if(comprobacion != null){
            this.Comprobaciones.remove(comprobacion);
            if(comprobacion.getResponsable() != null && comprobacion.getResponsable().equals(this))
                comprobacion.setResponsable(null);
        }
    }
    
    public void RemoveComprobacion(int IdComprobacion){
        Iterator<Comprobacion> it = this.Comprobaciones.iterator();
        while(it.hasNext()){
            Comprobacion c = it.next();
            if(c.getId() == IdComprobacion){
                it.remove();
                if(c.getResponsable() != null && c.getResponsable().equals(this))
                    c.setResponsable(null);
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
