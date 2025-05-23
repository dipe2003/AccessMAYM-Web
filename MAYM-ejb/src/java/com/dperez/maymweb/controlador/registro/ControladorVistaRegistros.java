/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maymweb.controlador.registro;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.TipoAccion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.empresa.Empresa;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.modelo.usuario.Usuario;
import com.dperez.maymweb.persistencia.FabricaRepositorio;
import com.dperez.maymweb.persistencia.RepositorioPersistencia;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Diego
 */
public class ControladorVistaRegistros implements Serializable {

    private final RepositorioPersistencia<Accion> repoAccion;
    private final RepositorioPersistencia<Area> repoArea;
    private final RepositorioPersistencia<Deteccion> repoDeteccion;
    private final RepositorioPersistencia<Codificacion> repoCodificacion;
    private final RepositorioPersistencia<Responsable> repoResponsable;
    private final RepositorioPersistencia<Usuario> repoUsuario;
    private final RepositorioPersistencia<Fortaleza> repoFortalezas;
    private final RepositorioPersistencia<Responsabilidad> repoResponsabilidades;
    private final RepositorioPersistencia<Empresa> repoEmpresa;

    //  Constructores
    public ControladorVistaRegistros() {
        repoAccion = FabricaRepositorio.getRespositorioAcciones();
        repoArea = FabricaRepositorio.getRepositorioAreas();
        repoDeteccion = FabricaRepositorio.getRepositorioDetecciones();
        repoCodificacion = FabricaRepositorio.getRepositorioCodificaciones();
        repoResponsable = FabricaRepositorio.getRepositorioResponsables();
        repoUsuario = FabricaRepositorio.getRepositorioUsuarios();
        repoFortalezas = FabricaRepositorio.getRepositorioFortalezas();
        repoResponsabilidades = FabricaRepositorio.getRepositorioResponsabilidades();
        repoEmpresa = FabricaRepositorio.getRepositorioEmpresa();
    }
    
    //<editor-fold desc="EMPRESAS">
    
    public Empresa getEmpresa(int idEmpresa){
        return repoEmpresa.find(idEmpresa);
    }
    
    public List<Empresa> listarEmpresas(){
        return repoEmpresa.findAll();
    }
    
    //</editor-fold>

    //<editor-fold desc="ACCIONES">
    /**
     * Devuelve una lista con todas las acciones registradas que se encuentran
     * en el estado especificado. Si EstadoAccion es null se devuelven todas las
     * acciones registradas.
     *
     * @param estado
     * @return
     */
    public List<Accion> listarAcciones(Estado estado) {
        if (estado != null) {
            return repoAccion.findAll().stream()
                    .filter(accion -> accion.getEstadoDeAccion() == estado)
                    .collect(Collectors.toList());
        }
        return repoAccion.findAll();
    }

    /**
     * Devuelve una lista con todas las acciones del tipo especificado
     * registradas que se encuentran en el estado indicado. Si EstadoAccion es
     * null se devuelven todas las acciones registradas del tipo indicado. Si
     * EstadoAccion es null y TipoAccion es null se devuelven todas las accines
     * registradas.
     *
     * @param estado
     * @param tipo
     * @return
     */
    public List<Accion> listarAcciones(Estado estado, TipoAccion tipo) {
        if (estado != null && tipo != null) {
            return repoAccion.findAll().stream()
                    .filter(accion -> accion.getEstadoDeAccion() == estado && accion.getClass().getName().equals(tipo.toString()))
                    .collect(Collectors.toList());
        } else {
            if (estado == null && tipo != null) {
                return repoAccion.findAll().stream()
                        .filter(accion -> accion.getClass().getSimpleName().equalsIgnoreCase(tipo.toString()))
                        .collect(Collectors.toList());
            }
        }
        return repoAccion.findAll();
    }

    /**
     * Devuelve la accion indicada por su id
     *
     * @param idAccion
     * @return
     */
    public Accion getAccion(int idAccion) {
        return repoAccion.find(idAccion);
    }

    /**
     * Devuelve la fortaleza indicada por su id
     *
     * @param idFortaleza
     * @return Retorna null si no se encontro fortaleza.
     */
    public Fortaleza getFortaleza(int idFortaleza) {
        return repoFortalezas.find(idFortaleza);
    }

    /**
     * Lista todas las fortalezas registradas
     *
     * @return
     */
    public List<Fortaleza> listarFortalezas() {
        return repoFortalezas.findAll();
    }

    //</editor-fold>
    //<editor-fold desc="USUARIOS">
    /**
     * Devuelve el usuario especificado por su id
     *
     * @param idUsuario
     * @return Null si no existe el usuario.
     */
    public Usuario getUsuario(int idUsuario) {
        return repoUsuario.find(idUsuario);
    }

    /**
     * Devuelve los usuarios de la empresa especificada segun su fecha de baja.
     *
     * @param soloVigentes True: si no fueron dados de baja (FechaBaja == null).
     * @return Lista de Usuarios.
     */
    public List<Usuario> listarUsuarios(boolean soloVigentes) {
        if (soloVigentes == true) {
            return repoUsuario.findAll().stream()
                    .filter(u -> u.getFechaBaja() == null)
                    .collect(Collectors.toList());
        }
        return repoUsuario.findAll();
    }

    /**
     * Lista todos los usuarios de la empresa seleccionada por id.
     *
     * @return
     */
    public List<Usuario> listarUsuarios() {
        return repoUsuario.findAll();
    }

    //</editor-fold>
    
    //<editor-fold desc="DETECCIONES / CODIFICACIONES / AREAS">
    /**
     * Devuelve una lista de detecciones.
     *
     * @return
     */
    public List<Deteccion> listarDetecciones() {
        return repoDeteccion.findAll();
    }

    /**
     * Devuelve las codificaciones.
     *
     * @return Lista de codificaciones.
     */
    public List<Codificacion> listarCodificaciones() {
        return repoCodificacion.findAll();
    }

    /**
     * Devuelve una todas las areas de una empresa.
     *
     * @return lista de areas.
     */
    public List<Area> listarAreas() {
        return repoArea.findAll();
    }
    //</editor-fold>

    //<editor-fold desc="RESPONSABLES / RESPONSABILIDADES">
    /*
    Responsabilidades
     */
    public List<Responsabilidad> listarResponsabilidades() {
        return repoResponsabilidades.findAll();
    }

    /*
    Responsables
     */
    public List<Responsable> listarResponsables(boolean soloVigentes) {
        if (soloVigentes) {
            return repoResponsable.findAll().stream()
                    .filter(r -> r.getUsuarioResponsable().isVigente() == true)
                    .collect(Collectors.toList());
        }
        return repoResponsable.findAll();
    }

    //</editor-fold>
}
