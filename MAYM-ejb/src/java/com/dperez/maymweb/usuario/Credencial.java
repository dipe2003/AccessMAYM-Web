/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.usuario;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Diego
 */
@Entity
@Table(name="Credenciales")
public class Credencial implements Serializable{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String Password = new String();
    private String PasswordKey = new String();
    
    @OneToOne(cascade = CascadeType.ALL)
    private Usuario UsuarioCredencial;
    
    //  Constructores
    public Credencial(){}
    public Credencial(String Password, String PasswordKey){
        this.Password = Password;
        this.PasswordKey = PasswordKey;
    }
    
    //  Getters
    
    public int getId() {return Id;}
    public String getPassword() {return Password;}
    public String getPasswordKey() {return PasswordKey;}
    
    public Usuario getUsuarioCredencial() {return UsuarioCredencial;}
    
    //  Setters
    
    public void setId(int Id) {this.Id = Id;}
    public void setPassword(String Password) {this.Password = Password;}
    public void setPasswordKey(String PasswordKey) {this.PasswordKey = PasswordKey;}
    
    public void setUsuarioCredencial(Usuario UsuarioCredencial) {
        this.UsuarioCredencial = UsuarioCredencial;
        if(UsuarioCredencial!= null && !UsuarioCredencial.getCredencialUsuario().equals(this))
            UsuarioCredencial.setCredencialUsuario(this);
    }
}
