/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.empresas;

import com.dperez.maymweb.facades.FacadeAdministrador;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;



@Named
@ViewScoped
public class CrearEmpresa implements Serializable {
    @Inject
    private FacadeAdministrador fAdmin;
    
    private int Id;
    private String NombreEmpresa;
    private String DireccionEmpresa;
    private String TelefonoEmpresa;
    private String CorreoEmpresa;
    private String Fax;
    private String Descripcion;
    private String Imagen;
    private String NumeroEmpresa;
    
    //  Getters
    
    public int getId() {return Id;}
    public String getNombreEmpresa() {return NombreEmpresa;}
    public String getDireccionEmpresa() {return DireccionEmpresa;}
    public String getTelefonoEmpresa() {return TelefonoEmpresa;}
    public String getCorreoEmpresa() {return CorreoEmpresa;}
    public String getFax() {return Fax;}
    public String getDescripcion() {return Descripcion;}
    public String getImagen() {return Imagen;}
    public String getNumeroEmpresa() {return NumeroEmpresa;}
    
    //  Setters
    
    public void setId(int Id) {this.Id = Id;}
    public void setNombreEmpresa(String NombreEmpresa) {this.NombreEmpresa = NombreEmpresa;}
    public void setDireccionEmpresa(String DireccionEmpresa) {this.DireccionEmpresa = DireccionEmpresa;}
    public void setTelefonoEmpresa(String TelefonoEmpresa) {this.TelefonoEmpresa = TelefonoEmpresa;}
    public void setCorreoEmpresa(String CorreoEmpresa) {this.CorreoEmpresa = CorreoEmpresa;}
    public void setFax(String Fax) {this.Fax = Fax;}
    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}
    public void setImagen(String Imagen) {this.Imagen = Imagen;}
    public void setNumeroEmpresa(String NumeroEmpresa) {this.NumeroEmpresa = NumeroEmpresa;}    
    
    //  Metodos
    public boolean comprobarDisponibilidadId(int idEmpresa){
        return fAdmin.ExisteEmpresa(idEmpresa);
    }
    
    public void registrarEmpresa() throws IOException{
        String mensaje = "";
        if(!comprobarDisponibilidadId(Id)){
            mensaje = "El numero de Empresa ya existe.";
        }else{
            //  registrar empresa
            if(fAdmin.NuevaEmpresa(Id, NombreEmpresa, DireccionEmpresa, TelefonoEmpresa, CorreoEmpresa, Fax, Descripcion, NumeroEmpresa)==null){
                mensaje = "No se pudo registrar la Empresa";
            }else{
                String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(url+"/Views/Main/Main.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            }
        }
        FacesContext.getCurrentInstance().addMessage("frm-datos-empresa:button-registrar-empresa", new FacesMessage(SEVERITY_ERROR, mensaje, mensaje));
        FacesContext.getCurrentInstance().renderResponse();
    }
    
}
