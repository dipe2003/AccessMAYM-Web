/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.controlador.configuracion;

import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.ControladorSeguridad;
import com.dperez.maymweb.modelo.usuario.Credencial;
import com.dperez.maymweb.modelo.usuario.Usuario;
import com.dperez.maymweb.modelo.usuario.EnumPermiso;
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.persistencia.FabricaRepositorio;
import com.dperez.maymweb.persistencia.RepositorioPersistencia;
import java.util.Date;
import javax.inject.Named;

/**
 * Este controlador implementa los metodos necesarios para la creacion de los
 * objetos que se modificarán como configuracion de la aplicacion.
 *
 * @author Diego
 */
@Named
public class ControladorConfiguracion {
    
    private final RepositorioPersistencia<Area> repoArea;
    private final RepositorioPersistencia<Deteccion> repoDeteccion;
    private final RepositorioPersistencia<Codificacion> repoCodificacion;
    private final RepositorioPersistencia<Usuario> repoUsuario;
    private final RepositorioPersistencia<Responsable> repoResponsable;
    private final RepositorioPersistencia<Responsabilidad> repoResponsabilidades;
    
    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    
    
    private final ControladorSeguridad cSeg;
    
    public ControladorConfiguracion() {
        this.repoCodificacion = fabricaRepositorio.getRepositorioCodificaciones();
        this.repoArea = fabricaRepositorio.getRepositorioAreas();
        this.repoDeteccion = fabricaRepositorio.getRepositorioDetecciones();
        this.repoUsuario = fabricaRepositorio.getRepositorioUsuarios();
        this.repoResponsable = fabricaRepositorio.getRepositorioResponsables();
        this.repoResponsabilidades = fabricaRepositorio.getRepositorioResponsabilidades();
        cSeg = new ControladorSeguridad();
    }
    
    /*
    USUARIO
    */
    /**
     * Crea un nuevo usuario y lo persiste en la base de datos.El usuario
     * creado no recibe alertas.
     *
     * @param idUsuario
     * @param nombreUsuario
     * @param apellidoUsuario
     * @param orreoUcsuario
     * @param password
     * @param permisoUsuario
     * @param idArea
     * @return null si no se creo.
     */
    public Usuario nuevoUsuario(int idUsuario, String nombreUsuario, String apellidoUsuario, String orreoUcsuario,
            String password, EnumPermiso permisoUsuario, int idArea) {
        Usuario usuario = new Usuario(nombreUsuario, apellidoUsuario, orreoUcsuario, false, permisoUsuario);
        String[] pass = cSeg.getPasswordSeguro(password);
        // pass[0] corresponde al password | pass[1] corresponde al salt
        Credencial credencial = new Credencial(pass[1], pass[0]);
        usuario.setCredencialUsuario(credencial);
        credencial.setUsuarioCredencial(usuario);
        usuario.setId(idUsuario);
        Area area = repoArea.find(idArea);
        usuario.setAreaUsuario(area);
        int res = repoUsuario.create(usuario).getId();
        if (res != -1) {
            return usuario;
        } else {
            return null;
        }
    }
    
    /**
     * Comprueba si existe el usuario con id especificado.
     *
     * @param idUsuario
     * @return
     */
    public boolean existeUsuario(int idUsuario) {
        return repoUsuario.find(idUsuario) != null;
    }
    
    /**
     * Setea los datos del usuario y actualiza la base de datos. No se realiza
     * comprobacion de password. Para cambiar password utilizar metodo
     * cambiarPasswordUsuario().
     *
     * @param idUsuario
     * @param nombreUsuario
     * @param apellidoUsuario
     * @param correoUsuario
     * @param permisoUsuario
     * @param recibeAlertas
     * @param idArea
     * @return -1 si no se actualizo.
     */
    public int cambiarDatosUsuario(int idUsuario, String nombreUsuario, String apellidoUsuario, String correoUsuario,
            EnumPermiso permisoUsuario, boolean recibeAlertas, int idArea) {
        Usuario usuario = repoUsuario.find(idUsuario);
        usuario.setNombre(nombreUsuario);
        usuario.setApellido(apellidoUsuario);
        usuario.setCorreo(correoUsuario);
        usuario.setPermisoUsuario(permisoUsuario);
        usuario.setRecibeAlertas(recibeAlertas);
        if (usuario.getAreaUsuario().getId() != idArea) {
            Area area = repoArea.find(idArea);
            usuario.setAreaUsuario(area);
            repoArea.update(area);
        }
        return repoUsuario.update(usuario).getId();
    }
    
