/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.controlador.registro;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.acciones.actividad.TipoActividad;
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
public class ControladorEdicionRegistro {
    
    private final RepositorioPersistencia<Accion> repoAccion;
    private final RepositorioPersistencia<Area> repoArea;
    private final RepositorioPersistencia<Deteccion> repoDeteccion;
    private final RepositorioPersistencia<Codificacion> repoCodificacion;
    private final RepositorioPersistencia<Responsable> repoResponsable;
    private final RepositorioPersistencia<Fortaleza> repoFortalezas;
    
    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    
    //  Constructores
    public ControladorEdicionRegistro(){
        repoAccion = fabricaRepositorio.getRespositorioAcciones();
        repoArea = fabricaRepositorio.getRepositorioAreas();
        repoDeteccion = fabricaRepositorio.getRepositorioDetecciones();
        repoCodificacion = fabricaRepositorio.getRepositorioCodificaciones();
        repoResponsable = fabricaRepositorio.getRepositorioResponsables();
        repoFortalezas = fabricaRepositorio.getRepositorioFortalezas();
    }
    
    /*
    ACCION
    */
    
    /**
     * Edita una accion con los mismos parametros que se creo.Actualiza la base de datos.
     * @param idAccion
     * @param fechaDeteccion
     * @param descripcion
     * @param analisisCausa
     * @param idAreaSector
     * @param idDeteccion
     * @param idCodificacion
     * @return -1 si no se actualizo.
     */
    public int editarAccion(int idAccion, Date fechaDeteccion, String descripcion, String referencias, String analisisCausa,
            int idAreaSector, int idDeteccion, int idCodificacion){
        Accion accion = repoAccion.find(idAccion);
        //  Comprobar si hay cambio en el valor para "ahorrar" llamada a la base de datos.
        if(accion.getAreaAccion().getId()!=idAreaSector){
            Area areaSector = repoArea.find(idAreaSector);
            accion.setAreaAccion(areaSector);
        }
        if(accion.getDeteccionAccion().getId()!=idDeteccion){
            Deteccion deteccion = repoDeteccion.find(idDeteccion);
            accion.setDeteccionAccion(deteccion);
        }
        if(accion.getCodificacionAccion() == null || accion.getCodificacionAccion()!=null &&  accion.getCodificacionAccion().getId()!=idCodificacion){
            Codificacion codificacion = repoCodificacion.find(idCodificacion);
            accion.setCodificacionAccion(codificacion);
        }
        accion.setFechaDeteccion(fechaDeteccion);
        accion.setDescripcion(descripcion);
        accion.setReferencias(referencias);
        accion.setAnalisisCausa(analisisCausa);
        return repoAccion.update(accion).getId();
    }
    
    /**
     * Actualiza Analisis de Causa
     * @param idAccion
     * @param analisisCausa
     * @return 
     */
     public int guardarAnalisisCausa(int idAccion, String analisisCausa){
        Accion accion = repoAccion.find(idAccion);
        accion.setAnalisisCausa(analisisCausa);
        return repoAccion.update(accion).getId();
    }
    
    /**
     * Elimina la accion seleccionada.
     * @param idAccion
     * @return return Retorna -1 si no se elimino. Retorna el id de la accion eliminada.
     */
    public int eliminarAccion(int idAccion){
        Accion accion = repoAccion.find(idAccion);
        repoAccion.delete(accion);
        return 1;
    }
    
    /**
     * Setea el analisis de causa de la desviacion y actualiza la base de datos.
     * @param analisisDeCausa
     * @param idAccion
     * @return -1 si no se actualizo.
     */
    public int setAnalisisDeCausa(String analisisDeCausa, int idAccion){
        Accion accion = repoAccion.find(idAccion);
        accion.setAnalisisCausa(analisisDeCausa);
        return repoAccion.update(accion).getId();
    }
    
    /**
     * Setea el estado de la accion como desestimada y actualiza la base de datos.
     * @param observaciones
     * @param idAccion
     * @return
     */
    public int desestimarAccion(String observaciones, int idAccion){
        Accion accion = repoAccion.find(idAccion);
        accion.setEstadoDeAccion(Estado.DESESTIMADA);
        accion.setObservacionesDesestimada(observaciones);
        return repoAccion.update(accion).getId();
    }
    
    /**
     * Setea el estado de la acción como debería estar según sus últimos datos de seguimiento cargados.
     * Se eliminan las observaciones de desestimada.
     * @param idAccion
     * @return 
     */
    public int restaurarAccion(int idAccion){
        Accion accion = repoAccion.find(idAccion);
        accion.setEstadoDeAccion(Estado.PENDIENTE);
        accion.setObservacionesDesestimada("");
        accion.cambiarEstado();
        return repoAccion.update(accion).getId();
    }
    
