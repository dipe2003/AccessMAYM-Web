/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.facades;

import com.dperez.maymweb.accion.comprobaciones.ResultadoComprobacion;
import com.dperez.maymweb.controlador.registro.ControladorEdicionRegistro;
import com.dperez.maymweb.controlador.registro.ControladorRegistro;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Contiene los metodos exclusivos para el permiso de verificador ademas de el ingreso de datos.
 * @author Diego
 */
@Named
@Stateless
public class FacadeVerificador {
    @Inject
    private ControladorRegistro cReg;
    @Inject
    private ControladorEdicionRegistro cEdicion;
    
    //  Constructores
    public FacadeVerificador(){}
    
    /**
     * Setea la comprobacion de implementacion de la accion, cambia el estado segun corresponda y actualiza la base de datos.
     * @param FechaComprobacionImplementacion
     * @param ComentariosImplementacion
     * @param Comprobacion
     * @param IdAccion
     * @return -1 si no se actualizo
     */
    public int SetComprobacionImplementacionAccion(Date FechaComprobacionImplementacion, String ComentariosImplementacion, ResultadoComprobacion Comprobacion,
            int IdAccion){
        return cReg.ComprobarImplementacionAccion(FechaComprobacionImplementacion, ComentariosImplementacion, Comprobacion, IdAccion);
    }
    
    /**
     * Setea la verificacion de eficacia de la accion, deja en estado cerrado y actualiza la base de datos.
     * @param FechaVerificacionEficacia
     * @param ComentariosVerificacion
     * @param Comprobacion
     * @param IdAccion
     * @return -1 si no se actualizo
     */
    public int SetVerificacionEficaciaAccion(Date FechaVerificacionEficacia, String ComentariosVerificacion, ResultadoComprobacion Comprobacion,
            int IdAccion){
        return cReg.VerificarEficaciaAccion(FechaVerificacionEficacia, ComentariosVerificacion, Comprobacion, IdAccion);
    }
    
    /**
     * Setea el estado de la accion como desestimada y actualiza la base de datos.
     * @param Observaciones
     * @param IdAccion
     * @return
     */
    public int DesestimarAccion(String Observaciones, int IdAccion){
        return cEdicion.DesestimarAccion(Observaciones, IdAccion);
    }
        
}
