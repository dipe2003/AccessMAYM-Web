/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.controlador.registro;

import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.acciones.ManejadorAccion;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.area.ManejadorArea;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.codificacion.ManejadorCodificacion;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.deteccion.ManejadorDeteccion;
import com.dperez.maymweb.empresa.Empresa;
import com.dperez.maymweb.empresa.ManejadorEmpresa;
import com.dperez.maymweb.acciones.EnumEstado;
import com.dperez.maymweb.fortaleza.Fortaleza;
import com.dperez.maymweb.fortaleza.ManejadorFortaleza;
import com.dperez.maymweb.usuario.ManejadorUsuario;
import com.dperez.maymweb.usuario.Usuario;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Diego
 */
@Named
@Stateless
public class ControladorVistaRegistros {
    @Inject
    private ManejadorAccion mAccion;
    @Inject
    private ManejadorUsuario mUsuario;
    @Inject
    private ManejadorEmpresa mEmpresa;
    @Inject
    private ManejadorDeteccion mDeteccion;
    @Inject
    private ManejadorCodificacion mCodificaciones;
    @Inject
    private ManejadorArea mArea;
    @Inject
    private ManejadorFortaleza mFortaleza;
    
    //  Constructores
    public ControladorVistaRegistros(){}
    
    /**
     * Devuelve una lista con todas las acciones registradas que se encuentran en el estado especificado.
     * Si EstadoAccion es null se devuelven todas las acciones registradas.
     * @param EstadoAccion
     * @return
     */
    public List<Accion> ListarAccionesSegunEstado(EnumEstado EstadoAccion){
        List<Accion> acciones;
        acciones = mAccion.ListarAcciones();
        if(EstadoAccion!=null){
            acciones = acciones.stream()
                    .filter(accion -> accion.getEstadoAccion() == EstadoAccion)
                    .collect(Collectors.toList());
        }
        return acciones;
    }
    
    /**
     * Devuelve una lista con todas las acciones del tipo especificado registradas que se encuentran en el estado indicado.
     * Si EstadoAccion es null se devuelven todas las acciones registradas del tipo indicado.
     * Si EstadoAccion es null y TipoAccion es null se devuelven todas las accines registradas.
     * @param EstadoAccion
     * @param TipoAccion
     * @return
     */
    public List<Accion> ListarAccionesSegunEstado(EnumEstado EstadoAccion, TipoAccion TipoAccion){
        List<Accion> acciones;
        acciones = mAccion.ListarAcciones();
        if(EstadoAccion!=null && TipoAccion !=null){
            acciones = acciones.stream()
                    .filter(accion -> accion.getEstadoAccion() == EstadoAccion && accion.getClass().getName().equals(TipoAccion.toString()))
                    .collect(Collectors.toList());
        }else{
            if(EstadoAccion == null && TipoAccion != null){
                acciones = acciones.stream()
                        .filter(accion -> accion.getClass().getSimpleName().equalsIgnoreCase(TipoAccion.toString()))
                        .collect(Collectors.toList());
            }
        }
        return acciones;
    }
    
    
    /**
     * Devuelve la accion indicada por su id
     * @param IdAccion
     * @return
     */
    public Accion GetAccion(int IdAccion){
        return mAccion.GetAccion(IdAccion);
    }
    
    /**
     * Devuelve el usuario especificado por su id
     * @param IdUsuario
     * @return Null si no existe el usuario.
     */
    public Usuario GetUsuario(int IdUsuario){
        return mUsuario.GetUsuario(IdUsuario);
    }
    
    /**
     * Devuelve los usuarios de la empresa especificada segun su fecha de baja.
     * @param SoloVigente True: si no fueron dados de baja (FechaBaja == null).
     * @param IdEmpresa -1 para todas las empresas.
     * @return Lista de Usuarios.
     */
    public List<Usuario> GetUsuarios(boolean SoloVigente){
        List<Usuario> lstUsuarios = mUsuario.ListarUsuarios();
        Iterator it = lstUsuarios.iterator();
        while(it.hasNext()){
            Usuario usr = (Usuario) it.next();
            if(SoloVigente){
                if(usr.getFechaBaja() != null){
                    it.remove();
                }
            }
        }
        return lstUsuarios;
    }
    
    
    /**
     * Devuelve una lista de detecciones.
     * @return
     */
    public List<Deteccion> GetDetecciones(){
        return mDeteccion.ListarDetecciones();
    }
    
    /**
     * Devuelve las codificaciones.
     * @param IdEmpresa -1 para todas las empresas
     * @return Lista de codificaciones.
     */
    public List<Codificacion> GetCodificaciones(){
        return mCodificaciones.ListarCodificaciones();
    }
    
    /**
     * Devuelve una todas las areas de una empresa.
     * @param IdEmpresa -1 para todas las empresas
     * @return lista de areas.
     */
    public List<Area> GetAreas(){
        return mArea.ListarAreas();
    }
    
    /**
     * Lista todos los usuarios de la empresa seleccionada por id.
     * @param IdEmpresa
     * @return
     */
    public List<Usuario> ListarUsuarios(){
        return mUsuario.ListarUsuarios();
    }
    
    /*
        TODO descomentar luego de implementar Manejador
    */
    
    /**
     * Lista todas las empresas registradas.
     * @return
     */
    
    public List<Empresa> ListarEmpresasRegistradas(){
        //return mEmpresa.ListarEmpresasRegistradas();
        throw new NotImplementedException();
    }
    
    /**
     * Devuelve la empresa indicada por su id.
     * @param IdEmpresa
     * @return Retorne Null si no se encontro empresa.
     */
    public Empresa GetEmpresa(int IdEmpresa){
       // return mEmpresa.GetEmpresa(IdEmpresa);
       throw new NotImplementedException();
    }
  
    /**
     * Devuelve la fortaleza indicada por su id
     * @param IdFortaleza
     * @return Retorna null si no se encontro fortaleza.
     */
    public Fortaleza GetFortaleza(int IdFortaleza){
        return mFortaleza.GetFortaleza(IdFortaleza);
    }
    
    /**
     * Lista todas las fortalezas registradas
     * @param IdEmpresa -1 para todas las empresas
     * @return
     */
    public List<Fortaleza> ListarFortalezas(){
        return mFortaleza.ListarFortalezas();
    }
    
}
