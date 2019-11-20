/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.facades;

import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.controlador.registro.ControladorVistaRegistros;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.fortaleza.Fortaleza;
import com.dperez.maymweb.usuario.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Diego
 */
@Named
@Stateless
public class FacadeLectura  {
    
    @Inject
    private ControladorVistaRegistros cVista;
    
    public Accion GetAccion(int IdAccion){
        return cVista.GetAccion(IdAccion);
    }
    
    public Usuario GetUsuario(int IdUsuario){
        return cVista.GetUsuario(IdUsuario);
    }
    
    /**
     * Devuelve los usuarios de la empresa especificada segun su fecha de baja.
     * @param Vigente True: si no fueron dados de baja (FechaBaja == null).
     * @param IdEmpresa -1 para todas las empresas.
     * @return Lista de Usuarios.
     */
    public List<Usuario> GetUsuarios(boolean Vigente){
        return cVista.GetUsuarios(Vigente);
    }
    
    public List<Empresa> ListaEmpresasRegistradas(){
        return cVista.ListarEmpresasRegistradas();
    }
    
    public Empresa GetEmpresa(int IdEmpresa){
        return cVista.GetEmpresa(IdEmpresa);
    }
    
    public List<Deteccion> ListarDetecciones(){
        return cVista.GetDetecciones();
    }
    
    /**
     * Devuelve las codificaciones.
     * @param IdEmpresa -1 para todas las empresas
     * @return Lista de codificaciones.
     */
    public List<Codificacion> ListarCodificaciones(){
        return cVista.GetCodificaciones();
    }
    
    /**
     * Devuelve una todas las areas de una empresa.
     * @param IdEmpresa -1 para todas las empresas
     * @return lista de areas.
     */
    public List<Area> ListarAreasSectores(){
        return cVista.GetAreas();
    }

    public List<Accion> ListarAccionesCorrectivas(){
        return cVista.ListarAccionesSegunEstado(null, TipoAccion.CORRECTIVA);
    }
    public List<Accion> ListarAccionesMejoras(){
        return cVista.ListarAccionesSegunEstado(null, TipoAccion.MEJORA);
    }
    
    public List<Accion> ListarAccionesPreventivas(){
        return cVista.ListarAccionesSegunEstado(null, TipoAccion.PREVENTIVA);
    }
    
    public List<Accion> ListarAcciones(){
        return cVista.ListarAccionesSegunEstado(null);
    }
    
    public Fortaleza GetFotaleza(int IdFortaleza){
        return cVista.GetFortaleza(IdFortaleza);
    }
    
    /**
     * Lista todas las fortalezas registradas
     * @param IdEmpresa -1 para todas las empresas
     * @return
     */
    public List<Fortaleza> ListarFortalezas(){
        return cVista.ListarFortalezas();
    }
}
