/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.herramientas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Diego
 */
@Named("scheduler")
@Stateless
public class ProgramadorEventos {
    @Resource
    private TimerService timerService;
    
    @Inject
    private ControladorAlertas cAlertas;
    
    /**
     * Retorna una lista de eventos programados.
     * @return
     */
    public List<Timer> getEventosProgramados(){
        Collection<Timer> timers = timerService.getTimers();
        List<Timer> timerList = new ArrayList<>();
        Iterator<Timer> it = timers.iterator();
        Timer timer;
        while(it.hasNext()){
            timer = (Timer) it;
            if (timer.getInfo() instanceof Evento) {
                timerList.add(timer);
            }
        }
        return timerList;
    }
    
    /**
     * Comprueba la existencia del evento programado.
     * @param evento
     * @return 
     */
    public boolean ExisteEventoActividad(Evento evento){
        Collection<Timer> timers = timerService.getTimers();
        Iterator<Timer> it = timers.iterator();
        Timer timer;
        while(it.hasNext()){
            timer = (Timer) it.next();
            if (timer.getInfo() instanceof EventoActividad) {
                EventoActividad e = (EventoActividad) timer.getInfo();
                if(((EventoActividad)evento).getActividad().getIdActividad() == e.getActividad().getIdActividad()){
                    return true;
                }
            }
        }
        return false;
    }
    
        /**
     * Comprueba la existencia del evento programado.
     * @param evento
     * @return 
     */
    public boolean ExisteEventoAccion(Evento evento){
        Collection<Timer> timers = timerService.getTimers();
        Iterator<Timer> it = timers.iterator();
        Timer timer;
        while(it.hasNext()){
            timer = (Timer) it.next();
            if (timer.getInfo() instanceof EventoAccion) {
                EventoAccion e = (EventoAccion) timer.getInfo();
                if(((EventoAccion)evento).getAccion().getId() == e.getAccion().getId()){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Remueve todos los eventos programados asociados la accion.
     * @param evento
     */
    public void RemoverEventoActividad(Evento evento){
        Collection<Timer> timers = timerService.getTimers();
        List<Timer> timerList = new ArrayList<>();
        Iterator<Timer> it = timers.iterator();
        Timer timer;
        while(it.hasNext()){
            timer = (Timer) it.next();
            if (timer.getInfo() instanceof EventoActividad) {
                EventoActividad e = (EventoActividad) timer.getInfo();
                if(e.getActividad().getIdActividad() == ((EventoActividad)evento).getActividad().getIdActividad()){
                    it.remove();
                }
            }
        }
    }
    
    /**
     * Remueve todos los eventos programados asociados la accion.
     * @param evento
     */
    public void RemoverEventoAccion(Evento evento){
        Collection<Timer> timers = timerService.getTimers();
        List<Timer> timerList = new ArrayList<>();
        Iterator<Timer> it = timers.iterator();
        Timer timer;
        while(it.hasNext()){
            timer = (Timer) it.next();
            if (timer.getInfo() instanceof EventoAccion) {
                EventoAccion e = (EventoAccion) timer.getInfo();
                if(e.getAccion().getId() == ((EventoAccion)evento).getAccion().getId() && ((EventoAccion)evento).getTipo() == e.getTipo() ){
                    it.remove();
                }
            }
        }
    }
    
     /**
     * Remueve todos los eventos programados asociados la accion.
     * @param Id 
     */
    public void RemoverEventos(int Id){
        Collection<Timer> timers = timerService.getTimers();
        List<Timer> timerList = new ArrayList<>();
        Iterator<Timer> it = timers.iterator();
        Timer timer;
        while(it.hasNext()){
            timer = (Timer) it.next();
            if (timer.getInfo() instanceof EventoAccion) {
                EventoAccion e = (EventoAccion) timer.getInfo();
                if(e.getAccion().getId() == Id){
                    it.remove();
                }
            }else if (timer.getInfo() instanceof EventoActividad){
                EventoActividad e = (EventoActividad) timer.getInfo();
                if(e.getActividad().getIdActividad()== Id){
                    it.remove();
                }
            }
        }
    }
    
    /**
     * Crea una ScheduleExpression representando la fecha y hora del evento.
     * Se utiliza para crear la programacion del evento.
     * @param minutos
     * @param hora
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    public ScheduleExpression crearFechaEvento(int minutos, int hora, int dia, int mes, int anio){
        ScheduleExpression se = new ScheduleExpression()
                .minute(minutos)
                .hour(hora)
                .dayOfMonth(dia)
                .month(mes)
                .year(anio)
                .start(new Date(anio-1900, mes, dia, hora, minutos));
        return se;
    }
    
    /**
     * Crea un timer dentro del TimeService con la programacion del evento.
     * Este timer esta especificado para resistir la caida del servidor o reinicios.
     * @param fecha fecha y hora de la programacion del evento.
     * @param evento evento a programar.
     */
    public void ProgramarEvento(Date fecha, Evento evento){
        TimerConfig timerConfig = new TimerConfig(evento, true);
        timerService.createSingleActionTimer(fecha, timerConfig);
    }
    
    /**
     * Este metodo es ejecutado cada vez que el timer se cumple.
     * @param timer
     */
    @Timeout
    public void EjecucionEvento(Timer timer){
        // comprueba que sea un timer de evento
        if (timer.getInfo() instanceof EventoAccion) {
            EventoAccion evento = (EventoAccion) timer.getInfo();
            System.out.println("Ejecucion del evento.");
            System.out.println("Evento: " + evento.getTipo());
            cAlertas.EnviarAlerta(evento);
        }else if (timer.getInfo() instanceof EventoActividad) {
            EventoActividad evento = (EventoActividad) timer.getInfo();
            System.out.println("Ejecucion del evento.");
            System.out.println("Evento Actividad: " + evento.getActividad().getDescripcion());
            cAlertas.EnviarAlerta(evento);
        }
    }
}

