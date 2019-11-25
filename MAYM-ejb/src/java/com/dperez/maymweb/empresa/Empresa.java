/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.empresa;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author dperez
 */
public class Empresa implements Serializable {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String NombreEmpresa = new String();
    private String DireccionEmpresa = new String();
    private String TelefonoEmpresa = new String();
    private String CorreoEmpresa = new String();
    private String FaxEmpresa = new String();
    private String DescripcionEmpresa = new String();
    private String ImagenEmpresa = new String();
    private String NumeroEmpresa = new String();
    
    //  Constructores
    public Empresa(){
    }
    public Empresa(int idEmpresa, String NombreEmpresa, String DireccionEmpresa, String TelefonoEmpresa, String CorreoEmpresa, 
            String FaxEmpresa, String DescripcionEmpresa, String NumeroEmpresa){
        this.Id = idEmpresa;
        this.NombreEmpresa = NombreEmpresa;
        this.DireccionEmpresa = DireccionEmpresa;
        this.TelefonoEmpresa = TelefonoEmpresa;
        this.CorreoEmpresa = CorreoEmpresa;
        this.FaxEmpresa = FaxEmpresa;
        this.DescripcionEmpresa = DescripcionEmpresa;
        this.NumeroEmpresa = NumeroEmpresa;
    }
    
    //  Getters
    public int getId() {return Id;}
    public String getNombreEmpresa() {return NombreEmpresa;}
    public String getDireccionEmpresa() {return DireccionEmpresa;}
    public String getTelefonoEmpresa() {return TelefonoEmpresa;}
    public String getCorreoEmpresa() {return CorreoEmpresa;}
    public String getFaxEmpresa(){return this.FaxEmpresa;}
    public String getImagenEmpresa(){return this.ImagenEmpresa;}
    public String getDescripcionEmpresa() {return DescripcionEmpresa;}
    public String getNumeroEmpresa() {return NumeroEmpresa;}
    
    //  Setters
    public void setId(int Id) {this.Id = Id;}
    public void setNombreEmpresa(String NombreEmpresa) {this.NombreEmpresa = NombreEmpresa;}
    public void setDireccionEmpresa(String DireccionEmpresa) {this.DireccionEmpresa = DireccionEmpresa;}
    public void setTelefonoEmpresa(String TelefonoEmpresa) {this.TelefonoEmpresa = TelefonoEmpresa;}
    public void setCorreoEmpresa(String CorreoEmpresa) {this.CorreoEmpresa = CorreoEmpresa;}
    public void setFaxEmpresa(String FaxEmpresa){this.FaxEmpresa = FaxEmpresa;}
    public void setDescripcion(String DescripcionEmpresa){this.DescripcionEmpresa = DescripcionEmpresa;}
    public void setImagenEmpresa(String ImagenEmpresa){this.ImagenEmpresa = ImagenEmpresa;}
    public void setDescripcionEmpresa(String DescripcionEmpresa) {this.DescripcionEmpresa = DescripcionEmpresa;}
    public void setNumeroEmpresa(String NumeroEmpresa) {this.NumeroEmpresa = NumeroEmpresa;}
    
}

