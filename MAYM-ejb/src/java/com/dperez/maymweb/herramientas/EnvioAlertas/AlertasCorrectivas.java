/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.herramientas.EnvioAlertas;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.Correctiva;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.herramientas.ControladorAlertas;
import com.dperez.maymweb.herramientas.Evento;
import com.dperez.maymweb.herramientas.EventoAccion;
import com.dperez.maymweb.herramientas.EventoActividad;
import com.dperez.maymweb.herramientas.TipoEvento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dperez
 */

public class AlertasCorrectivas implements Serializable, Runnable {
    
    private ControladorAlertas cAlertas;
    
    private final List<Accion> AccionesCorrectivas;
    
    public AlertasCorrectivas(){
        this.AccionesCorrectivas = new ArrayList<>();
    }
    
    public AlertasCorrectivas(List<Accion> AccionesCorrectivas, ControladorAlertas cAlertas){
        this.AccionesCorrectivas = AccionesCorrectivas;
        this.cAlertas = cAlertas;
    }
    
    /**
     * Envia las alertas para las acciones correctivas.
     * Recorrer las acciones que no esten desestimadas o cerradas.
     * Si la accion esta implementada, comprueba las fechas de implementacion y eficacia y envia,sino recorre las actividades, comprueba
     * las fechas de implementacion y envia las alertas.
     * @param AccionesCorrectivas
     */
    private void enviarAlertasCorrectivas(){
        Date Hoy = new Date();
        for(Accion accion: AccionesCorrectivas){
            if(accion.getEstadoDeAccion() != Estado.CERRADA && accion.getEstadoDeAccion() != Estado.DESESTIMADA &&
                    accion.getComprobacionImplementacion() != null && accion.getComprobacionEficacia() != null){
                // Primero Si esta implementada
                if(accion.estaImplementadaAlgunaActividad()){
                    if(accion.getEstadoDeAccion() == Estado.PROCESO_IMP){
                        if(accion.getComprobacionImplementacion().getFechaComprobacion() == null ||
                                accion.getComprobacionImplementacion().getFechaEstimada().compareTo(Hoy)< 0){
                            Evento evento = new EventoAccion(TipoEvento.IMPLEMENTACION_ACCION, accion);
                            cAlertas.EnviarAlerta(evento);
                        }
                    }else {
                        if(accion.getComprobacionEficacia().getFechaComprobacion() == null ||
                                accion.getComprobacionEficacia().getFechaEstimada().compareTo(Hoy)< 0){
                            Evento evento = new EventoAccion(TipoEvento.VERIFICACION_EFICACIA, accion);
                            cAlertas.EnviarAlerta(evento);
                        }
                    }
                }else{
                    List<Actividad> Actividades = accion.getActividadesDeAccion();
                    for(Actividad actividad: Actividades){
                        if(actividad.getFechaImplementacion() == null || actividad.getFechaEstimadaImplementacion().compareTo(Hoy)<0){
                            Evento evento = new EventoActividad(actividad);
                            cAlertas.EnviarAlerta(evento);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void run() {
        enviarAlertasCorrectivas();
    }
    
}
