/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.facades;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.controlador.registro.ControladorVistaRegistros;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.modelo.usuario.Usuario;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Diego
 */
public class FacadeLectura implements Serializable {
 
    private final ControladorVistaRegistros cVista;
    
    public FacadeLectura(){
        cVista = new ControladorVistaRegistros();
    }
    
    public Accion GetAccion(int IdAccion){
        return cVista.getAccion(IdAccion);
    }
    
    public Usuario GetUsuario(int IdUsuario){
        return cVista.getUsuario(IdUsuario);
    }
    
    /**
     * Devuelve los usuarios de la empresa especificada segun su fecha de baja.
     * @param Vigente True: si no fueron dados de baja (FechaBaja == null).
     * @return Lista de Usuarios.
     */
    public List<Usuario> listarUsuarios(boolean Vigente){
        return cVista.listarUsuarios(Vigente);
    }
    
    public List<Deteccion> listarDetecciones(){
        return cVista.listarDetecciones();
    }
    
    /**
     * Devuelve las codificaciones.
     * @return Lista de codificaciones.
     */
    public List<Codificacion> listarCodificaciones(){
        return cVista.listarCodificaciones();
    }
    
    /**
     * Devuelve una todas las areas de una empresa.
     * @return lista de areas.
     */
    public List<Area> listarAreas(){
        return cVista.listarAreas();
    }

    public List<Accion> listarAccionesCorrectivas(){
        return cVista.listarAcciones(null, TipoAccion.CORRECTIVA);
    }
    public List<Accion> listarAccionesMejoras(){
        return cVista.listarAcciones(null, TipoAccion.MEJORA);
    }
    
    public List<Accion> listarAccionesPreventivas(){
        return cVista.listarAcciones(null, TipoAccion.PREVENTIVA);
    }
    
    public List<Accion> listarAcciones(){
        return cVista.listarAcciones(null);
    }
    
    public Fortaleza getFortaleza(int IdFortaleza){
        return cVista.getFortaleza(IdFortaleza);
    }
    
    /**
     * Lista todas las fortalezas registradas
     * @return
     */
    public List<Fortaleza> listarFortalezas(){
        return cVista.listarFortalezas();
    }
    
    /*
        Responsabilidades
    */
    
    public List<Responsabilidad> listarResponsabilidades(){
        return cVista.listarResponsabilidades();
    } 
    
    /*
        Responsables
    */
    
    public List<Responsable> listarResponsables(boolean soloVigentes){
        return cVista.listarResponsables(soloVigentes);
    }
}
