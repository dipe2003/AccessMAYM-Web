package com.dperez.maymweb.persistencia;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.empresa.Empresa;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.modelo.usuario.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;


public class FabricaRepositorio {
    
    static final EntityManager entityManager = Persistence.createEntityManagerFactory("MAYM-ejbPU").createEntityManager();
    
    public static RepositorioPersistencia<Usuario> getRepositorioUsuarios() {
        return new RepositorioPersistencia<>(Usuario.class, entityManager);
    }
    
    public static RepositorioPersistencia<Codificacion> getRepositorioCodificaciones() {
        return new RepositorioPersistencia<>(Codificacion.class, entityManager);
    }
    
    public static RepositorioPersistencia<Area> getRepositorioAreas() {
        return new RepositorioPersistencia<>(Area.class,entityManager);
    }
    
    public static RepositorioPersistencia<Deteccion> getRepositorioDetecciones() {
        return new RepositorioPersistencia<>(Deteccion.class,entityManager);
    }
    
    public static RepositorioPersistencia<Responsable> getRepositorioResponsables() {
        return new RepositorioPersistencia<>(Responsable.class, entityManager);
    }
    
    public static RepositorioPersistencia<Responsabilidad> getRepositorioResponsabilidades(){
        return new RepositorioPersistencia<>(Responsabilidad.class, entityManager);
    }
    
    public static RepositorioPersistencia<Accion> getRespositorioAcciones(){
        return new RepositorioPersistencia<>(Accion.class, entityManager);
    }
    
    public static RepositorioPersistencia<Fortaleza> getRepositorioFortalezas(){
        return new RepositorioPersistencia<>(Fortaleza.class, entityManager);
    } 
    
    public static RepositorioPersistencia<Empresa> getRepositorioEmpresa(){
        return new RepositorioPersistencia<>(Empresa.class, entityManager);
    } 
  
}
