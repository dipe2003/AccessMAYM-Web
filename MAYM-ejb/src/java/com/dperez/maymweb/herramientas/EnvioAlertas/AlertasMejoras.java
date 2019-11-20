/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.herramientas.EnvioAlertas;

import com.dperez.maymweb.accion.Accion;
import com.dperez.maymweb.accion.acciones.Mejora;
import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.estado.EnumEstado;
import com.dperez.maymweb.herramientas.ControladorAlertas;
import com.dperez.maymweb.herramientas.Evento;
import com.dperez.maymweb.herramientas.TipoEvento;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dperez
 */
public class AlertasMejoras implements Runnable {
    
    private ControladorAlertas cAlertas;
       
    private final List<Accion> AccionesMejora;
    
    public AlertasMejoras(){
        this.AccionesMejora = new ArrayList<>();
    }
    
    public AlertasMejoras(List<Accion> AccionesMejora, ControladorAlertas cAlertas){
        this.AccionesMejora = AccionesMejora;
        this.cAlertas = cAlertas;
    }    
    
    /**
     * Envia las alertas para las acciones Mejora.
     * Recorrer las acciones que no esten desestimadas o cerradas.
     * Si la accion esta implementada, comprueba las fechas de implementacion y eficacia y envia,sino recorre las actividades, comprueba
     * las fechas de implementacion y envia las alertas.
     * @param AccionesMejora
     */
    private void EnviarAlertasMejora(){
        Date Hoy = new Date();
        for(Accion accion: AccionesMejora){
            if(accion.getEstadoAccion() != EnumEstado.CERRADA && accion.getEstadoAccion() != EnumEstado.DESESTIMADA && 
                    accion.getComprobacionImplementacion() != null && accion.getComprobacionEficacia() != null){
                // Primero Si esta implementada
                if(accion.EstanImplementadasActividades()){
                    if(accion.getEstadoAccion() == EnumEstado.PROCESO_IMP){
                        if(accion.getComprobacionImplementacion().getFechaComprobacion() == null ||
                                accion.getComprobacionImplementacion().getFechaEstimada().compareTo(Hoy)< 0){
                            Evento evento = new Evento(TipoEvento.IMPLEMENTACION_ACCION, accion.getComprobacionImplementacion().getResponsable().getId(),
                                    accion.getId(),0);
                            cAlertas.EnviarAlerta(evento);
                        }
                    }else {
                        if(accion.getComprobacionEficacia().getFechaComprobacion() == null ||
                                accion.getComprobacionEficacia().getFechaEstimada().compareTo(Hoy)< 0){
                            Evento evento = new Evento(TipoEvento.VERIFICACION_EFICACIA, accion.getComprobacionImplementacion().getResponsable().getId(),
                                    accion.getId(),0);
                            cAlertas.EnviarAlerta(evento);
                        }
                    }
                }else{
                    List<Actividad> Actividades = ((Mejora)accion).getActividades();
                    for(Actividad actividad: Actividades){
                        if(actividad.getFechaImplementacion() == null && actividad.getFechaEstimadaImplementacion().compareTo(Hoy)<0){
                            Evento evento = new Evento(TipoEvento.IMPLEMENTACION_ACTIVIDAD, actividad.getResponsableImplementacion().getId(),
                                    accion.getId(),actividad.getIdActividad());
                            cAlertas.EnviarAlerta(evento);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void run() {
        EnviarAlertasMejora();
    }
    
}