    /**
     * Comprueba la validez del password ingresado con el correspondiente del
     * usuario en la base de datos.
     *
     * @param idUsuario
     * @param password
     * @return True si es valido | Fales si no es valido.
     */
    public boolean comprobarValidezPassword(int idUsuario, String password) {
        Usuario usuario = repoUsuario.find(idUsuario);
        String PasswordStored = cSeg.getPasswordSeguro(password, usuario.getCredencialUsuario().getPasswordKey());
        return PasswordStored.equals(usuario.getCredencialUsuario().getPassword());
    }
    
    /**
     * Cambia la credencial (password y password key) del usuario especificado y
     * actualiza la base de datos. Si el password ingresado no coincide con el
     * guardado no se actualiza.
     *
     * @param idUsuario
     * @param oldPassword: password del usuario guardado en la base de datos.
     * @param newPassword: nuevo password para el usuario.
     * @return Retorna la credencial del usuario actualizada. Retorna Null si no
     * se pudo actualizar.
     */
    public Credencial cambiarCredencialUsuario(int idUsuario, String oldPassword, String newPassword) {
        int res = -1;
        Usuario usuario = repoUsuario.find(idUsuario);
        String PasswordStored = cSeg.getPasswordSeguro(oldPassword, usuario.getCredencialUsuario().getPasswordKey());
        if (PasswordStored.equals(oldPassword)) {
            String[] nuevaCredencial = cSeg.getPasswordSeguro(newPassword);
            usuario.getCredencialUsuario().setPassword(nuevaCredencial[1]);
            usuario.getCredencialUsuario().setPasswordKey(nuevaCredencial[0]);
            res = repoUsuario.update(usuario).getId();
        }
        if (res != -1) {
            return usuario.getCredencialUsuario();
        } else {
            return null;
        }
    }
    
    /**
     * Cambia la credencial (password y password key) del usuario especificado y
     * actualiza la base de datos. El metodo debe ser utilizado por un
     * Administrador del Sistema para resetear el password de un usuario.
     *
     * @param idUsuario
     * @param newPassword: nuevo password para el usuario.
     * @return Retorna la credencial del usuario actualizada. Retorna Null si no
     * se pudo actualizar.
     */
    public Credencial resetCredencialUsuario(int idUsuario, String newPassword) {
        int res = -1;
        Usuario usuario = repoUsuario.find(idUsuario);
        String[] nuevaCredencial = cSeg.getPasswordSeguro(newPassword);
        usuario.getCredencialUsuario().setPassword(nuevaCredencial[1]);
        usuario.getCredencialUsuario().setPasswordKey(nuevaCredencial[0]);
        res = repoUsuario.update(usuario).getId();
        if (res != -1) {
            return usuario.getCredencialUsuario();
        } else {
            return null;
        }
    }
    
    /**
     * Elimina el usuario indicado por su id. Si el usuario esta relacionado con
     * comprobaciones o actividades no se elimina.
     *
     * @param idUsuario
     * @return Retorna <b>Tur</b> si se elimina, <b>False</b> de lo contrario.
     */
    public int eliminarUsuario(int idUsuario) {
        Usuario usuario = repoUsuario.find(idUsuario);
        repoUsuario.delete(usuario);
        return 1;
    }
    
