/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.herramientas.alertas;

import com.dperez.maym.web.herramientas.eventos.EventoActividad;
import com.dperez.maym.web.herramientas.eventos.TipoEvento;
import com.dperez.maym.web.herramientas.eventos.EventoAccion;
import com.dperez.maym.web.herramientas.eventos.Evento;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author Diego
 */
@Stateless
public class ControladorAlertas implements Serializable{
    private SendMail cMail;
    
    // Metodos
    
    /**
     * Envia un correo de alerta segun el evento.
     * Se extrae la informacion correspondiente al tipo de evento y se seleccionada el mensaje adecuado.
     * El evento contiene usuario destinatario y/o sector, se define el correo a utilizar segun el usuario recibe alerta o este vigente.
     * @param evento
     */
    public void EnviarAlerta(Evento evento){
        if (evento instanceof EventoAccion){
            EnviarAlertaAccion((EventoAccion) evento);
        }else if (evento instanceof EventoActividad){
            EnviarAlertaActividad((EventoActividad) evento);
        }
    }
    
    private void EnviarAlertaAccion(EventoAccion evento){
        Usuario usuario;
        String to = "";
        String asunto = "M.A.Y.M. - Plazo Cumplido";
        String mensaje = "";
        if(evento.getTipo() == TipoEvento.IMPLEMENTACION_ACCION){
            usuario = evento.getAccion().getComprobacionImplementacion().getResponsableComprobacion().getUsuarioResponsable();
            // si el usuario se dio de baja o no recibe alertas el destinatario sera el sector.
            if(usuario.getFechaBaja() != null || !usuario.isRecibeAlertas()){
                to = usuario.getAreaUsuario().getCorreo();
            }else{
                to = usuario.getCorreo();
            }
            mensaje = "<h2 style='color:brown;font-family: sans-serif'> Se cumplio el plazo para la comprobar la implementacion de la Accion con Id " + evento.getAccion().getId() + ":</h2>";
            mensaje = mensaje + "<p style='color:chocolate;font-family: sans-serif'> "+ evento.getAccion().getDescripcion() +" </p>";
        }else{
            usuario = evento.getAccion().getComprobacionEficacia().getResponsableComprobacion().getUsuarioResponsable();
            // si el usuario se dio de baja o no recibe alertas el destinatario sera el sector.
            if(usuario.getFechaBaja() != null || !usuario.isRecibeAlertas()){
                to = usuario.getAreaUsuario().getCorreo();
            }else{
                to = usuario.getCorreo();
            }
            mensaje = "<h2 style='color:brown;font-family: sans-serif'> Se cumplio el plazo para la verificar la eficacia de la Accion con Id " + evento.getAccion().getId() + ":</h2>";
            mensaje = mensaje + "<p style='color:chocolate;font-family: sans-serif'> "+ evento.getAccion().getDescripcion() +" </p>";
        }
        if(usuario.getEmpresaUsuario().getOpcionesSistema().getOpcionesCorreo().isAlertasActivadas()){   
            try {
                cMail = new SendMail(usuario.getEmpresaUsuario().getOpcionesSistema().getOpcionesCorreo());
                //cMail.enviarMail(to, mensaje, asunto);
                System.out.println("Mensaje: " + mensaje);
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
    
    private void EnviarAlertaActividad(EventoActividad evento){
        Usuario usuario;
        String to = "";
        String asunto = "M.A.Y.M. - Plazo Cumplido";
        String mensaje = "";

        usuario = evento.getActividad().getResponsableImplementacion().getUsuarioResponsable();
        // si el usuario se dio de baja o no recibe alertas el destinatario sera el sector.
        if (usuario.getFechaBaja() != null || !usuario.isRecibeAlertas()) {
            to = usuario.getAreaUsuario().getCorreo();
        } else {
            to = usuario.getCorreo();
        }
        mensaje = "<h2 style='color:brown;font-family: sans-serif'> Se cumplio el plazo para la implementacion de la actividad de la Accion con Id " + evento.getActividad().getAccionActividad().getId() + ":</h2>";
        mensaje = mensaje + "<p style='color:chocolate;font-family: sans-serif'> " + evento.getActividad().getDescripcion() + " </p>";

        if (usuario.getEmpresaUsuario().getOpcionesSistema().getOpcionesCorreo().isAlertasActivadas()) {
            try {
                cMail = new SendMail(usuario.getEmpresaUsuario().getOpcionesSistema().getOpcionesCorreo());
                //cMail.enviarMail(to, mensaje, asunto);
                System.out.println("Mensaje: " + mensaje);
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
    
}
