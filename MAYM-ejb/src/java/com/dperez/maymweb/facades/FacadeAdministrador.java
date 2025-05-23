/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maymweb.facades;

import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.controlador.configuracion.ControladorConfiguracion;
import com.dperez.maymweb.controlador.registro.ControladorEdicionRegistro;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.modelo.empresa.Empresa;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Usuario;
import com.dperez.maymweb.modelo.usuario.EnumPermiso;
import com.dperez.maymweb.modelo.usuario.Responsable;

/**
 * Facade con los medotos para el manejo solo de las clases que seran parte de
 * la configuracion de la aplicacion. No genera/edita registros.
 *
 * @author Diego
 */
public class FacadeAdministrador {

    private final ControladorConfiguracion cConfig;
    private final ControladorEdicionRegistro cEdicion;

    //  Constructores
    public FacadeAdministrador() {
        cConfig = new ControladorConfiguracion();
        cEdicion = new ControladorEdicionRegistro();
    }
    
    //<editor-fold desc="EMPRESAS">
    /**
     * Crea un nueva Empresa.
     *
     * @param nombre
     * @param direccion
     * @param telefono
     * @param movil
     * @param correo
     * @param nombreExtra
     * @return null si no se creo.
     */
    public Empresa nuevaEmpresa(String nombre, String direccion, String telefono, String movil, String correo, String nombreExtra) {
        return cConfig.nuevaEmpresa(nombre, direccion, telefono, movil, correo, nombreExtra);
    }

    /**
     * Setea los datos de la empresa.
     *
     * @param idEmpresa
     * @param nombre
     * @param direccion
     * @param telefono
     * @param movil
     * @param correo
     * @param nombreExtra
     * @return -1 si no se actualizo.
     */
    public int cambiarDatosEmpresa(int idEmpresa, String nombre, String direccion, String telefono, String movil,
            String correo, String nombreExtra) {
        return cConfig.cambiarDatosEmpresa(idEmpresa, nombre, direccion, telefono, movil, correo, nombreExtra);
    }

    /**
     * Setea la ubicacion del logo de la empresa.
     *
     * @param idEmpresa
     * @param ubicacionLogo
     * @return
     */
    public int setUbicacionLogoEmpresa(int idEmpresa, String ubicacionLogo) {
        return cConfig.setUbicacionLogoEmpresa(idEmpresa, ubicacionLogo);
    }

    /**
     * Setea la ubicacion del logo adicional de la empresa.
     *
     * @param idEmpresa
     * @param ubicacionLogo
     * @return
     */
    public int setUbicacionLogoAdicionaleEmpresa(int idEmpresa, String ubicacionLogo) {
        return cConfig.setUbicacionLogoAdicionaleEmpresa(idEmpresa, ubicacionLogo);
    }

    /**
     * Setea las opciones de apariencia del sistema asociadas a la Empresa.
     * @param idEmpresa
     * @param colorSuperiorPanelTitulo
     * @param colorInferiorPanelTitulo
     * @param colorFuentePanelEncabezado
     * @param colorPanelTitulo
     * @param colorFuentePanelTitulo
     * @param colorBody
     * @param colorBoton
     * @return 
     */
    public int setConfiguracionApariencia(int idEmpresa, String colorSuperiorPanelTitulo, String colorInferiorPanelTitulo,
            String colorFuentePanelEncabezado, String colorPanelTitulo, String colorFuentePanelTitulo, String colorBody, String colorBoton) {
        return cConfig.setConfiguracionApariencia(idEmpresa, colorSuperiorPanelTitulo, colorInferiorPanelTitulo, colorFuentePanelEncabezado, 
                colorPanelTitulo, colorFuentePanelTitulo, colorBody, colorBoton);                
    }
    
    /**
     * Setea las opciones de apariencia del sistema asociadas a la Empresa.
     * @param idEmpresa
     * @param mailFrom
     * @param mailUser
     * @param mailPass
     * @param mailPort
     * @param mailTLS

     * @return 
     */
    public int setConfiguracionCorreo(int idEmpresa, String mailFrom, String mailUser, String mailPass, String mailHostSMTP, int mailPort, boolean mailTLS) {
        return cConfig.setConfiguracionCorreo(idEmpresa, mailFrom, mailUser, mailPass, mailHostSMTP, mailPort, mailTLS);
    }
    