    /**
     * Cambia el estado de un usuario: da de alta o de baja. Baja de usuario
     * setea la fecha actual como fecha de baja.
     *
     * @param idUsuario
     * @param altaUsuario: True para dar de alta. False para dar de baja.
     * @return
     */
    public int cambiarEstadoUsuario(int idUsuario, boolean altaUsuario) {
        Usuario usuario = repoUsuario.find(idUsuario);
        if (altaUsuario) {
            usuario.setFechaBaja(null);
        } else {
            usuario.setFechaBaja(new Date());
        }
        return repoUsuario.update(usuario).getId();
    }
    
    /*
    AREA
    */
    
    /**
     * *
     * Crea una nueva area/sector y la persiste en la base de datos. Se verifica
     * si existe el nombre del area en la base de datos.
     *
     * @param nombreArea
     * @param correoArea
     * @return null si no se creo.
     */
    public Area nuevaArea(String nombreArea, String correoArea) {
        if (!existeNombreArea(nombreArea)) {
            Area area = new Area(nombreArea, correoArea);
            area.setId(repoArea.create(area).getId());
            if (area.getId() != -1) {
                return area;
            }
        }
        return null;
    }
    
    /**
     * Cambia los valores de area y actualiza la base de datos. Se verifica si
     * existe el nombre del area en la base de datos.
     *
     * @param idArea
     * @param nombreArea
     * @param correoArea
     * @return Retorna -1 si no se actualizo. Retorna el IdArea si se actualizo.
     */
    public int editarArea(int idArea, String nombreArea, String correoArea) {
        if (!existeNombreArea(idArea, nombreArea)) {
            Area area = repoArea.find(idArea);
            area.setNombre(nombreArea);
            area.setCorreo(correoArea);
            return repoArea.update(area).getId();
        }
        return -1;
    }
    
    /**
     * Elimina el area de la base de datos. Actualiza la relaciones Empesa. Se
     * comprueba que no tenga acciones y fortalezas relacionadas.
     *
     * @param idArea
     * @return Retorna el id del area si se elimino. Retorna -1 si no se
     * elimino.
     */
    public int eliminarArea(int idArea) {
        Area area = repoArea.find(idArea);
        if (area.getAccionesArea().isEmpty() && area.getFortalezasArea().isEmpty()) {
            repoArea.delete(area);
            return 1;
        }
        return -1;
    }
    
    /**
     * Comprueba si el nombre especificado para el area ya existe en la base de
     * datos. Se ignoan las mayusculas y minusculas.
     *
     * @param nombreArea
     * @param idArea
     * @return <b>True</b> Si existe. <b>False</b> si no existe.
     */
    private boolean existeNombreArea(int idArea, String nombreArea) {
        return repoArea.findAll().stream()
                .anyMatch(area -> area.getNombre().equalsIgnoreCase(nombreArea) && area.getId() != idArea);
    }
    
    /**
     * Comprueba si el nombre especificado para el area ya existe en la base de
     * datos. Se ignoan las mayusculas y minusculas.
     *
     * @param nombreArea
     * @return <b>True</b> Si existe. <b>False</b> si no existe.
     */
    private boolean existeNombreArea(String nombreArea) {
        return repoArea.findAll().stream()
                .anyMatch(area -> area.getNombre().equalsIgnoreCase(nombreArea));
    }
    
    /*
    CODIFICACION
    */
    /**
     * *
     * Crea una nueva codificacion y la persiste en la base de datos. Se
     * verifica si existe el nombre de la codificacion en la base de datos.
     *
     * @param nombreCodificacion
     * @param descripcionCodificacion
     * @return null si no se creo.
     */
    public Codificacion nuevaCodificacion(String nombreCodificacion, String descripcionCodificacion) {
        if (!existeNombreCodificacion(nombreCodificacion)) {
            Codificacion codificacion = new Codificacion(nombreCodificacion, descripcionCodificacion);
            codificacion.setId(repoCodificacion.create(codificacion).getId());
            if (codificacion.getId() != -1) {
                return codificacion;
            }
        }
        return null;
    }
    
