/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.facades;

import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.acciones.TipoDesvio;
import com.dperez.maymweb.accion.actividad.TipoActividad;
import com.dperez.maymweb.accion.adjunto.EnumTipoAdjunto;
import com.dperez.maymweb.controlador.registro.ControladorEdicionRegistro;
import com.dperez.maymweb.controlador.registro.ControladorRegistro;
import com.dperez.maymweb.fortaleza.Fortaleza;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Diego
 */
@Named
@Stateless
public class FacadeDatos {
    @Inject
    private ControladorRegistro cReg;
    @Inject
    private ControladorEdicionRegistro cEdicion;
    
    //  Constructores
    public FacadeDatos(){}
    
    /**
     * Crea una nueva accion en estado pendiente y la persiste en la base de datos.
     * @param TipoAccion
     * @param FechaDeteccion
     * @param Descripcion
     * @param TipoDesvio Null cuando no corresponde
     * @param IdAreaSector
     * @param IdDeteccion
     * @param IdEmpresa
     * @return Null: si no se creo.
     */
    public Accion NuevaAccion(TipoAccion TipoAccion, Date FechaDeteccion, String Descripcion, TipoDesvio TipoDesvio,
            int IdAreaSector, int IdDeteccion, int IdEmpresa){
        return cReg.NuevaAccion(TipoAccion, FechaDeteccion, Descripcion, TipoDesvio, IdAreaSector, IdDeteccion);
    }
    
    /**
     * Crea el/los productos involucrados en el desvio, los agrega a la accion correctiva y actualiza la base de datos.
     * @param AccionCorrectiva
     * @param NombreProducto
     * @param DatosProducto
     * @return -1 si no se creo.
     */
    public int AgregarProductoInvolucrado(int AccionCorrectiva, String NombreProducto, String DatosProducto){
        return cReg.AgregarProductoInvolucrado(AccionCorrectiva, NombreProducto, DatosProducto);
    }
    
    /**
     * Crea el/los adjuntos, los agrega a la accion y actualiza la base de datos.
     * @param IdAccion
     * @param TituloAdjunto
     * @param UbicacionAdjunto
     * @param TipoAdjunto
     * @return -1 si no se creo.
     */
    public int AgregarArchivoAdjunto(int IdAccion, String TituloAdjunto, String UbicacionAdjunto, EnumTipoAdjunto TipoAdjunto){
        return cReg.AgregarArchivoAdjunto(IdAccion, TituloAdjunto, UbicacionAdjunto, TipoAdjunto);
    }
    
    /**
     * Crea una nueva actividad de mejora, la persiste en la base de datos y se asocia a la mejora.
     * @param IdAccion
     * @param FechaEstimadaImplementacion
     * @param Descripcion
     * @param IdResponsable
     * @param TipoActividad
     * @return null si no se creo.
     */
    public Actividad AgregarActividad(int IdAccion, Date FechaEstimadaImplementacion, String Descripcion, int IdResponsable, TipoActividad TipoActividad){
        return cReg.AgregarActividad(IdAccion, FechaEstimadaImplementacion, Descripcion, IdResponsable, TipoActividad);
    }
    
    /**
     * Setea la fecha de implementacion de la Medida correctiva, cambia el estado de la accion (si corresponde) y persiste los cambios en la base de deatos.
     * @param FechaImplementacion
     * @param IdMedidaCorrectiva
     * @param IdAccion
     * @return -1 si no se pudo actualizar.
     */
    public int SetFechaImplementacionActividad(Date FechaImplementacion, int IdMedidaCorrectiva, int IdAccion){
        return cReg.SetFechaImplementacionActividad(FechaImplementacion, IdMedidaCorrectiva, IdAccion);
    }
    
    /**
     * Setea la comprobacion de implementacion estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param FechaEstimada
     * @param IdUsuarioResponsableImplementacion
     * @param IdAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int SetComprobacionImplementacion(Date FechaEstimada, int IdUsuarioResponsableImplementacion, int IdAccion){
        return cReg.SetComprobacionImplementacion(FechaEstimada, IdUsuarioResponsableImplementacion, IdAccion);
    }
    
    /**
     * Setea la comprobacion de eficacia estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param FechaEstimada
     * @param IdUsuarioResponsableComprobacion
     * @param IdAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int SetVerificacionEficacia(Date FechaEstimada, int IdUsuarioResponsableComprobacion, int IdAccion){
        return cReg.SetComprobacionEficacia(FechaEstimada, IdUsuarioResponsableComprobacion, IdAccion);
    }
    
    /**
     * Remueve la medida correctiva de la accion correctiva seleccionada y actualiza la base de datos.
     * Elimina la medida correctiva de la base de datos.
     * @param IdAccion
     * @param IdActividad
     * @return Retorna -1 si se actualizo. Retorna el IdAccion si no se elimino.
     */
    public int RemoverActividad(int IdAccion, int IdActividad){
        return cEdicion.RemoverActividad(IdAccion, IdActividad);
    }
        
    /**
     * Remueve el archivo adjunto de la accion seleccionada y actualiza la base de datos.
     * Elimina el adjunto de la base de datos.
     * @param IdAccion
     * @param IdAdjunto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int RemoverAdjunto(int IdAccion, int IdAdjunto){
        return cEdicion.RemoverArchivoAdjunto(IdAccion, IdAdjunto);
    }
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
        return cEdicion.EditarAccion(IdAccion, TipoAccion, FechaDeteccion, Descripcion, AnalisisCausa, TipoDesvio, IdAreaSector, IdDeteccion, IdCodificacion);
    }
    
    /**
     * Actualiza la actividad en la base de datos.
     * @param IdActividad
     * @param Descripcion
     * @param ResponsableImplementacion
     * @param FechaImplementacion
     * @return Retorna -1 si no se actualizo. Retorna el IdActividad si se actualizo.
     */
    public int EditarActividad(int IdActividad, String Descripcion, int ResponsableImplementacion, Date FechaImplementacion){
        return cEdicion.EditarActividad(IdActividad, ResponsableImplementacion, FechaImplementacion,Descripcion);
    }
    
    /**
     * Remueve el producto involucrado de la accion seleccionada y actualiza la base de datos.
     * Elimina el producto de la base de datos.
     * @param IdAccionCorrectiva
     * @param NombreProducto
     * @return Retorna -1 si no se actualizo. Retorna IdAccion si se actualizo.
     */
    public int RemoverProductoInvolucrado(int IdAccionCorrectiva, String NombreProducto){
        return cEdicion.RemoverProductoInvolucrado(IdAccionCorrectiva, NombreProducto);
    }
    
    /**
     * Crea una nueva fortaleza y la persiste en la base de datos
     * @param FechaDeteccion
     * @param Descripcion
     * @param IdDeteccion
     * @param IdAreaSector
     * @param IdEmpresa
     * @return Null: si no se creo.
     */
    public Fortaleza NuevaFortaleza(Date FechaDeteccion, String Descripcion, int IdDeteccion, int IdAreaSector){
        return cReg.NuevaFortaleza(FechaDeteccion, Descripcion, IdDeteccion, IdAreaSector);
    }
    
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
        return cEdicion.EditarFortaleza(IdFortaleza, FechaDeteccion, Descripcion, IdDeteccion, IdAreaSector);
    }
    
    
}
