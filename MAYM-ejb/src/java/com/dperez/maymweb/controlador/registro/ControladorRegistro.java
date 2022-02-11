/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.controlador.registro;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.comprobaciones.ResultadoComprobacion;
import com.dperez.maymweb.modelo.acciones.Correctiva;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.acciones.Mejora;
import com.dperez.maymweb.modelo.acciones.Preventiva;
import com.dperez.maymweb.modelo.acciones.adjunto.Adjunto;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.acciones.actividad.TipoActividad;
import com.dperez.maymweb.modelo.acciones.adjunto.TipoAdjunto;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.producto.Producto;
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.persistencia.FabricaRepositorio;
import com.dperez.maymweb.persistencia.RepositorioPersistencia;
import java.util.Date;

/**
 *
 * @author Diego
 */
public class ControladorRegistro {
    
    private final RepositorioPersistencia<Accion> repoAccion;
    private final RepositorioPersistencia<Area> repoArea;
    private final RepositorioPersistencia<Deteccion> repoDeteccion;
    private final RepositorioPersistencia<Responsable> repoResponsable;
    private final RepositorioPersistencia<Fortaleza> repoFortalezas;
    
    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    
    //  Constructores
    public ControladorRegistro(){
        repoAccion = fabricaRepositorio.getRespositorioAcciones();
        repoArea = fabricaRepositorio.getRepositorioAreas();
        repoDeteccion = fabricaRepositorio.getRepositorioDetecciones();
        repoResponsable = fabricaRepositorio.getRepositorioResponsables();
        repoFortalezas = fabricaRepositorio.getRepositorioFortalezas();
    }
    
    /**
     * Crea una nueva accion en estado pendiente y la persiste en la base de datos.Se codifica automaticamente la accion como 'Sin Codificar'.Si la codificacon no existe se crea.
     * @param tipo
     * @param fechaDeteccion
     * @param descripcion
     * @param idArea
     * @param idDeteccion
     * @return Null: si no se creo.
     */
    public Accion nuevaAccion(TipoAccion tipo, Date fechaDeteccion, String descripcion, int idArea, int idDeteccion){
        Area area = repoArea.find(idArea);
        Deteccion deteccion = repoDeteccion.find(idDeteccion);
        
        Accion accion;
        switch(tipo){
            case CORRECTIVA -> accion = new Correctiva(fechaDeteccion , descripcion, area, deteccion);
            
            case MEJORA ->accion = new Mejora(fechaDeteccion , descripcion, area, deteccion);
            
            default -> accion = new Preventiva(fechaDeteccion , descripcion, area, deteccion);
        }
        
        return repoAccion.create(accion);
    }
    
    /**
     * Crea el/los productos involucrados en el desvio, los agrega a la accion correctiva y actualiza la base de datos.
     * @param idAccion
     * @param nombreProducto
     * @param datosProducto
     * @return -1 si no se creo.
     */
    public int agregarProductoInvolucrado(int idAccion, String nombreProducto, String datosProducto){
        Accion accion =  repoAccion.find(idAccion);
        Producto producto = accion.crearProducto(nombreProducto, datosProducto);
        repoAccion.update(accion);
        return producto.getId();
    }
    
    /**
     * Crea el/los adjuntos, los agrega a la accion y actualiza la base de datos.
     * @param idAccion
     * @param tituloAdjunto
     * @param ubicacionAdjunto
     * @param tipo
     * @return -1 si no se creo.
     */
    public int agregarArchivoAdjunto(int idAccion, String tituloAdjunto, String ubicacionAdjunto, TipoAdjunto tipo){
        Accion accion = repoAccion.find(idAccion);
        Adjunto adjunto = accion.crearAdjunto(tituloAdjunto, ubicacionAdjunto, tipo);
        repoAccion.update(accion);
        return adjunto.getIda();
    }
    
    /**
     * Crea una nueva actividad de mejora, la persiste en la base de datos y se asocia a la mejora.
     * @param idAccion
     * @param fechaEstimadaImplementacion
     * @param descripcion
     * @param idResponsable
     * @param tipoActividad
     * @return null si no se creo.
     */
    public Actividad agregarActividad(int idAccion, Date fechaEstimadaImplementacion, String descripcion, int idResponsable, TipoActividad tipoActividad){
        Accion accion = repoAccion.find(idAccion);
        Responsable responsable = repoResponsable.find(idResponsable);
        Actividad actividad =  accion.crearActividad(fechaEstimadaImplementacion, descripcion, responsable, tipoActividad);
        accion.cambiarEstado();
        repoAccion.update(accion);
        if (actividad.getId() > 0){
            return actividad;
        }else{
            return null;
        }
    }
    
