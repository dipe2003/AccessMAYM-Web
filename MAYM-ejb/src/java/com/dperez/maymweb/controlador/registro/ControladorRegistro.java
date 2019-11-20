/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.controlador.registro;

import com.dperez.maymweb.acciones.Accion;
import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
import com.dperez.maymweb.accion.comprobaciones.ResultadoComprobacion;
import com.dperez.maymweb.acciones.ManejadorAccion;
import com.dperez.maymweb.acciones.Correctiva;
import com.dperez.maymweb.acciones.TipoAccion;
import com.dperez.maymweb.acciones.TipoDesvio;
import com.dperez.maymweb.acciones.Mejora;
import com.dperez.maymweb.acciones.Preventiva;
import com.dperez.maymweb.accion.adjunto.Adjunto;
import com.dperez.maymweb.accion.actividad.ManejadorActividad;
import com.dperez.maymweb.accion.actividad.Actividad;
import com.dperez.maymweb.accion.actividad.TipoActividad;
import com.dperez.maymweb.accion.adjunto.EnumTipoAdjunto;
import com.dperez.maymweb.accion.adjunto.ManejadorAdjunto;
import com.dperez.maymweb.area.Area;
import com.dperez.maymweb.area.ManejadorArea;
import com.dperez.maymweb.codificacion.Codificacion;
import com.dperez.maymweb.codificacion.ManejadorCodificacion;
import com.dperez.maymweb.deteccion.Deteccion;
import com.dperez.maymweb.deteccion.ManejadorDeteccion;
import com.dperez.maymweb.empresa.ManejadorEmpresa;
import com.dperez.maymweb.fortaleza.Fortaleza;
import com.dperez.maymweb.fortaleza.ManejadorFortaleza;
import com.dperez.maymweb.producto.ManejadorProducto;
import com.dperez.maymweb.producto.Producto;
import com.dperez.maymweb.usuario.ManejadorUsuario;
import com.dperez.maymweb.usuario.Responsable;
import com.dperez.maymweb.usuario.Usuario;
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
public class ControladorRegistro {
    @Inject
    private ManejadorAccion mAccion;
    @Inject
    private ManejadorArea mArea;
    @Inject
    private ManejadorAdjunto mAdjunto;
    @Inject
    private ManejadorDeteccion mDeteccion;
    @Inject
    private ManejadorActividad mMedida;
    @Inject
    private ManejadorUsuario mUsuario;
    @Inject
    private ManejadorEmpresa mEmpresa;
    @Inject
    private ManejadorFortaleza mFortaleza;
    @Inject
    private ManejadorCodificacion mCodificacion;
    @Inject
    private ManejadorProducto mProducto;
    
    //  Constructores
    public ControladorRegistro(){}
    
