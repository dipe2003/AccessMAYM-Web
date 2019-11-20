/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.controlador.registro;

import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.acciones.ManejadorAccion;
import com.dperez.maymweb.acciones.Correctiva;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.acciones.TipoDesvio;
import com.dperez.maymweb.acciones.Mejora;
import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.accion.adjunto.Adjunto;
import com.dperez.maymweb.accion.adjunto.ManejadorAdjunto;
import com.dperez.maymweb.accion.actividad.ManejadorActividad;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.area.ManejadorArea;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.codificacion.ManejadorCodificacion;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.deteccion.ManejadorDeteccion;
import com.dperez.maymweb.estado.EnumEstado;
import com.dperez.maymweb.fortaleza.Fortaleza;
import com.dperez.maymweb.fortaleza.ManejadorFortaleza;
import com.dperez.maymweb.producto.ManejadorProducto;
import com.dperez.maymweb.producto.Producto;
import com.dperez.maymweb.usuario.ManejadorUsuario;
import com.dperez.maymweb.usuario.Responsable;
import java.util.Date;
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
public class ControladorEdicionRegistro {
    @Inject
    private ManejadorAccion mAccion;
    @Inject
    private ManejadorArea mArea;
    @Inject
    private ManejadorDeteccion mDeteccion;
    @Inject
    private ManejadorCodificacion mCodificacion;
    @Inject
    private ManejadorProducto mProducto;
    @Inject
    private ManejadorAdjunto mAdjunto;
    @Inject
    private ManejadorActividad mActividad;
    @Inject
    private ManejadorUsuario mUsuario;
    @Inject
    private ManejadorFortaleza mFortaleza;
    
    //  Constructores
    public ControladorEdicionRegistro(){}
    
    /*
    ACCION
    */
    
