
package com.dperez.maymweb.herramientas;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Stateless
@ManagedBean
public class ControladorPropiedad {
    @Inject
    private IOPropiedades ioProp;
    
    public ControladorPropiedad(){}
    
    //  Propiedades
    
    public String getMailFrom(){
        return ioProp.getPropiedad("mail_from");
    }
    public String getMailSmtp() {
        return ioProp.getPropiedad("mail_smtp");
    }
    public String getMailUser() {
        return ioProp.getPropiedad("mail_user");
    }
    public String getMailPass(){
        return ioProp.getPropiedad("mail_pass");
    }
    public int getMailPort(){
        return Integer.parseInt(ioProp.getPropiedad("mail_port"));
    }
        
}