    /**
     * Crea una nueva accion en estado pendiente y la persiste en la base de datos.
     * Se codifica automaticamente la accion como 'Sin Codificar'. Si la codificacon no existe se crea.
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
            int IdAreaSector, int IdDeteccion){
        Accion accion;
        switch(TipoAccion){
            case CORRECTIVA:
                accion = new Correctiva(FechaDeteccion, Descripcion, TipoDesvio);
                break;
                
            case MEJORA:
                accion = new Mejora(FechaDeteccion, Descripcion);
                break;
                
            default:
                accion = new Preventiva(FechaDeteccion, Descripcion);
                break;
        }
        
        Area area = mArea.GetArea(IdAreaSector);
        accion.setAreaSectorAccion(area);
        Deteccion deteccion = mDeteccion.GetDeteccion(IdDeteccion);
        accion.setDeteccion(deteccion);
        // codificacion
        List<Codificacion> codificaciones = mCodificacion.ListarCodificaciones();
        int existe = 0;
        existe = codificaciones.stream()
                .filter(codificacion -> codificacion.getNombre().equalsIgnoreCase("Sin Codificar"))
                .findFirst()
                .get().getId();
        if(existe==0){
            Codificacion codificacion = new Codificacion("Sin Codificar", "No se ha asignado codificacion aun.");
            codificacion.setId(mCodificacion.CrearCodificacion(codificacion));
            accion.setCodificacionAccion(codificacion);
        }else{
            Codificacion codificacion = mCodificacion.GetCodificacion(existe);
            accion.setCodificacionAccion(codificacion);
        }
        accion.setId(mAccion.CrearAccion(accion));
        
        if(accion.getId()!=-1){
            return accion;
        }else{
            return null;
        }
    }
    
    /**
     * Crea el/los productos involucrados en el desvio, los agrega a la accion correctiva y actualiza la base de datos.
     * @param AccionCorrectiva
     * @param NombreProducto
     * @param DatosProducto
     * @return -1 si no se creo.
     */
    public int AgregarProductoInvolucrado(int AccionCorrectiva, String NombreProducto, String DatosProducto){
        Correctiva correctiva = (Correctiva) mAccion.GetAccion(AccionCorrectiva);
        Producto producto = correctiva.addProductoAfectado(NombreProducto, DatosProducto);
        if(mProducto.CrearProducto(producto)!=-1){
            return mAccion.ActualizarAccion(correctiva);
        }
        return -1;
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
        Accion accion = mAccion.GetAccion(IdAccion);
        Adjunto adjunto = new Adjunto(TituloAdjunto, UbicacionAdjunto, TipoAdjunto);
        if(mAdjunto.CrearAdjunto(adjunto)!=-1){
            accion.AddAdjunto(adjunto);
        }
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Crea una nueva actividad de mejora, la persiste en la base de datos y se asocia a la mejora.
     * @param IdAccion
     * @param FechaEstimadaImplementacion
     * @param Descripcion
     * @param IdResponsable
     * @param tipoActividad
     * @return -1 si no se creo.
     */
    public int AgregarActividad(int IdAccion, Date FechaEstimadaImplementacion, String Descripcion, int IdResponsable, TipoActividad tipoActividad){
        Accion accion = mAccion.GetAccion(IdAccion);
        // @todo implementar manejador/controlador responsable 
        Responsable resposable = mUsuario.GetUsuario(IdResponsable);
        Actividad actividad =  accion.AddActividad(FechaEstimadaImplementacion, Descripcion, resposable, tipoActividad);
        accion.CambiarEstado();
        mAccion.ActualizarAccion(accion);
        return actividad.getIdActividad();
    }
    
    
    /**
     * Setea la fecha de implementacion de la Actividad correctiva, cambia el estado de la accion (si corresponde) y persiste los cambios en la base de deatos.
     * @param FechaImplementacion
     * @param IdActividad
     * @param IdAccion
     * @return -1 si no se pudo actualizar.
     */
    public int SetFechaImplementacionActividad(Date FechaImplementacion, int IdActividad, int IdAccion){
        Accion accion = mAccion.GetAccion(IdAccion);
        accion.SetFechaImplementacionActividad(IdActividad, FechaImplementacion);
        accion.CambiarEstado();
        return mAccion.ActualizarAccion(accion);
    }

    
    /**
     * Setea la comprobacion de implementacion estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param FechaEstimada
     * @param IdUsuarioResponsableImplementacion
     * @param IdAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int SetComprobacionImplementacion(Date FechaEstimada, int IdUsuarioResponsableImplementacion, int IdAccion){
        // @todo agregar manejador/controlador para responsables
        Responsable responsable = mUsuario.GetUsuario(IdUsuarioResponsableImplementacion);
        Accion accion = mAccion.GetAccion(IdAccion);
        Comprobacion comprobacion = accion.setComprobacionImplementacion(FechaEstimada, responsable);
        mAccion.crearComprobacion(comprobacion);
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Setea la comprobacion de eficacia estimada con responsable y fecha estimada. Actualiza la base de datos.
     * @param FechaEstimada
     * @param IdUsuarioResponsableComprobacion
     * @param IdAccion
     * @return -1 si no se actualizo. IdAccion si correcto.
     */
    public int SetComprobacionEficacia(Date FechaEstimada, int IdUsuarioResponsableComprobacion, int IdAccion){
        // @todo agregar manejador/controlador para responsables
        Responsable responsable = mUsuario.GetUsuario(IdUsuarioResponsableComprobacion);
        Accion accion = mAccion.GetAccion(IdAccion);
        Comprobacion comprobacion = accion.setComprobacionEficacia(FechaEstimada, responsable);
        mAccion.crearComprobacion(comprobacion);
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Setea la comprobacion de implementacion de la accion, cambia el estado segun corresponda y actualiza la base de datos.
     * PRE: la accion debe tener comprobacion != null (seteada fecha estimada de comprobacion de implementacion)
     * @param FechaComprobacionImplementacion
     * @param ComentariosImplementacion
     * @param Comprobacion
     * @param IdAccion
     * @return -1 si no se actualizo
     */
    public int ComprobarImplementacionAccion(Date FechaComprobacionImplementacion, String ComentariosImplementacion, ResultadoComprobacion Comprobacion,
            int IdAccion){
        Accion accion = mAccion.GetAccion(IdAccion);
        accion.getComprobacionImplementacion().setFechaComprobacion(FechaComprobacionImplementacion);
        accion.getComprobacionImplementacion().setObservaciones(ComentariosImplementacion);
        accion.getComprobacionImplementacion().setResultado(Comprobacion);
        accion.CambiarEstado();
        return mAccion.ActualizarAccion(accion);
    }
    
    /**
     * Setea la verificacion de eficacia de la accion, deja en estado cerrado y actualiza la base de datos.
     * @param FechaVerificacionEficacia
     * @param ComentariosVerificacion
     * @param Comprobacion
     * @param IdAccion
     * @return -1 si no se actualizo
     */
    public int VerificarEficaciaAccion(Date FechaVerificacionEficacia, String ComentariosVerificacion, ResultadoComprobacion Comprobacion,
            int IdAccion){
        Accion accion = mAccion.GetAccion(IdAccion);
        accion.getComprobacionEficacia().setFechaComprobacion(FechaVerificacionEficacia);
        accion.getComprobacionEficacia().setObservaciones(ComentariosVerificacion);
        accion.getComprobacionEficacia().setResultado(Comprobacion);
        accion.CambiarEstado();
        return mAccion.ActualizarAccion(accion);
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
        Fortaleza fortaleza = new Fortaleza(FechaDeteccion, Descripcion);
        
        Area area = mArea.GetArea(IdAreaSector);
        Deteccion deteccion = mDeteccion.GetDeteccion(IdDeteccion);
        fortaleza.setAreaSectorFortaleza(area);
        fortaleza.setDeteccion(deteccion);
        fortaleza.setId(mFortaleza.CrearFortaleza(fortaleza));
        
        if(fortaleza.getId()== -1){
            return null;
        }
        return fortaleza;
    }
    
}