    /**
     * Edita una accion con los mismos parametros que se creo. Actualiza la base de datos.
     * @param IdAccion
     * @param TipoAccion
     * @param FechaDeteccion
     * @param Descripcion
     * @param AnalisisCausa
     * @param TipoDesvio
     * @param IdAreaSector
     * @param IdDeteccion
     * @param IdCodificacion
     * @return -1 si no se actualizo.
     */
    public int EditarAccion(int IdAccion, TipoAccion TipoAccion, Date FechaDeteccion, String Descripcion, String AnalisisCausa, TipoDesvio TipoDesvio,
            int IdAreaSector, int IdDeteccion, int IdCodificacion){
        Accion accion = mAccion.GetAccion(IdAccion);
        //  Comprobar si hay cambio en el valor para "ahorrar" llamada a la base de datos.
        if(accion.getAreaSectorAccion().getId()!=IdAreaSector){
            Area areaSector = mArea.GetArea(IdAreaSector);
            accion.setAreaSectorAccion(areaSector);
        }
        if(accion.getDeteccion().getId()!=IdDeteccion){
            Deteccion deteccion = mDeteccion.GetDeteccion(IdDeteccion);
            accion.setDeteccion(deteccion);
        }
        if(accion.getCodificacionAccion().getId()!=IdCodificacion){
            Codificacion codificacion = mCodificacion.GetCodificacion(IdCodificacion);
            accion.setCodificacionAccion(codificacion);
        }
        accion.setFechaDeteccion(FechaDeteccion);
        accion.setDescripcion(Descripcion);
        if(TipoAccion == TipoAccion.CORRECTIVA){
            ((Correctiva)accion).setTipo(TipoDesvio);
        }
        accion.setAnalisisCausa(AnalisisCausa);
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Elimina la accion seleccionada.
     * @param IdAccion
     * @return return Retorna -1 si no se elimino. Retorna el id de la accion eliminada.
     */
    public int EliminarAccion(int IdAccion){
        Accion accion = mAccion.GetAccion(IdAccion);
        return mAccion.BorrarAccion(accion);
    }
    
    /**
     * Setea el analisis de causa de la desviacion y actualiza la base de datos.
     * @param AnalisisDeCausa
     * @param IdAccion
     * @return -1 si no se actualizo.
     */
    public int SetAnalisisDeCausa(String AnalisisDeCausa, int IdAccion){
        Accion accion = mAccion.GetAccion(IdAccion);
        accion.setAnalisisCausa(AnalisisDeCausa);
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Setea el estado de la accion como desestimada y actualiza la base de datos.
     * @param Observaciones
     * @param IdAccion
     * @return
     */
    public int DesestimarAccion(String Observaciones, int IdAccion){
        Accion accion = mAccion.GetAccion(IdAccion);
        accion.setEstadoAccion(EnumEstado.DESESTIMADA);
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Remueve el producto involucrado de la accion seleccionada y actualiza la base de datos.
     * Elimina el producto de la base de datos.
     * @param IdAccionCorrectiva
     * @param NombreProducto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int RemoverProductoInvolucrado(int IdAccionCorrectiva, String NombreProducto){
        Accion accion = mAccion.GetAccion(IdAccionCorrectiva);
        int IdProducto =0;
        List<Producto> productos = ((Correctiva)accion).getProductosAfectados();
        IdProducto = productos.stream()
                .filter(producto -> producto.getNombre().equalsIgnoreCase(NombreProducto))
                .findFirst()
                .get().getId();
        ((Correctiva)accion).removeProductoAfectado(IdProducto);
        int res = mAccion.ActualizarAccion(accion);
        if(res!=-1){
            Producto producto = mProducto.GetProducto(IdProducto);
            mProducto.BorrarProducto(producto);
        }
        return res;
    }
    
    /**
     * Remueve el archivo adjunto de la accion seleccionada y actualiza la base de datos.
     * Elimina el adjunto de la base de datos.
     * @param IdAccion
     * @param IdAdjunto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int RemoverArchivoAdjunto(int IdAccion, int IdAdjunto){
        Accion accion = mAccion.GetAccion(IdAccion);
        accion.RemoveAdjunto(IdAdjunto);
        int res = mAccion.ActualizarAccion(accion);
        if(res!=-1){
            Adjunto adjunto = mAdjunto.GetAdjunto(IdAdjunto);
            mAdjunto.BorrarAdjunto(adjunto);
        }
        return res;
    }
    
    /**
     * Remueve la actividad de mejora de la accion de mejora seleccionada y actualiza la base de datos.
     * Elimina la actividad de mejora de la base de datos.
     * @param IdAccion
     * @param IdActividad
     * @return Retorna -1 si se actualizo. Retorna el IdAccion si no se elimino.
     */
    public int RemoverActividad(int IdAccion, int IdActividad){
        Accion accion = mAccion.GetAccion(IdAccion);
        Actividad actividad = mActividad.GetActividad(IdActividad);
        accion.RemoveActividad(actividad);

        ((Mejora)accion).CambiarEstado();
        int res = mAccion.ActualizarAccion(accion);
        if(res!=-1){
           res = mActividad.BorrarActividad(actividad);
        }
        return res;
    }   
    
    /**
     * Actualiza la actividad en la base de datos.
     * @param IdActividad
     * @param IdResponsable
     * @param FechaEstimada
     * @param Descripcion
     * @return Retorna -1 si no se actualizo. Retorna el IdActividad si se actualizo.
     */
    public int EditarActividad(int IdActividad, int IdResponsable, Date FechaEstimada, String Descripcion){
        Actividad actividad = mActividad.GetActividad(IdActividad);
        actividad.setDescripcion(Descripcion);
        actividad.setFechaEstimadaImplementacion(FechaEstimada);
        if(actividad.getResponsableImplementacion().getId() != IdResponsable){
            // @todo implementar manejador/controlador responsables
            Responsable responsable = mUsuario.GetUsuario(IdResponsable);
            actividad.setResponsableImplementacion(responsable);
        }
        return mActividad.ActualizarActividad(actividad);
    }
    
    /*
    PRODUCTO
    */
    
    /**
     * Cambia los valores de Producto y actualiza la base de datos.
     * @param IdProducto
     * @param NombreProducto
     * @param DatosProducto
     * @return Retorna -1 si no se actualizo. Retorna el IdProducto si se actualizo.
     */
    public int EditarProducto(int IdProducto, String NombreProducto, String DatosProducto){
        Producto producto = mProducto.GetProducto(IdProducto);
        producto.setDatos(DatosProducto);
        producto.setNombre(NombreProducto);
        return mProducto.ActualizarProducto(producto);
    }
    
    /*
    FORTALEZA
    */
    
    /**
     * Actualiza la fortaleza especificada por su id en la base de datos.
     * @param IdFortaleza
     * @param FechaDeteccion
     * @param Descripcion
     * @param IdDeteccion
     * @param IdAreaSector
     * @return Retorna el id de la fortaleza si se actualizo, de lo contrario retorna -1.
     */
    public int EditarFortaleza(int IdFortaleza, Date FechaDeteccion, String Descripcion, int IdDeteccion, int IdAreaSector){
        Fortaleza fortaleza = mFortaleza.GetFortaleza(IdFortaleza);
        if(fortaleza.getAreaSectorFortaleza().getId() != IdAreaSector){
            Area area = mArea.GetArea(IdAreaSector);
            fortaleza.setAreaSectorFortaleza(area);
        }
        if(fortaleza.getDeteccion().getId() != IdDeteccion){
            Deteccion deteccion = mDeteccion.GetDeteccion(IdDeteccion);
            fortaleza.setDeteccion(deteccion);
        }
        fortaleza.setDescripcion(Descripcion);
        fortaleza.setFechaDeteccion(FechaDeteccion);
        return mFortaleza.ActualizarFortaleza(fortaleza);
    }
    
    /**
     * Se elimina la fortaleza indicada de la base de datos.
     * @param IdFortaleza
     * @return Retorna el id de la fortaleza si se elimino, de lo contrario retorna -1.
     */
    public int EliminarFortaleza(int IdFortaleza){
        Fortaleza fortaleza = mFortaleza.GetFortaleza(IdFortaleza);
        return mFortaleza.BorrarFortaleza(fortaleza);
    }
}
