/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.usuario;

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
    private int id;
    private String password = new String();
    private String passwordKey = new String();
    
    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuarioCredencial;
    
    //  Constructores
    public Credencial(){}
    public Credencial(String Password, String PasswordKey){
        this.password = Password;
        this.passwordKey = PasswordKey;
    }
    
    //  Getters
    
    public int getId() {return id;}
    public String getPassword() {return password;}
    public String getPasswordKey() {return passwordKey;}
    
    public Usuario getUsuarioCredencial() {return usuarioCredencial;}
    
    //  Setters
    
    public void setId(int id) {this.id = id;}
    public void setPassword(String password) {this.password = password;}
    public void setPasswordKey(String passwordKey) {this.passwordKey = passwordKey;}
    
    public void setUsuarioCredencial(Usuario usuarioCredencial) {
        this.usuarioCredencial = usuarioCredencial;
    }
}
