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
import com.dperez.maymweb.modelo.empresa.Empresa;
import com.dperez.maymweb.modelo.empresa.OpcionesApariencia;
import com.dperez.maymweb.modelo.empresa.OpcionesCorreo;
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
    private final RepositorioPersistencia<Empresa> repoEmpresa;

    private final ControladorSeguridad cSeg;

    public ControladorConfiguracion() {
        this.repoCodificacion = FabricaRepositorio.getRepositorioCodificaciones();
        this.repoArea = FabricaRepositorio.getRepositorioAreas();
        this.repoDeteccion = FabricaRepositorio.getRepositorioDetecciones();
        this.repoUsuario = FabricaRepositorio.getRepositorioUsuarios();
        this.repoResponsable = FabricaRepositorio.getRepositorioResponsables();
        this.repoResponsabilidades = FabricaRepositorio.getRepositorioResponsabilidades();
        this.repoEmpresa = FabricaRepositorio.getRepositorioEmpresa();
        cSeg = new ControladorSeguridad();
    }

    //<editor-fold desc="EMPRESA">
    /**
     * Crea un nueva Empresa y lo persiste en la base de datos.
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
        Empresa empresa = new Empresa(nombre, direccion, telefono, movil, correo, nombreExtra);
        int id = repoEmpresa.create(empresa).getId();
        return id > 0 ? empresa : null;
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
        Empresa empresa = repoEmpresa.find(idEmpresa);
        if (empresa != null) {
            empresa.setNombre(nombre);
            empresa.setDireccion(direccion);
            empresa.setTelefono(telefono);
            empresa.setMovil(movil);
            empresa.setCorreo(correo);
            empresa.setNombre(nombre);
            return repoEmpresa.update(empresa).getId();
        }
        return -1;
    }

    /**
     * Setea la ubicacion del logo de la empresa.
     *
     * @param idEmpresa
     * @param ubicacionLogo
     * @return
     */
    public int setUbicacionLogoEmpresa(int idEmpresa, String ubicacionLogo) {
        Empresa empresa = repoEmpresa.find(idEmpresa);
        if (empresa != null) {
            empresa.setUbicacionLogo(ubicacionLogo);
            return repoEmpresa.update(empresa).getId();
        }
        return -1;
    }

    /**
     * Setea la ubicacion del logo adicional de la empresa.
     *
     * @param idEmpresa
     * @param ubicacionLogo
     * @return
     */
    public int setUbicacionLogoAdicionaleEmpresa(int idEmpresa, String ubicacionLogo) {
        Empresa empresa = repoEmpresa.find(idEmpresa);
        if (empresa != null) {
            empresa.setUbicacionLogoAdicional(ubicacionLogo);
            return repoEmpresa.update(empresa).getId();
        }
        return -1;
    }

    /**
     * Setea las opciones de apariencia del sistema asociadas a la Empresa.
     *
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
        Empresa empresa = repoEmpresa.find(idEmpresa);
        if (empresa != null) {
            OpcionesApariencia ops = empresa.getOpcionesSistema().getOpcionesApariencia();
            if (!colorBody.isBlank()) {
                ops.setColorBody(colorBody);
            }
            if (!colorBoton.isBlank()) {
                ops.setColorBoton(colorBoton);
            }
            if (!colorFuentePanelEncabezado.isBlank()) {
                ops.setColorFuentePanelEncabezado(colorFuentePanelEncabezado);
            }
            if (!colorFuentePanelTitulo.isBlank()) {
                ops.setColorFuentePanelTitulo(colorFuentePanelTitulo);
            }
            if (!colorInferiorPanelTitulo.isBlank()) {
                ops.setColorInferiorPanelTitulo(colorInferiorPanelTitulo);
            }
            if (!colorPanelTitulo.isBlank()) {
                ops.setColorPanelTitulo(colorPanelTitulo);
            }
            if (!colorSuperiorPanelTitulo.isBlank()) {
                ops.setColorSuperiorPanelTitulo(colorSuperiorPanelTitulo);
            }
            return repoEmpresa.update(empresa).getId();
        }
        return -1;
    }

    /**
     * Setea las opciones de apariencia del sistema asociadas a la Empresa.
     *
     * @param idEmpresa
     * @param mailFrom
     * @param mailUser
     * @param mailPass
     * @param mailPort
     * @param mailTLS
     *
     * @return
     */
    public int setConfiguracionCorreo(int idEmpresa, String mailFrom, String mailUser, String mailPass, String mailHostSMTP, int mailPort, boolean mailTLS) {
        Empresa empresa = repoEmpresa.find(idEmpresa);
        if (empresa != null) {
            OpcionesCorreo ops = empresa.getOpcionesSistema().getOpcionesCorreo();
            ops.setMailFrom(mailFrom);
            ops.setMailPass(mailPass);
            ops.setMailHostSMTP(mailHostSMTP);
            ops.setMailPort(mailPort);
            ops.setMailTLS(mailTLS);
            ops.setMailUser(mailUser);
            return repoEmpresa.update(empresa).getId();
        }
        return -1;
    }

    /**
     * Setea estado de alertas. PRE: las opciones de correo deben estar
     * seteadas.
     *
     * @param idEmpresa
     * @param setAlertas
     * @return
     */
    public int setAlertas(int idEmpresa, boolean setAlertas) {
        Empresa empresa = repoEmpresa.find(idEmpresa);
        if (empresa != null) {

            if (empresa.getOpcionesSistema() != null && empresa.getOpcionesSistema().getOpcionesCorreo() != null) {
                OpcionesCorreo ops = empresa.getOpcionesSistema().getOpcionesCorreo();
                ops = empresa.getOpcionesSistema().getOpcionesCorreo();
                ops.setAlertasActivadas(setAlertas);
                return repoEmpresa.update(empresa).getId();
            }
        }
        return -1;
    }
    //</editor-fold>

    //<editor-fold desc="USUARIOS y CREDENCIALES">
    /**
     * Crea un nuevo usuario y lo persiste en la base de datos.El usuario creado
     * no recibe alertas.
     *
     * @param idUsuario
     * @param nombreUsuario
     * @param apellidoUsuario
     * @param correoUsuario
     * @param password
     * @param permisoUsuario
     * @param idArea
     * @return null si no se creo.
     */
    public Usuario nuevoUsuario(int idUsuario, String nombreUsuario, String apellidoUsuario, String correoUsuario,
            String password, EnumPermiso permisoUsuario, int idArea, int idEmpresa) {
        Empresa empresa = repoEmpresa.find(idEmpresa);
        Usuario usuario = new Usuario(nombreUsuario, apellidoUsuario, correoUsuario, false, permisoUsuario, empresa);
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
     * @param idEmpresa
     * @return -1 si no se actualizo.
     */
    public int cambiarDatosUsuario(int idUsuario, String nombreUsuario, String apellidoUsuario, String correoUsuario,
            EnumPermiso permisoUsuario, boolean recibeAlertas, int idArea, int idEmpresa) {
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
        if(usuario.getEmpresaUsuario().getId()!= idEmpresa){
            Empresa empresa = repoEmpresa.find(idEmpresa);
            usuario.setEmpresaUsuario(empresa);
            repoEmpresa.update(empresa);
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

    //</editor-fold>
    //<editor-fold desc="AREAS">
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

    //</editor-fold>
    //<editor-fold desc="CODIFICACIONES">
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
     * Elimina la codificacion de la base de datos. Se comprueba que no tenga
     * acciones relacionadas.
     *
     *
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

    //</editor-fold>
    //<editor-fold desc="DETECCION Y TIPO DE DETECCION">
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

    //</editor-fold>
    //<editor-fold desc="RESPONSABLES Y RESPONSABILIDADES">
    public Responsabilidad crearResponsabilidad(String nombre) {
        Responsabilidad responsabilidad = new Responsabilidad(nombre);
        return repoResponsabilidades.create(responsabilidad);
    }

    public int editarResponsabilidad(int idResponsabilidad, String nuevoNombre) {
        Responsabilidad responsabilidad = repoResponsabilidades.find(idResponsabilidad);
        responsabilidad.setNombre(nuevoNombre);
        try {
            repoResponsabilidades.update(responsabilidad);
            return 1;
        } catch (Exception ex) {
        }
        return -1;
    }

    //TODO: revisar metodo, no se esta enlazando al usuario y en la base de datos se crea doble
    public Responsable crearResponsable(int idResponsabilidad, int idUsuario) {
        Usuario usuario = repoUsuario.find(idUsuario);
        Responsabilidad responsabilidad = repoResponsabilidades.find(idResponsabilidad);
        Responsable responsable = responsabilidad.crearResponsable(usuario);
        try {
            repoUsuario.update(usuario);
            return responsable;
        } catch (Exception ex) {
        }
        return null;
    }

    public int darBajaResponsable(int idResponsabilidad, int idResponsable) {
        Responsabilidad responsabilidad = repoResponsabilidades.find(idResponsabilidad);
        if (responsabilidad.darBajaResponsable(idResponsable, new Date()) == 1) {
            try {
                repoResponsabilidades.update(responsabilidad);
                return 1;
            } catch (Exception ex) {
            }
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
    //</editor-fold>
}
