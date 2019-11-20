/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.herramientas;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.estado.EnumEstado;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.usuario.Usuario;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Diego
 */
@ManagedBean
@Stateless
public class ControladorAlertas implements Serializable{
    private SendMail cMail = new SendMail();

    @Inject
    private FacadeLectura fLectura;
    
    private Map<Integer, Usuario> Usuarios;
    private Map<Integer, Accion> Acciones;
    
    // Metodos

    /**
     * Envia un correo de alerta segun el evento.
     * Se extrae la informacion correspondiente al tipo de evento y se seleccionada el mensaje adecuado.
     * El evento contiene usuario destinatario y/o sector, se define el correo a utilizar segun el usuario recibe alerta o este vigente.
     * @param evento 
     */
    public void EnviarAlerta(Evento evento){
        Usuario usuario = Usuarios.get(evento.getIdUsuarioResponsable());
        String to = "";
        // si el usuario se dio de baja o no recibe alertas el destinatario sera el sector.
        if(usuario.getFechaBaja() != null || !usuario.isRecibeAlertas()){
            to = usuario.getAreaSectorUsuario().getCorreo();
        }else{
            to = usuario.getCorreo();
        }
        String asunto = "M.A.Y.M. - Plazo Cumplido";
        String mensaje = "";
        switch(evento.getTipo()){
            case IMPLEMENTACION_ACTIVIDAD:
                mensaje = "<h2 style='color:brown;font-family: sans-serif'> Se cumplio el plazo para la implementacion de la actividad de la Accion con Id " + evento.getIdAccion() + ":</h2>";
                mensaje = mensaje + "<p style='color:chocolate;font-family: sans-serif'> "+ usuario.GetActividad(evento.getIdActividad()).getDescripcion() +" </p>";
                break;
                
            case IMPLEMENTACION_ACCION:
                mensaje = "<h2 style='color:brown;font-family: sans-serif'> Se cumplio el plazo para la comprobar la implementacion de la Accion con Id " + evento.getIdAccion() + ":</h2>";
                mensaje = mensaje + "<p style='color:chocolate;font-family: sans-serif'> "+ Acciones.get(evento.getIdAccion()).getDescripcion() +" </p>";
                break;
                
            case VERIFICACION_EFICACIA:
                mensaje = "<h2 style='color:brown;font-family: sans-serif'> Se cumplio el plazo para la verificar la eficacia de la Accion con Id " + evento.getIdAccion() + ":</h2>";
                mensaje = mensaje + "<p style='color:chocolate;font-family: sans-serif'> "+ Acciones.get(evento.getIdAccion()).getDescripcion() +" </p>";
                break;
                
        }
        System.out.println("Mensaje: " + mensaje);
        //cMail.enviarMail(to, mensaje, asunto);
    }
    
    @PostConstruct
    public void init(){
        List<Usuario> lstUsuarios = fLectura.GetUsuarios(false);
        Usuarios = new HashMap<>();
        for(Usuario usuario: lstUsuarios){
            Usuarios.put(usuario.getId(), usuario);
        }
        List<Accion> lstAcciones = fLectura.ListarAcciones();
        Acciones = new HashMap<>();
        for(Accion accion: lstAcciones){
            if(accion.getEstadoAccion() != EnumEstado.CERRADA && accion.getEstadoAccion() != EnumEstado.DESESTIMADA){
                Acciones.put(accion.getId(), accion);
            }
        }
    }
}