    /**
     * Setea estado de alertas.
     * @param idEmpresa
     * @param setAlertas
     * @return 
     */
    public int setAlertas(int idEmpresa, boolean setAlertas){
        return setAlertas(idEmpresa, setAlertas);
    }
    //</editor-fold>
    
    //</editor-fold>

    //<editor-fold desc="AREAS">
    /**
     * *
     * Crea una nueva area/sector y la persiste en la base de datos. Se verifica
     * si existe el nombre del area en la base de datos.
     *
     * @param NombreArea
     * @param CorreoArea
     * @return null si no se creo.
     */
    public Area nuevaArea(String NombreArea, String CorreoArea) {
        return cConfig.nuevaArea(NombreArea, CorreoArea);
    }

    /**
     * Cambia los valores de area y actualiza la base de datos. Se verifica si
     * existe el nombre del area en la base de datos.
     *
     * @param IdArea
     * @param NombreArea
     * @param CorreoArea
     * @return Retorna -1 si no se actualizo. Retorna el IdArea si se actualizo.
     */
    public int editarArea(int IdArea, String NombreArea, String CorreoArea) {
        return cConfig.editarArea(IdArea, NombreArea, CorreoArea);
    }

    /**
     * Elimina el area de la base de datos. Actualiza la relaciones Empesa. Se
     * comprueba que no tenga acciones y fortalezas relacionadas.
     *
     * @param IdArea
     * @return Retorna el id del area si se elimino. Retorna -1 si no se
     * elimino.
     */
    public int eliminarArea(int IdArea) {
        return cConfig.eliminarArea(IdArea);
    }

    //</editor-fold>
    //<editor-fold desc="CODIFICACIONES">
    /**
     * *
     * Crea una nueva codificacion y la persiste en la base de datos.
     *
     * @param NombreCodificacion
     * @param DescripcionCodificacion
     * @return null si no se creo.
     */
    public Codificacion nuevaCodificacion(String NombreCodificacion, String DescripcionCodificacion) {
        return cConfig.nuevaCodificacion(NombreCodificacion, DescripcionCodificacion);
    }

    /**
     * Cambia los valores de Codificacion y actualiza la base de datos. Se
     * verifica si existe el nombre de la codificacion en la base de datos.
     *
     * @param IdCodificacion
     * @param NombreCodificacion
     * @param DescripcionCodificacion
     * @return Retorna -1 si no se actualizo. Retorna el IdCodificacion si se
     * actualizo.
     */
    public int editarCodificacion(int IdCodificacion, String NombreCodificacion, String DescripcionCodificacion) {
        return cConfig.editarCodificacion(IdCodificacion, NombreCodificacion, DescripcionCodificacion);
    }

    /**
     * Elimina la codificacion de la base de datos. PRE: se debe comprobar que
     * existe a una unica empresa. Se comprueba que no tenga acciones
     * relacionadas.
     *
     * @param IdCodificacion
     * @return Retorna el id de la codificacion si se elimino. Retorna -1 si no
     * se elimino.
     */
    public int eliminarCodificacion(int IdCodificacion) {
        return cConfig.eliminarCodificacion(IdCodificacion);
    }
    //</editor-fold>

    //<editor-fold desc="DETECCIONES">
    /**
     * *
     * Crea una nueva deteccion y la persiste en la base de datos.
     *
     * @param NombreDeteccion
     * @param tipoDeteccion
     * @return null si no se creo.
     */
    public Deteccion nuevaDeteccion(String NombreDeteccion, TipoDeteccion tipoDeteccion) {
        return cConfig.nuevaDeteccion(NombreDeteccion, tipoDeteccion);
    }

    /**
     * Cambia los valores de Deteccion y actualiza la base de datos. Se
     * comprueba que no existe una deteccion con el mismo nombre.
     *
     * @param IdDteccion
     * @param NombreDeteccion
     * @param TipoDeteccion
     * @return Retorna -1 si no se actualizo. Retorna el IdDeteccion si se
     * actualizo.
     */
    public int editarDeteccion(int IdDteccion, String NombreDeteccion, TipoDeteccion TipoDeteccion) {
        return cConfig.editarDeteccion(IdDteccion, NombreDeteccion, TipoDeteccion);
    }