    /**
     * Remueve el producto involucrado de la accion seleccionada y actualiza la base de datos.
     * Elimina el producto de la base de datos.
     * @param idAccion
     * @param idProducto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int removerProductoInvolucrado(int idAccion, int idProducto){
        Accion accion = repoAccion.find(idAccion);
        accion.removeProducto(idProducto);
        repoAccion.update(accion);
        return 1;
    }
    
    /**
     * Remueve el archivo adjunto de la accion seleccionada y actualiza la base de datos.
     * Elimina el adjunto de la base de datos.
     * @param idAccion
     * @param idAdjunto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int removerArchivoAdjunto(int idAccion, int idAdjunto){
        Accion accion = repoAccion.find(idAccion);
        accion.removeAdjunto(idAdjunto);
        repoAccion.update(accion);
        return 1;
    }
    
    /**
     * Remueve la actividad de la accion seleccionada y actualiza la base de datos.
     * Elimina la actividad de la base de datos.
     * @param idAccion
     * @param idActividad
     * @return Retorna -1 si se actualizo. Retorna el IdAccion si no se elimino.
     */
    public int removerActividad(int idAccion, int idActividad){
        Accion accion = repoAccion.find(idAccion);
        Actividad actividad = accion.findActividad(idActividad);
        accion.removeActividad(actividad);
        
        accion.cambiarEstado();
        repoAccion.update(accion);
        return 1;
    }
    
    /**
     * Actualiza la actividad en la base de datos.
     * @param idAccion
     * @param idActividad
     * @param idResponsable
     * @param fechaEstimada
     * @param descripcion
     * @param nuevoTipoActividad
     * @return Retorna -1 si no se actualizo. Retorna el IdActividad si se actualizo.
     */
    public int editarActividad(int idAccion, int idActividad, int idResponsable, Date fechaEstimada, String descripcion, TipoActividad nuevoTipoActividad){
        Accion accion = repoAccion.find(idAccion);
        Actividad actividad = accion.findActividad(idActividad);
        actividad.setDescripcion(descripcion);
        actividad.setFechaEstimadaImplementacion(fechaEstimada);
        actividad.setTipoDeActividad(nuevoTipoActividad);
        if(actividad.getResponsableImplementacion().getId() != idResponsable){
            Responsable responsable = repoResponsable.find(idResponsable);
            actividad.setResponsableImplementacion(responsable);
        }
        accion.cambiarEstado();
        return repoAccion.update(accion).getId();
    }
    
    /*
    PRODUCTO
    */
    
    /**
     * Cambia los valores de Producto y actualiza la base de datos.
     * @param idAccion
     * @param idProducto
     * @param nombreProducto
     * @param datosProducto
     * @return Retorna -1 si no se actualizo. Retorna el IdProducto si se actualizo.
     */
    public int editarProducto(int idAccion, int idProducto, String nombreProducto, String datosProducto){
        Accion accion = repoAccion.find(idAccion);
        Producto producto = accion.findProducto(idProducto);
        producto.setDatos(datosProducto);
        producto.setNombre(nombreProducto);
        return repoAccion.update(accion).getId();
    }
    
    /*
    FORTALEZA
    */
    
    /**
     * Actualiza la fortaleza especificada por su id en la base de datos.
     * @param idFortaleza
     * @param fechaDeteccion
     * @param descripcion
     * @param idDeteccion
     * @param idAreaSector
     * @return Retorna el id de la fortaleza si se actualizo, de lo contrario retorna -1.
     */
    public int editarFortaleza(int idFortaleza, Date fechaDeteccion, String descripcion, int idDeteccion, int idAreaSector){
        Fortaleza fortaleza = repoFortalezas.find(idFortaleza);
        if(fortaleza.getAreaFortaleza().getId() != idAreaSector){
            Area area = repoArea.find(idAreaSector);
            fortaleza.setAreaFortaleza(area);
        }
        if(fortaleza.getDeteccionFortaleza().getId() != idDeteccion){
            Deteccion deteccion = repoDeteccion.find(idDeteccion);
            fortaleza.setDeteccionFortaleza(deteccion);
        }
        fortaleza.setDescripcion(descripcion);
        fortaleza.setFechaDeteccion(fechaDeteccion);
        return repoFortalezas.update(fortaleza).getId();
    }
    
    /**
     * Se elimina la fortaleza indicada de la base de datos.
     * @param idFortaleza
     * @return Retorna el id de la fortaleza si se elimino, de lo contrario retorna -1.
     */
    public int eliminarFortaleza(int idFortaleza){
        Fortaleza fortaleza = repoFortalezas.find(idFortaleza);
        repoFortalezas.delete(fortaleza);
        return 1;
    }
}
