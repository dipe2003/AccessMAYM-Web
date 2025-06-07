/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.facades;

import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.acciones.actividad.TipoActividad;
import com.dperez.maymweb.modelo.acciones.adjunto.TipoAdjunto;
import com.dperez.maymweb.controlador.registro.ControladorEdicionRegistro;
import com.dperez.maymweb.controlador.registro.ControladorRegistro;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import java.util.Date;

/**
 *
 * @author Diego
 */
public class FacadeDatos {

    private final ControladorRegistro cReg;
    private final ControladorEdicionRegistro cEdicion;
    
    //  Constructores
    public FacadeDatos(){
        cReg = new ControladorRegistro();
        cEdicion = new ControladorEdicionRegistro();
    }
    
    /**
     * Crea una nueva accion en estado pendiente y la persiste en la base de datos.
     * @param tipoAccion
     * @param fechaDeteccion
     * @param descripcion
     * @param referencias
     * @param idAreaSector
     * @param idDeteccion
     * @param idCodificacion
     * @param idEmpresa
     * @return Null: si no se creo.
     */
    public Accion nuevaAccion(TipoAccion tipoAccion, Date fechaDeteccion, String descripcion,String referencias,
            int idAreaSector, int idDeteccion, int idCodificacion, int idEmpresa){
        return cReg.nuevaAccion(tipoAccion, fechaDeteccion, descripcion, referencias, idAreaSector, idDeteccion, idCodificacion, idEmpresa);
    }
    
    /**
     * Crea el/los productos involucrados en el desvio, los agrega a la accion correctiva y actualiza la base de datos.
     * @param idAccion
     * @param nombreProducto
     * @param datosProducto
     * @return -1 si no se creo.
     */
    public int agregarProductoInvolucrado(int idAccion, String nombreProducto, String datosProducto){
        return cReg.agregarProductoInvolucrado(idAccion, nombreProducto, datosProducto);
    }
    
    /**
     * Crea el/los adjuntos, los agrega a la accion y actualiza la base de datos.
     * @param idAccion
     * @param tituloAdjunto
     * @param ubicacionAdjunto
     * @param tipoAdjunto
     * @return -1 si no se creo.
     */
    public int agregarArchivoAdjunto(int idAccion, String tituloAdjunto, String ubicacionAdjunto, TipoAdjunto tipoAdjunto){
        return cReg.agregarArchivoAdjunto(idAccion, tituloAdjunto, ubicacionAdjunto, tipoAdjunto);
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
        return cReg.agregarActividad(idAccion, fechaEstimadaImplementacion, descripcion, idResponsable, tipoActividad);
    }
    
    /**
     * Setea la fecha de implementacion de la Medida correctiva, cambia el estado de la accion (si corresponde) y persiste los cambios en la base de deatos.
     * @param fechaImplementacion
     * @param idActividad
     * @param idAccion
     * @return -1 si no se pudo actualizar.
     */
    public int setFechaImplementacionActividad(Date fechaImplementacion, int idActividad, int idAccion){
        return cReg.setFechaImplementacionActividad(fechaImplementacion, idActividad, idAccion);
    }
    
    /**
     * Setea la comprobacion de implementacion estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param fechaEstimada
     * @param idUsuarioResponsable
     * @param idAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int setComprobacionImplementacion(Date fechaEstimada, int idUsuarioResponsable, int idAccion){
        return cReg.setComprobacionImplementacion(fechaEstimada, idUsuarioResponsable, idAccion);
    }
    
    /**
     * Setea la comprobacion de eficacia estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param fechaEstimada
     * @param idUsuarioResponsable
     * @param idAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int setVerificacionEficacia(Date fechaEstimada, int idUsuarioResponsable, int idAccion){
        return cReg.setComprobacionEficacia(fechaEstimada, idUsuarioResponsable, idAccion);
    }
    
    /**
     * Remueve la medida correctiva de la accion correctiva seleccionada y actualiza la base de datos.
     * Elimina la medida correctiva de la base de datos.
     * @param idAccion
     * @param idActividad
     * @return Retorna -1 si se actualizo. Retorna el IdAccion si no se elimino.
     */
    public int removerActividad(int idAccion, int idActividad){
        return cEdicion.removerActividad(idAccion, idActividad);
    }
        
    /**
     * Remueve el archivo adjunto de la accion seleccionada y actualiza la base de datos.
     * Elimina el adjunto de la base de datos.
     * @param idAccion
     * @param idAdjunto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int removerAdjunto(int idAccion, int idAdjunto){
        return cEdicion.removerArchivoAdjunto(idAccion, idAdjunto);
    }
    /**
     * Edita una accion con los mismos parametros que se creo.Actualiza la base de datos.
     * @param idAccion
     * @param fechaDeteccion
     * @param descripcion
     * @param referencias
     * @param analisisCausa
     * @param idArea
     * @param idDeteccion
     * @param idCodificacion
     * @return -1 si no se actualizo.
     */
    public int editarAccion(int idAccion, Date fechaDeteccion, String descripcion, String referencias, String analisisCausa, int idArea, 
            int idDeteccion, int idCodificacion){
        return cEdicion.editarAccion(idAccion, fechaDeteccion, descripcion, referencias, analisisCausa, idArea, idDeteccion, idCodificacion);
    }
    
    /**
     * Actualiza la actividad en la base de datos.
     * @param idAccion
     * @param idActividad
     * @param descripcion
     * @param idResponsable
     * @param fechaImplementacion
     * @param nuevoTipoActividad
     * @return Retorna -1 si no se actualizo. Retorna el IdActividad si se actualizo.
     */
    public int editarActividad(int idAccion, int idActividad, String descripcion, int idResponsable, Date fechaImplementacion, TipoActividad nuevoTipoActividad){
        return cEdicion.editarActividad(idAccion, idActividad, idResponsable, fechaImplementacion,descripcion, nuevoTipoActividad);
    }
    
    /**
     * Remueve el producto involucrado de la accion seleccionada y actualiza la base de datos.Elimina el producto de la base de datos.
     * @param idAccion
     * @param idProducto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int removerProductoInvolucrado(int idAccion, int idProducto){
        return cEdicion.removerProductoInvolucrado(idAccion, idProducto);
    }
    
    /**
     * Crea una nueva fortaleza y la persiste en la base de datos
     * @param fechaDeteccion
     * @param descripcion
     * @param idDeteccion
     * @param idAreaSector
     * @param idEmpresa
     * @return Null: si no se creo.
     */
    public Fortaleza nuevaFortaleza(Date fechaDeteccion, String descripcion, int idDeteccion, int idAreaSector, int idEmpresa){
        return cReg.nuevaFortaleza(fechaDeteccion, descripcion, idDeteccion, idAreaSector, idEmpresa);
    }
    
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
        return cEdicion.editarFortaleza(idFortaleza, fechaDeteccion, descripcion, idDeteccion, idAreaSector);
    }
    
    
}