    /**
     * Cambia los valores de Codificacion y actualiza la base de datos. Se
     * verifica si existe el nombre de la codificacion en la base de datos.
     *
     * @param idCodificacion
     * @param nombreCodificacion
     * @param descripcionCodificacion
     * @return Retorna -1 si no se actualizo. Retorna el IdCodificacion si se
     * actualizo.
     */
    public int editarCodificacion(int idCodificacion, String nombreCodificacion, String descripcionCodificacion) {
        if (!existeNombreCodificacion(idCodificacion, nombreCodificacion)) {
            Codificacion codificacion = repoCodificacion.find(idCodificacion);
            codificacion.setNombre(nombreCodificacion);
            codificacion.setDescripcion(descripcionCodificacion);
            return repoCodificacion.update(codificacion).getId();
        }
        return -1;
    }
    
    /**
     * Elimina la codificacion de la base de datos.
     * Se comprueba que no tenga acciones relacionadas.     *
     * @param idCodificacion
     * @return Retorna el id de la codificacion si se elimino. Retorna -1 si no
     * se elimino.
     */
    public int eliminarCodificacion(int idCodificacion) {
        Codificacion codificacion = repoCodificacion.find(idCodificacion);
        if (codificacion.getAccionesCodificacion().isEmpty()) {
            repoCodificacion.delete(codificacion);
            return 1;
        }
        return -1;
    }
    
    /**
     * Comprueba si el nombre especificado para la codificacion ya existe en la
     * base de datos. Se ignoar las mayusculas y minusculas.
     *
     * @param nombreCodificacion
     * @param idCodificacion
     * @return <b>True</b> Si existe. <b>False</b> si no existe.
     */
    private boolean existeNombreCodificacion(int idCodificacion, String nombreCodificacion) {
        return repoCodificacion.findAll().stream()
                .anyMatch(codificacion -> codificacion.getNombre().equalsIgnoreCase(nombreCodificacion) && codificacion.getId() != idCodificacion);
    }
    
    /**
     * Comprueba si el nombre especificado para la codificacion ya existe en la
     * base de datos. Se ignoar las mayusculas y minusculas.
     *
     * @param nombreCodificacion
     * @return <b>True</b> Si existe. <b>False</b> si no existe.
     */
    private boolean existeNombreCodificacion(String nombreCodificacion) {
        return repoCodificacion.findAll().stream()
                .anyMatch(codificacion -> codificacion.getNombre().equalsIgnoreCase(nombreCodificacion));
    }
    
    /*
    DETECCION Y TIPO DE DETECCION
    */
    /**
     * *
     * Crea una nueva deteccion y la persiste en la base de datos. * Se
     * comprueba que no existe una deteccion con el mismo nombre,
     *
     * @param nombreDeteccion
     * @param tipoDeteccion
     * @return null si no se creo.
     */
    public Deteccion nuevaDeteccion(String nombreDeteccion, TipoDeteccion tipoDeteccion) {
        if (!existeNombreDeteccion(nombreDeteccion)) {
            Deteccion deteccion = new Deteccion(nombreDeteccion, tipoDeteccion);
            int id = repoDeteccion.create(deteccion).getId();
            deteccion.setId(id);
            if (deteccion.getId() != -1) {
                return deteccion;
            }
        }
        return null;
    }
    
    /**
     * Cambia los valores de Deteccion y actualiza la base de datos. Se
     * comprueba que no existe una deteccion con el mismo nombre.
     *
     * @param idDteccion
     * @param nombreDeteccion
     * @param tipoDeteccion
     * @return Retorna -1 si no se actualizo. Retorna el IdDeteccion si se
     * actualizo.
     */
    public int editarDeteccion(int idDteccion, String nombreDeteccion, TipoDeteccion tipoDeteccion) {
        if (!existeNombreDeteccion(idDteccion, nombreDeteccion)) {
            Deteccion deteccion = repoDeteccion.find(idDteccion);
            // comprobar si se cambia el tipo de deteccion para 'ahorrar' llamada a la base de datos.
            if (!deteccion.getTipoDeDeteccion().equals(tipoDeteccion)) {
                deteccion.setTipoDeDeteccion(tipoDeteccion);
            }
            deteccion.setNombre(nombreDeteccion);
            return repoDeteccion.update(deteccion).getId();
        }
        return -1;
    }
    
