/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.herramientas.eventos;

import com.dperez.maym.web.herramientas.alertas.AlertasAcciones;
import com.dperez.maym.web.herramientas.alertas.ControladorAlertas;
import com.dperez.maymweb.facades.FacadeLectura;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Diego
 */
@Stateless
public class RepetidorTareas {
 
    private FacadeLectura fLectura;
    @Inject
    private ControladorAlertas cAlertas;
    
    public RepetidorTareas(){
        fLectura = new FacadeLectura();
    }
    
    // cada 15 dias, los domingos, todos los meses, todos los años, a la hora 4:00 0s
    @Schedule(dayOfWeek = "Sun", month = "*", hour = "4", dayOfMonth = "15", year = "*", minute = "0", second = "0")
    
    // solo para testing envio cada 2 minutos:
    //@Schedule(month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "0/2", second = "0")
    public void RenviarAlertasEventos() {
        // Acciones Correctivas
        Thread tCorrectivas = new Thread(new AlertasAcciones(fLectura.listarAccionesCorrectivas(), cAlertas) );
        // Acciones de Mejora
        Thread tMejoras = new Thread(new AlertasAcciones(fLectura.listarAccionesMejoras(), cAlertas));
        
        tCorrectivas.start();       
        tMejoras.start();
    }
    
}