    /**
     * Setea la fecha de implementacion de la Actividad correctiva, cambia el estado de la accion (si corresponde) y persiste los cambios en la base de deatos.
     * @param fechaImplementacion
     * @param idActividad
     * @param idAccion
     * @return -1 si no se pudo actualizar.
     */
    public int setFechaImplementacionActividad(Date fechaImplementacion, int idActividad, int idAccion){
        Accion accion = repoAccion.find(idAccion);
        accion.setFechaImplementacionActividad(idActividad, fechaImplementacion);
        accion.cambiarEstado();
        repoAccion.update(accion);
        return 1;
    }
    
    /**
     * Setea la comprobacion de implementacion estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param fechaEstimada
     * @param idResponsable
     * @param idAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int setComprobacionImplementacion(Date fechaEstimada, int idResponsable, int idAccion){
        Responsable responsable = repoResponsable.find(idResponsable);
        Accion accion = repoAccion.find(idAccion);
        accion.crearComprobacionImplementacion(fechaEstimada, responsable);
        repoAccion.update(accion);
        return 1;
        // TODO: no crear si ya existe.
    }
    
    /**
     * Setea la comprobacion de eficacia estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param fechaEstimada
     * @param idResponsable
     * @param idAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int setComprobacionEficacia(Date fechaEstimada, int idResponsable, int idAccion){
        Responsable responsable = repoResponsable.find(idResponsable);
        Accion accion = repoAccion.find(idAccion);
        accion.crearComprobacionEficacia(fechaEstimada, responsable);
        repoAccion.update(accion);
        return 1;
        // TODO: no crear si ya existe.
    }
    
    /**
     * Setea la comprobacion de implementacion de la accion, cambia el estado segun corresponda y actualiza la base de datos.
     * PRE: la accion debe tener comprobacion != null (seteada fecha estimada de comprobacion de implementacion)
     * @param fecha
     * @param observaciones
     * @param resultado
     * @param idAccion
     * @return -1 si no se actualizo
     */
    public int comprobarImplementacion(Date fecha, String observaciones, ResultadoComprobacion resultado, int idAccion){
        Accion accion = repoAccion.find(idAccion);
        accion.getComprobacionImplementacion().setFechaComprobacion(fecha);
        accion.getComprobacionImplementacion().setObservaciones(observaciones);
        accion.getComprobacionImplementacion().setResultadoComprobacion(resultado);
        accion.cambiarEstado();
        repoAccion.update(accion);
        return 1;
    }
    
    /**
     * Setea la verificacion de eficacia de la accion, deja en estado cerrado y actualiza la base de datos.
     * @param fecha
     * @param observaciones
     * @param resultado
     * @param idAccion
     * @return -1 si no se actualizo
     */
    public int verificarEficacia(Date fecha, String observaciones, ResultadoComprobacion resultado, int idAccion){
        Accion accion = repoAccion.find(idAccion);
        accion.getComprobacionEficacia().setFechaComprobacion(fecha);
        accion.getComprobacionEficacia().setObservaciones(observaciones);
        accion.getComprobacionEficacia().setResultadoComprobacion(resultado);
        accion.cambiarEstado();
        repoAccion.update(accion);
        return 1;
    }
    
    /**
     * Crea una nueva fortaleza y la persiste en la base de datos
     * @param fechaDeteccion
     * @param descripcion
     * @param idDeteccion
     * @param idAreaSector
     * @return Null: si no se creo.
     */
    public Fortaleza nuevaFortaleza(Date fechaDeteccion, String descripcion, int idDeteccion, int idAreaSector){
        Area area = repoArea.find(idAreaSector);
        Deteccion deteccion = repoDeteccion.find(idDeteccion);
        Fortaleza fortaleza = new Fortaleza(fechaDeteccion, descripcion, area, deteccion);
        
        repoFortalezas.create(fortaleza);
        if(fortaleza.getId() > 0)
            return fortaleza;
        return null;
    }
    
}