    /**
     * Elimina la deteccion de la base de datos. Se comprueba que no tenga
     * acciones y fortalezas relacionadas.
     *
     * @param idDeteccion
     * @return Retorna el id de la deteccion si se elimino. Retorna -1 si no se
     * elimino.
     */
    public int eliminarDeteccion(int idDeteccion) {
        Deteccion det = repoDeteccion.find(idDeteccion);
        if (det.getFortalezasDeteccion().isEmpty() && det.getAccionesDeteccion().isEmpty()) {
            repoDeteccion.delete(det);
            return 1;
        }
        return -1;
    }
    
    /**
     * Comprueba si el nombre especificado para la deteccion ya existe en la
     * base de datos. Se ignoar las mayusculas y minusculas.
     *
     * @param nombreDeteccion
     * @param idDeteccion
     * @return <b>True</b> Si existe. <b>False</b> si no existe.
     */
    private boolean existeNombreDeteccion(int idDeteccion, String nombreDeteccion) {
        return repoDeteccion.findAll().stream()
                .anyMatch(deteccion -> deteccion.getNombre().equalsIgnoreCase(nombreDeteccion) && deteccion.getId() != idDeteccion);
    }
    
    /**
     * Comprueba si el nombre especificado para la deteccion ya existe en la
     * base de datos. Se ignoar las mayusculas y minusculas.
     *
     * @param nombreDeteccion
     * @return <b>True</b> Si existe. <b>False</b> si no existe.
     */
    private boolean existeNombreDeteccion(String nombreDeteccion) {
        return repoDeteccion.findAll().stream()
                .anyMatch(deteccion -> deteccion.getNombre().equalsIgnoreCase(nombreDeteccion));
    }
    
    public Responsabilidad crearResponsabilidad(String nombre){
        Responsabilidad responsabilidad = new Responsabilidad(nombre);
        return repoResponsabilidades.create(responsabilidad);
    }
    
    public int editarResponsabilidad(int idResponsabilidad, String nuevoNombre){
        Responsabilidad responsabilidad = repoResponsabilidades.find(idResponsabilidad);
        responsabilidad.setNombre(nuevoNombre);
        try{
            repoResponsabilidades.update(responsabilidad);
            return 1;
        }catch(Exception ex){}
        return -1;
    }
    //TODO: revisar metodo, no se esta enlazando al usuario y en la base de datos se crea doble
    public Responsable crearResponsable(int idResponsabilidad, int idUsuario){
        Usuario usuario = repoUsuario.find(idUsuario);
        Responsabilidad responsabilidad= repoResponsabilidades.find(idResponsabilidad);
        return repoResponsable.update(responsabilidad.crearResponsable(usuario));
    }
    
    public int darBajaResponsable(int idResponsabilidad, int idResponsable){
        Responsabilidad responsabilidad = repoResponsabilidades.find(idResponsabilidad);
        if(responsabilidad.darBajaResponsable(idResponsable, new Date()) == 1){
            try{
                repoResponsabilidades.update(responsabilidad);
                return 1;
            }catch(Exception ex){}
        }
        return -1;
    }
    
    public int eliminarResponsable(int idResponsable) {
        Responsable responsable = repoResponsable.find(idResponsable);
        if (responsable.getActividadesResponsable().isEmpty() && responsable.getComprobacionesResponsable().isEmpty()) {
            repoResponsable.delete(responsable);
            return 1;
        }
        return -1;
    }

    public int eliminarResponsabilidad(int idResponsabilidadSeleccionada) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
  
}