    /**
     * Elimina la deteccion de la base de datos. Se comprueba que no tenga
     * acciones y fortalezas relacionadas.
     *
     * @param IdDeteccion
     * @return Retorna el id de la deteccion si se elimino. Retorna -1 si no se
     * elimino.
     */
    public int eliminarDeteccion(int IdDeteccion) {
        return cConfig.eliminarDeteccion(IdDeteccion);
    }
    //</editor-fold>

    //<editor-fold desc="USUARIOS">
    /**
     * Crea un nuevo usuario y lo persiste en la base de datos. El usuario
     * creado no recibe alertas.
     *
     * @param IdUsuario
     * @param NombreUsuario
     * @param ApellidoUsuario
     * @param CorreoUsuario
     * @param Password
     * @param PermisoUsuario
     * @param IdArea
     * @param IdEmpresa
     * @return null si no se creo.
     */
    public Usuario nuevoUsuario(int IdUsuario, String NombreUsuario, String ApellidoUsuario, String CorreoUsuario, String Password,
            EnumPermiso PermisoUsuario, int IdArea, int IdEmpresa) {
        return cConfig.nuevoUsuario(IdUsuario, NombreUsuario, ApellidoUsuario, CorreoUsuario, 
                Password, PermisoUsuario, IdArea, IdEmpresa);
    }

    /**
     * Comprueba si existe el usuario con el id especificado.
     *
     * @param IdUsuario
     * @return
     */
    public boolean existeUsuario(int IdUsuario) {
        return cConfig.existeUsuario(IdUsuario);
    }

    /**
     * Elimina el usuario indicado por su id. Si el usuario esta relacionado con
     * comprobaciones o actividades no se elimina.
     *
     * @param IdUsuario
     * @return Retorna <b>Tur</b> si se elimina, <b>False</b> de lo contrario.
     */
    public int eliminarUsuario(int IdUsuario) {
        return cConfig.eliminarUsuario(IdUsuario);
    }

    /**
     * Da de baja un usuario.
     *
     * @param IdUsuario
     * @return
     */
    public int darDeBajaUsuario(int IdUsuario) {
        return cConfig.cambiarEstadoUsuario(IdUsuario, false);
    }

    /**
     * Da de alta un usuario.
     *
     * @param IdUsuario
     * @return
     */
    public int darDeAltaUsuario(int IdUsuario) {
        return cConfig.cambiarEstadoUsuario(IdUsuario, true);
    }

    //</editor-fold>
    //<editor-fold desc="ACCIONES">
    /**
     * Elimina la accion seleccionada.
     *
     * @param IdAccion
     * @return return Retorna -1 si no se elimino. Retorna el id de la accion
     * eliminada.
     */
    public int eliminarAccion(int IdAccion) {
        return cEdicion.eliminarAccion(IdAccion);
    }

    /**
     * Se elimina la fortaleza indicada de la base de datos.
     *
     * @param IdFortaleza
     * @return Retorna el id de la fortaleza si se elimino, de lo contrario
     * retorna -1.
     */
    public int eliminarFortaleza(int IdFortaleza) {
        return cEdicion.eliminarFortaleza(IdFortaleza);
    }

    //</editor-fold>
    //<editor-fold desc="RESPONSABLES Y RESPONSABILIDADES">
    public Responsabilidad nuevaResponsabilidad(String nombre) {
        return cConfig.crearResponsabilidad(nombre);
    }

    public int editarResponsabilidad(int idResponsabilidad, String nombreNuevo) {
        return cConfig.editarResponsabilidad(idResponsabilidad, nombreNuevo);
    }

    public Responsable nuevoResponsable(int idResponsabilidad, int idUsuario) {
        return cConfig.crearResponsable(idResponsabilidad, idUsuario);
    }

    public int darBajaResponsable(int idResponsabilidad, int idResponsable) {
        return cConfig.darBajaResponsable(idResponsabilidad, idResponsable);
    }

    public int eliminarResponsable(int idResponsable) {
        return cConfig.eliminarResponsable(idResponsable);
    }

    public int eliminarResponsabilidad(int idResponsabilidadSeleccionada) {
        return cConfig.eliminarResponsabilidad(idResponsabilidadSeleccionada);
    }

    public int editarResponsable(int id, String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    //</editor-fold>
}
